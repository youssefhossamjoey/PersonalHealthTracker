package com.example.personalhealthtracker.services.impl;

import com.example.personalhealthtracker.domain.dto.RecipeSummary;
import com.example.personalhealthtracker.domain.entities.RecipeEntity;
import com.example.personalhealthtracker.repositories.FoodItemRepository;
import com.example.personalhealthtracker.repositories.RecipeRepository;
import com.example.personalhealthtracker.services.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor

public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final FoodItemRepository foodItemRepository;

    @Override
    public RecipeEntity createRecipe(RecipeEntity recipeEntity) {
        return recipeRepository.save(recipeEntity);
    }

    @Override
    public List<RecipeEntity> findAll(UUID ownerId) {
        return StreamSupport.stream(recipeRepository.findAllByOwner_Id(ownerId).spliterator(),false).collect(Collectors.toList());
    }

    @Override
    public Page<RecipeEntity> findAll(UUID ownerId, Pageable pageable) {
        return recipeRepository.findAllByOwner_Id(
                ownerId,
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "name"))
                ));
    }

    @Override
    public Optional<RecipeEntity> findOne(UUID ownerId, UUID id) {
        return recipeRepository.findByIdAndOwner_Id(id,ownerId);
    }

    @Override
    public Optional<RecipeSummary> findOneSummary(UUID ownerId, UUID id) {
        return recipeRepository.getRecipeSummary(ownerId,id);
    }

    @Override
    public void delete(UUID ownerId, UUID id) {
        recipeRepository.deleteByIdAndOwner_Id(id,ownerId);
    }

    @Override
    public void deleteBatch(UUID ownerId, List<UUID> ids) {
//        recipeRepository.deleteAllByIdsAndOwner(ownerId, ids);
        recipeRepository.deleteAllByIdInAndOwnerId(ids, ownerId);
    }

    @Override
    public Page<RecipeEntity> findAll(UUID ownerId, String q, Pageable pageable) {
        return recipeRepository.search(
                ownerId,
                q,
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "name"))
                ));
    }

    @Override
    public Page<RecipeSummary> findAllSummary(UUID ownerId, String q, Pageable pageable) {
        return recipeRepository.searchRecipeSummary(
                ownerId,
                q,
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "name"))
                ));
    }
    @Override
    public Page<RecipeSummary> findAllSummary(UUID ownerId, Pageable pageable) {
        return recipeRepository.searchRecipeSummary(
                ownerId,
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "name"))
                ));
    }
}

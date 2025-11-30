package com.example.personalhealthtracker.services;

import com.example.personalhealthtracker.domain.dto.RecipeSummary;
import com.example.personalhealthtracker.domain.entities.RecipeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecipeService {

    RecipeEntity createRecipe(RecipeEntity recipeEntity);

    List<RecipeEntity> findAll(UUID ownerId);

    Page<RecipeEntity> findAll(UUID ownerId, Pageable pageable);

    Optional<RecipeEntity> findOne(UUID ownerId, UUID id);

    void delete(UUID ownerId,UUID id);

    void deleteBatch(UUID ownerId,List<UUID> ids);

    Page<RecipeEntity> findAll(UUID id, String q, Pageable name);

    Optional<RecipeSummary> findOneSummary(UUID ownerId, UUID id);

    Page<RecipeSummary> findAllSummary(UUID ownerId, String q, Pageable pageable);

    Page<RecipeSummary> findAllSummary(UUID ownerId, Pageable pageable);
}

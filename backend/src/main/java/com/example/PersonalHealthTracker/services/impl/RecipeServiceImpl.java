package com.example.PersonalHealthTracker.services.impl;

import com.example.PersonalHealthTracker.domain.entities.RecipeEntity;
import com.example.PersonalHealthTracker.repositories.RecipeRepository;
import com.example.PersonalHealthTracker.services.RecipeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RecipeServiceImpl implements RecipeService {
    private RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public RecipeEntity creteRecipe(RecipeEntity recipe) {
       return recipeRepository.save(recipe);
    }

    @Override
    public List<RecipeEntity> findAll() {
        return StreamSupport
                .stream(recipeRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Page<RecipeEntity> findAll(Pageable page) {
        return recipeRepository.findAll(page) ;
    }
    @Override
    public List<RecipeEntity> findAllByName(String name) {
        return StreamSupport
                .stream(recipeRepository.findByRecipeNameContainingIgnoreCase(name).spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Page<RecipeEntity> findAllByName(String name, Pageable page) {
        return recipeRepository.findByRecipeNameContainingIgnoreCase(name,page) ;
    }

    @Override
    public Optional<RecipeEntity> findOne(Long id) {
        return recipeRepository.findById(id);
    }
}

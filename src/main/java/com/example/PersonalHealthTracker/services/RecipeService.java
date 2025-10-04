package com.example.PersonalHealthTracker.services;

import com.example.PersonalHealthTracker.domain.entities.RecipeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface RecipeService {

    RecipeEntity creteRecipe(RecipeEntity recipeEntity);
    List<RecipeEntity> findAll();
    Page<RecipeEntity> findAll(Pageable page);

    List<RecipeEntity> findAllByName(String name);
    Page<RecipeEntity> findAllByName(String name,Pageable page);

    Optional<RecipeEntity> findOne(Long id);
}

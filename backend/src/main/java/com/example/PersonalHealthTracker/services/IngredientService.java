package com.example.PersonalHealthTracker.services;

import com.example.PersonalHealthTracker.domain.entities.IngredientEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface IngredientService {
    IngredientEntity createIngredient(IngredientEntity ingredientEntity);

    List<IngredientEntity> batchCreateIngredients(List<IngredientEntity> ingredientEntityList);
    List<IngredientEntity> findAll();

    Optional<IngredientEntity> findOne(Long id);

}

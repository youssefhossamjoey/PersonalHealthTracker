package com.example.PersonalHealthTracker.controllers;

import com.example.PersonalHealthTracker.domain.dto.RecipeDto;
import com.example.PersonalHealthTracker.domain.entities.IngredientEntity;
import com.example.PersonalHealthTracker.domain.entities.RecipeEntity;
import com.example.PersonalHealthTracker.mappers.Impl.RecipeMapper;
import com.example.PersonalHealthTracker.services.IngredientService;
import com.example.PersonalHealthTracker.services.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Set;

@Controller
public class RecipeController {
    private RecipeService recipeService;
    private RecipeMapper recipeMapper;
    private final IngredientService ingredientService;

    public RecipeController(RecipeService recipeService, RecipeMapper recipeMapper, IngredientService ingredientService) {
        this.recipeService = recipeService;
        this.recipeMapper = recipeMapper;
        this.ingredientService = ingredientService;
    }
    @PostMapping(path = "/recipes")
    public ResponseEntity<RecipeDto> createItem(@RequestBody RecipeDto recipe){
        RecipeEntity recipeEntity = recipeMapper.mapFrom(recipe);

        List<IngredientEntity> ingredients = recipeEntity.getRecipeIngredients();
        recipeEntity.setRecipeIngredients(null);

        RecipeEntity savedRecipeEntity= recipeService.creteRecipe(recipeEntity);

        if (ingredients != null && !ingredients.isEmpty()) {
            for (IngredientEntity ingredient : ingredients) {
                ingredient.setRecipe(savedRecipeEntity);
            }
            ingredientService.batchCreateIngredients(ingredients);
            savedRecipeEntity.setRecipeIngredients(ingredients);
        }

        return new ResponseEntity<>(recipeMapper.mapTo(savedRecipeEntity), HttpStatus.CREATED);
    }
}

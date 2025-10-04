package com.example.PersonalHealthTracker.controllers;

import com.example.PersonalHealthTracker.mappers.Impl.IngredientMapper;
import com.example.PersonalHealthTracker.services.IngredientService;
import org.springframework.stereotype.Controller;

@Controller
public class IngredientController {
    private IngredientService ingredientService;
    private IngredientMapper ingredientMapper;

    public IngredientController(IngredientService ingredientService, IngredientMapper ingredientMapper) {
        this.ingredientService = ingredientService;
        this.ingredientMapper = ingredientMapper;
    }


}

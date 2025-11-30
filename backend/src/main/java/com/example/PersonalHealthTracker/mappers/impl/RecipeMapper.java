package com.example.personalhealthtracker.mappers.impl;

import com.example.personalhealthtracker.domain.dto.Recipe;
import com.example.personalhealthtracker.domain.entities.RecipeEntity;
import com.example.personalhealthtracker.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RecipeMapper implements Mapper<RecipeEntity, Recipe> {

    private final ModelMapper modelMapper;

    public RecipeMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Recipe mapTo(RecipeEntity recipeEntity) {
        return modelMapper.map(recipeEntity,Recipe.class);
    }

    @Override
    public RecipeEntity mapFrom(Recipe recipe) {
        return modelMapper.map(recipe,RecipeEntity.class);
    }
}

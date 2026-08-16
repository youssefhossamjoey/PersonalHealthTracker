package com.example.personalhealthtracker.mappers.impl;

import com.example.personalhealthtracker.domain.dto.RecipeDetail;
import com.example.personalhealthtracker.domain.entities.RecipeEntity;
import com.example.personalhealthtracker.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RecipeMapper implements Mapper<RecipeEntity, RecipeDetail> {

    private final ModelMapper modelMapper;

    public RecipeMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public RecipeDetail mapTo(RecipeEntity recipeEntity) {
        return modelMapper.map(recipeEntity, RecipeDetail.class);
    }

    @Override
    public RecipeEntity mapFrom(RecipeDetail recipeDetail) {
        return modelMapper.map(recipeDetail,RecipeEntity.class);
    }
}

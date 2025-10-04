package com.example.PersonalHealthTracker.mappers.Impl;

import com.example.PersonalHealthTracker.domain.dto.RecipeDto;
import com.example.PersonalHealthTracker.domain.entities.RecipeEntity;
import com.example.PersonalHealthTracker.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RecipeMapper implements Mapper<RecipeEntity, RecipeDto> {
    private ModelMapper modelMapper;

    public RecipeMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public RecipeDto mapTo(RecipeEntity recipeEntity) {
        return modelMapper.map(recipeEntity, RecipeDto.class);
    }

    @Override
    public RecipeEntity mapFrom(RecipeDto recipeDto) {
        return modelMapper.map(recipeDto, RecipeEntity.class);
    }
}

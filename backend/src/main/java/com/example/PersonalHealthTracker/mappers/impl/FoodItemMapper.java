package com.example.personalhealthtracker.mappers.impl;

import com.example.personalhealthtracker.domain.dto.FoodItem;
import com.example.personalhealthtracker.domain.entities.FoodItemEntity;
import com.example.personalhealthtracker.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class FoodItemMapper implements Mapper<FoodItemEntity,FoodItem> {

    private final ModelMapper modelMapper;

    public FoodItemMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public FoodItem mapTo(FoodItemEntity foodItemEntity) {
        return modelMapper.map(foodItemEntity, FoodItem.class);
    }

    @Override
    public FoodItemEntity mapFrom(FoodItem foodItem) {
       return modelMapper.map(foodItem, FoodItemEntity.class);
    }
}

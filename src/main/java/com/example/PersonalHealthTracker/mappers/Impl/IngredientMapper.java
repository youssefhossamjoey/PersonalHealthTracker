package com.example.PersonalHealthTracker.mappers.Impl;

import com.example.PersonalHealthTracker.domain.dto.IngredientDto;
import com.example.PersonalHealthTracker.domain.entities.IngredientEntity;
import com.example.PersonalHealthTracker.domain.entities.ItemEntity;
import com.example.PersonalHealthTracker.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class IngredientMapper implements Mapper<IngredientEntity, IngredientDto> {
    @Override
    public IngredientDto mapTo(IngredientEntity ingredientEntity) {
        if (ingredientEntity == null) return null;
        double grams = ingredientEntity.getGrams();
        Long id =ingredientEntity.getId();
        return IngredientDto.builder()
                .id(id)
                .recipeId(ingredientEntity.getRecipe().getId())
                .itemId(ingredientEntity.getItem().getId())
                .itemName(ingredientEntity.getItem().getItemName())
                .grams(grams)
                .build();
    }

    @Override
    public IngredientEntity mapFrom(IngredientDto ingredientDto) {
        if (ingredientDto == null) return null;

        IngredientEntity entity = new IngredientEntity();
        entity.setGrams(ingredientDto.getGrams());

        ItemEntity item = new ItemEntity();
        item.setId(ingredientDto.getItemId());
        entity.setItem(item);
        return entity;

    }
}

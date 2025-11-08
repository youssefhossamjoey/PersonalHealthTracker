package com.example.PersonalHealthTracker.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IngredientDto {
    private Long id;
    private Long itemId;
    private Long recipeId;
    private String itemName;
    private double grams;
}

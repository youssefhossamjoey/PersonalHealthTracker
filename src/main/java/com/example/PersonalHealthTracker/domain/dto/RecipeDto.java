package com.example.PersonalHealthTracker.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDto {
    private Long id;
    private String recipeName;
    private List<IngredientDto> ingredients;
    private Float totalProtein;
    private Float totalKcal;
}

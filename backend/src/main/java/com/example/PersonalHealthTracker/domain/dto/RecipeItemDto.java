package com.example.personalhealthtracker.domain.dto;


import java.util.UUID;

public record RecipeItemDto(
        UUID foodItemId,
        String foodItemName,
        Double amount
) {
}

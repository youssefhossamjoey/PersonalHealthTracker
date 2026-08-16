package com.example.personalhealthtracker.domain.dto;

import java.util.List;
import java.util.UUID;

public record CreateRecipeRequest(
        String name,
        List<RecipeItemRequest> items
) {
    public record RecipeItemRequest(
            UUID foodItemId,
            Double amount
    ) {
    }
}

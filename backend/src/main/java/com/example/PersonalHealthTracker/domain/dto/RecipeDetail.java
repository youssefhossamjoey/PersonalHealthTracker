package com.example.personalhealthtracker.domain.dto;


import java.util.List;
import java.util.UUID;

public record RecipeDetail(
        UUID id,
        String name,
        List<RecipeItemDto> items
) {}

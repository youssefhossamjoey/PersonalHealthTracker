package com.example.personalhealthtracker.domain.dto;

import java.util.UUID;

public record RecipeSummary (
    UUID id,
    String name,
    Double kcal,
    Double pro
){}

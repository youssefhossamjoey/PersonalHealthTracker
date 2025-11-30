package com.example.personalhealthtracker.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {
    private UUID id;
    private String name;
    private Map<UUID,Double> items;

    public Recipe(String name, Map<UUID, Double> items) {
        this.name = name;
        this.items = items;
    }
}

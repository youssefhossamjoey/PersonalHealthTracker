package com.example.personalhealthtracker.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodItem {
    private Long id;
    private String name;
    private Double kcal;
    private Double pro;
}

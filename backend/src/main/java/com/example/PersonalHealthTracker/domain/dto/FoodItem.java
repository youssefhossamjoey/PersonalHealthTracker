package com.example.personalhealthtracker.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoodItem {
    private UUID id;
    private String name;
    private Double kcal;
    private Double pro;

    public FoodItem(String name, Double pro, Double kcal) {
        this.name = name;
        this.pro = pro;
        this.kcal = kcal;
    }
}

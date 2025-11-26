package com.example.personalhealthtracker.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name= "food_item")
public class FoodItemEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String name;

    private Double kcal;

    private Double pro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false,referencedColumnName = "id")
    private UserAccountEntity owner;

    public FoodItemEntity(String name, Double kcal,Double pro,UserAccountEntity owner) {
        this.name = name;
        this.kcal = kcal;
        this.pro = pro;
        this.owner = owner;
    }
}

package com.example.personalhealthtracker.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;

import java.util.Map;
import java.util.UUID;

@Data
@Entity
@Table(name="recipe")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false,referencedColumnName = "id")
    private UserAccountEntity owner;

    @ElementCollection
    @CollectionTable(name = "recipe_items", joinColumns = @JoinColumn(name = "recipe_id"))
    @MapKeyColumn(name = "food_item_id")
    @Column(name = "amount")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private Map<UUID, Double> items;

    public RecipeEntity(String name, UserAccountEntity owner, Map<UUID, Double> items) {
        this.name = name;
        this.owner = owner;
        this.items = items;
    }
}

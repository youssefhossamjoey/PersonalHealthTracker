package com.example.personalhealthtracker.domain.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name="recipe")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false,referencedColumnName = "id")
    private UserAccountEntity owner;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RecipeItemEntity> items = new ArrayList<>();

    public void addItem(FoodItemEntity foodItem, Double amount) {
        RecipeItemEntity item = RecipeItemEntity.builder()
                .recipe(this)
                .foodItem(foodItem)
                .amount(amount)
                .build();
        this.items.add(item);
    }

    public void removeItem(RecipeItemEntity item) {
        this.items.remove(item);
        item.setRecipe(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeEntity that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


}

package com.example.personalhealthtracker.repositories;

import com.example.personalhealthtracker.domain.entities.RecipeItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RecipeItemRepository extends JpaRepository<RecipeItemEntity, UUID> {
    List<RecipeItemEntity> findByRecipeId(UUID recipeId);

}

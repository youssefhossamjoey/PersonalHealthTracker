package com.example.PersonalHealthTracker.repositories;

import com.example.PersonalHealthTracker.domain.entities.IngredientEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends CrudRepository<IngredientEntity, Long> {
}

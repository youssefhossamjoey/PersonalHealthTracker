package com.example.personalhealthtracker.repositories;

import com.example.personalhealthtracker.domain.entities.FoodItemEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.util.UUID;

public interface FoodItemRepository extends CrudRepository<FoodItemEntity, UUID>, PagingAndSortingRepository<FoodItemEntity,UUID> {
}

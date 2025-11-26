package com.example.personalhealthtracker.repositories;

import com.example.personalhealthtracker.domain.entities.FoodItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FoodItemRepository extends CrudRepository<FoodItemEntity, UUID>, PagingAndSortingRepository<FoodItemEntity,UUID> {
    Optional<FoodItemEntity> findByIdAndOwner_Id(UUID id, UUID ownerId);
    List<FoodItemEntity> findAllByOwner_Id(UUID ownerId);
    Page<FoodItemEntity> findAllByOwner_Id(UUID ownerId, Pageable pageable);
}

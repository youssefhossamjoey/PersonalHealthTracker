package com.example.personalhealthtracker.services;

import com.example.personalhealthtracker.domain.dto.FoodItem;
import com.example.personalhealthtracker.domain.entities.FoodItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FoodItemService {

    FoodItemEntity createFoodItem(FoodItemEntity foodItemEntity);

    List<FoodItemEntity> findAll(UUID ownerId);

    Page<FoodItemEntity> findAll(UUID ownerId,Pageable pageable);

    Optional<FoodItemEntity> findOne(UUID ownerId,UUID id);

    void delete(UUID ownerId,UUID id);

    void deleteBatch(UUID ownerId,List<UUID> ids);

    Page<FoodItemEntity> findAll(UUID id, String q, Pageable name);
}

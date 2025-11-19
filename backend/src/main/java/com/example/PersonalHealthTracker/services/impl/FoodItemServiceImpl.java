package com.example.personalhealthtracker.services.impl;

import com.example.personalhealthtracker.domain.entities.FoodItemEntity;
import com.example.personalhealthtracker.repositories.FoodItemRepository;
import com.example.personalhealthtracker.services.FoodItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class FoodItemServiceImpl implements FoodItemService {

    private FoodItemRepository foodItemRepository;

    public FoodItemServiceImpl(FoodItemRepository foodItemRepository) {
        this.foodItemRepository = foodItemRepository;
    }

    @Override
    public FoodItemEntity createFoodItem(FoodItemEntity foodItemEntity) {
        return foodItemRepository.save(foodItemEntity);
    }

    @Override
    public List<FoodItemEntity> findAll() {
        return StreamSupport.stream(foodItemRepository.findAll().spliterator(),false).collect(Collectors.toList());
    }

    @Override
    public Page<FoodItemEntity> findAll(Pageable pageable) {
        return foodItemRepository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount"))
                ));
    }


    @Override
    public Optional<FoodItemEntity> findOne(UUID id) {
        return foodItemRepository.findById(id);
    }
}

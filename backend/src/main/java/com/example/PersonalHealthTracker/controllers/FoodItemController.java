package com.example.personalhealthtracker.controllers;

import com.example.personalhealthtracker.domain.dto.FoodItem;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.personalhealthtracker.domain.entities.FoodItemEntity;
import com.example.personalhealthtracker.mappers.Mapper;
import com.example.personalhealthtracker.mappers.impl.FoodItemMapper;
import com.example.personalhealthtracker.repositories.FoodItemRepository;
import com.example.personalhealthtracker.services.FoodItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/fooditem")
public class FoodItemController {

    private Mapper<FoodItemEntity, FoodItem> foodItemMapper;
    private FoodItemService foodItemService;

    public FoodItemController(Mapper<FoodItemEntity, FoodItem> foodItemMapper, FoodItemService foodItemService) {
        this.foodItemMapper = foodItemMapper;
        this.foodItemService = foodItemService;
    }
    @PostMapping
    public ResponseEntity<Void> createFoodItem(@RequestBody FoodItem foodItem, UriComponentsBuilder ucb){
        FoodItemEntity entity = foodItemMapper.mapFrom(foodItem);
        FoodItem createdFoodItem = foodItemMapper.mapTo(foodItemService.createFoodItem(entity));
        URI locationOfNewFoodItem = ucb
                .path("fooditem/{id}")
                .buildAndExpand(createdFoodItem.getId())
                .toUri();
        return ResponseEntity.created(locationOfNewFoodItem).build();
    }
    @GetMapping(path = "/{id}")
    public ResponseEntity<FoodItem> getFoodItem(@PathVariable("id") UUID id){
        Optional<FoodItemEntity> requestedFoodItem = foodItemService.findOne(id);
        return requestedFoodItem.map(foodItemEntity ->
                ResponseEntity.ok(foodItemMapper.mapTo(foodItemEntity)))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }
@GetMapping
    private ResponseEntity<Page<FoodItem>> findAll(Pageable pageable){
        Page<FoodItemEntity> page = foodItemService.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC,"name"))
                ));
        return ResponseEntity.ok(page.map(foodItemMapper::mapTo));

}
}

package com.example.personalhealthtracker.controllers;

import com.example.personalhealthtracker.domain.dto.FoodItem;

import com.example.personalhealthtracker.security.UserAccountDetails;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

import com.example.personalhealthtracker.domain.entities.FoodItemEntity;
import com.example.personalhealthtracker.mappers.Mapper;
import com.example.personalhealthtracker.services.FoodItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("api/fooditem")
public class FoodItemController {

    private Mapper<FoodItemEntity, FoodItem> foodItemMapper;
    private FoodItemService foodItemService;

    public FoodItemController(Mapper<FoodItemEntity, FoodItem> foodItemMapper, FoodItemService foodItemService) {
        this.foodItemMapper = foodItemMapper;
        this.foodItemService = foodItemService;
    }

    @PostMapping
    public ResponseEntity<Void> createFoodItem(@RequestBody FoodItem foodItem, UriComponentsBuilder ucb, @AuthenticationPrincipal UserAccountDetails user) {
        FoodItemEntity entity = foodItemMapper.mapFrom(foodItem);
        entity.setOwner(user.getUser());
        FoodItem createdFoodItem = foodItemMapper.mapTo(foodItemService.createFoodItem(entity));
        URI locationOfNewFoodItem = ucb
                .path("fooditem/{id}")
                .buildAndExpand(createdFoodItem.getId())
                .toUri();
        return ResponseEntity.created(locationOfNewFoodItem).build();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<FoodItem> getFoodItem(@PathVariable("id") UUID id, @AuthenticationPrincipal UserAccountDetails user) {
        Optional<FoodItemEntity> requestedFoodItem = foodItemService.findOne(id, user.getId());
        return requestedFoodItem.map(foodItemEntity ->
                        ResponseEntity.ok(foodItemMapper.mapTo(foodItemEntity)))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @GetMapping
    private ResponseEntity<Page<FoodItem>> findAll(Pageable pageable, @AuthenticationPrincipal UserAccountDetails user) {
        Page<FoodItemEntity> page = foodItemService.findAll(
                user.getId(),
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "name"))
                ));
        return ResponseEntity.ok(page.map(foodItemMapper::mapTo));

    }

    @DeleteMapping(path = "/{id}")
    private ResponseEntity<Void> delete(@PathVariable("id") UUID id, @AuthenticationPrincipal UserAccountDetails user) {
        foodItemService.delete(user.getId(), id);
        return ResponseEntity.noContent().build();
    }
}

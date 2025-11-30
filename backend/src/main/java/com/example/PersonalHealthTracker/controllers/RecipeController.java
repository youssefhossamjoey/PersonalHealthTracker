package com.example.personalhealthtracker.controllers;

import com.example.personalhealthtracker.domain.dto.FoodItem;
import com.example.personalhealthtracker.domain.dto.Recipe;
import com.example.personalhealthtracker.domain.dto.RecipeSummary;
import com.example.personalhealthtracker.domain.entities.FoodItemEntity;
import com.example.personalhealthtracker.domain.entities.RecipeEntity;
import com.example.personalhealthtracker.mappers.Mapper;
import com.example.personalhealthtracker.mappers.impl.RecipeMapper;
import com.example.personalhealthtracker.security.UserAccountDetails;
import com.example.personalhealthtracker.services.RecipeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {
    private RecipeMapper recipeMapper;
    private RecipeService recipeService;

    public RecipeController(RecipeMapper recipeMapper, RecipeService recipeService) {
        this.recipeMapper = recipeMapper;
        this.recipeService = recipeService;
    }
    @PostMapping
    public ResponseEntity<Void> createRecipe(@RequestBody Recipe recipe, UriComponentsBuilder ucb, @AuthenticationPrincipal UserAccountDetails user) {
        RecipeEntity entity = recipeMapper.mapFrom(recipe);
        entity.setOwner(user.getUser());
        Recipe createdRecipe = recipeMapper.mapTo(recipeService.createRecipe(entity));
        URI locationOfNewRecipe = ucb
                .path("recipe/{id}")
                .buildAndExpand(createdRecipe.getId())
                .toUri();
        return ResponseEntity.created(locationOfNewRecipe).build();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable("id") UUID id, @AuthenticationPrincipal UserAccountDetails user) {
        Optional<RecipeEntity> requestedRecipe = recipeService.findOne(id, user.getId());
        return requestedRecipe.map(recipeEntity ->
                        ResponseEntity.ok(recipeMapper.mapTo(recipeEntity)))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }

    @GetMapping(path = "summary/{id}")
    public ResponseEntity<RecipeSummary> getRecipeSummary(@PathVariable("id") UUID id, @AuthenticationPrincipal UserAccountDetails user) {
        Optional<RecipeSummary> requestedRecipe = recipeService.findOneSummary(id, user.getId());
        return requestedRecipe.map(recipeEntity ->
                        ResponseEntity.ok(requestedRecipe.get()))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));

    }
    @GetMapping
    private ResponseEntity<Page<RecipeSummary>> findAll(Pageable pageable,
                                                   @AuthenticationPrincipal UserAccountDetails user,
                                                   @RequestParam(required = false) String search) {
        if (search != null && !search.isEmpty()) {
            Page<RecipeSummary> page = recipeService.findAllSummary(
                    user.getId(),
                    search,
                    PageRequest.of(
                            pageable.getPageNumber(),
                            pageable.getPageSize(),
                            pageable.getSortOr(Sort.by(Sort.Direction.ASC, "name"))
                    ));
            return ResponseEntity.ok(page);
        } else {
            Page<RecipeSummary> page = recipeService.findAllSummary(
                    user.getId(),
                    PageRequest.of(
                            pageable.getPageNumber(),
                            pageable.getPageSize(),
                            pageable.getSortOr(Sort.by(Sort.Direction.ASC, "name"))
                    ));
            return ResponseEntity.ok(page);
        }


    }

    @DeleteMapping(path = "/{id}")
    private ResponseEntity<Void> delete(@PathVariable("id") UUID id, @AuthenticationPrincipal UserAccountDetails user) {
        recipeService.delete(user.getId(), id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    private ResponseEntity<Void> Batchdelete(@RequestBody List<UUID> ids, @AuthenticationPrincipal UserAccountDetails user) {
        recipeService.deleteBatch(user.getId(), ids);
        return ResponseEntity.noContent().build();
    }

}

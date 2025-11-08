package com.example.PersonalHealthTracker.repositories;

import com.example.PersonalHealthTracker.domain.entities.ItemEntity;
import com.example.PersonalHealthTracker.domain.entities.RecipeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends CrudRepository<RecipeEntity, Long>, PagingAndSortingRepository<RecipeEntity, Long> {
    List<RecipeEntity> findByRecipeNameContainingIgnoreCase(String name);
    Page<RecipeEntity> findByRecipeNameContainingIgnoreCase(String name, Pageable pageable);
}

package com.example.PersonalHealthTracker.services.impl;

import com.example.PersonalHealthTracker.domain.entities.IngredientEntity;
import com.example.PersonalHealthTracker.repositories.IngredientRepository;
import com.example.PersonalHealthTracker.services.IngredientService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class IngredientServiceImpl implements IngredientService {
    private IngredientRepository ingredientRepository;

    public IngredientServiceImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public IngredientEntity createIngredient(IngredientEntity ingredient) {
        return ingredientRepository.save(ingredient);
    }

    @Override
    public List<IngredientEntity> batchCreateIngredients(List<IngredientEntity> ingredientEntityList) {
        return StreamSupport
                .stream(ingredientRepository.saveAll(ingredientEntityList).spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<IngredientEntity> findAll() {
        return StreamSupport
                .stream(ingredientRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<IngredientEntity> findOne(Long id) {
        return ingredientRepository.findById(id);
    }
}

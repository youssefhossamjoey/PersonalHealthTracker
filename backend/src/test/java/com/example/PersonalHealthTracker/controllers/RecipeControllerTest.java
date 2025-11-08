package com.example.PersonalHealthTracker.controllers;

import com.example.PersonalHealthTracker.domain.dto.RecipeDto;
import com.example.PersonalHealthTracker.domain.entities.IngredientEntity;
import com.example.PersonalHealthTracker.domain.entities.RecipeEntity;
import com.example.PersonalHealthTracker.mappers.Impl.RecipeMapper;
import com.example.PersonalHealthTracker.services.IngredientService;
import com.example.PersonalHealthTracker.services.RecipeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RecipeController.class)
public class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeService recipeService;

    @MockBean
    private RecipeMapper recipeMapper;

    @MockBean
    private IngredientService ingredientService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void postRecipe_withoutIngredients_createsRecipe() throws Exception {
        RecipeDto requestDto = RecipeDto.builder().recipeName("Salad").build();

        RecipeEntity mappedEntity = RecipeEntity.builder().recipeName("Salad").recipeIngredients(new ArrayList<>()).build();
        RecipeEntity savedEntity = RecipeEntity.builder().id(1L).recipeName("Salad").recipeIngredients(new ArrayList<>()).build();
        RecipeDto responseDto = RecipeDto.builder().id(1L).recipeName("Salad").build();

        when(recipeMapper.mapFrom(any(RecipeDto.class))).thenReturn(mappedEntity);
        when(recipeService.creteRecipe(any(RecipeEntity.class))).thenReturn(savedEntity);
        when(recipeMapper.mapTo(any(RecipeEntity.class))).thenReturn(responseDto);

        mockMvc.perform(post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.recipeName", is("Salad")));
    }

    @Test
    void postRecipe_withIngredients_callsBatchCreate() throws Exception {
        RecipeDto requestDto = RecipeDto.builder().recipeName("Sandwich").build();

        List<IngredientEntity> ingredients = new ArrayList<>();
        IngredientEntity ing = IngredientEntity.builder().grams(100).build();
        ingredients.add(ing);

        RecipeEntity mappedEntity = RecipeEntity.builder().recipeName("Sandwich").recipeIngredients(ingredients).build();
        RecipeEntity savedEntity = RecipeEntity.builder().id(2L).recipeName("Sandwich").recipeIngredients(ingredients).build();
        RecipeDto responseDto = RecipeDto.builder().id(2L).recipeName("Sandwich").build();

    when(recipeMapper.mapFrom(any(RecipeDto.class))).thenReturn(mappedEntity);
    when(recipeService.creteRecipe(any(RecipeEntity.class))).thenReturn(savedEntity);
    when(recipeMapper.mapTo(any(RecipeEntity.class))).thenReturn(responseDto);
    when(ingredientService.batchCreateIngredients(any(List.class))).thenReturn(ingredients);

        mockMvc.perform(post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.recipeName", is("Sandwich")));

    verify(ingredientService).batchCreateIngredients(any(List.class));
    }
}

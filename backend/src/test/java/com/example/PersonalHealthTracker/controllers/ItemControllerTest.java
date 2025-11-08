package com.example.PersonalHealthTracker.controllers;

import com.example.PersonalHealthTracker.domain.dto.ItemDto;
import com.example.PersonalHealthTracker.domain.entities.ItemEntity;
import com.example.PersonalHealthTracker.mappers.Impl.ItemMapper;
import com.example.PersonalHealthTracker.services.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @MockBean
    private ItemMapper itemMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void postItem_createsAndReturnsItem() throws Exception {
        ItemDto requestDto = ItemDto.builder().itemName("Apple").kcal(52f).protein(0.3f).build();

        ItemEntity mappedEntity = ItemEntity.builder().itemName("Apple").kcal(52f).protein(0.3f).build();
        ItemEntity savedEntity = ItemEntity.builder().id(1L).itemName("Apple").kcal(52f).protein(0.3f).build();
        ItemDto responseDto = ItemDto.builder().id(1L).itemName("Apple").kcal(52f).protein(0.3f).build();

        when(itemMapper.mapFrom(any(ItemDto.class))).thenReturn(mappedEntity);
        when(itemService.creteItem(any(ItemEntity.class))).thenReturn(savedEntity);
        when(itemMapper.mapTo(any(ItemEntity.class))).thenReturn(responseDto);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.itemName", is("Apple")));
    }
}

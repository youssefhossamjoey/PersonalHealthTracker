package com.example.personalhealthtracker.controllers;

import com.example.personalhealthtracker.domain.dto.FoodItem;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FoodItemControllerTest {

@Autowired
    private TestRestTemplate restTemplate;


@Test
    public void FoodItemsCanBeCreated(){
    FoodItem foodItem= new FoodItem("chicken",1.00,2.00);
    ResponseEntity<Void> response = restTemplate.postForEntity("/fooditem",foodItem,Void.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    URI locationOfNewFoodItem = response.getHeaders().getLocation();
    ResponseEntity<String> getResponse = restTemplate.getForEntity(locationOfNewFoodItem, String.class);
    assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

    DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
    String id = documentContext.read("$.id");
    String name = documentContext.read("$.name");
    Number kcal = documentContext.read("$.kcal");
    Number pro = documentContext.read("$.pro");

    assertThat(id).isNotNull();
    assertThat(name).isEqualTo("chicken");
    assertThat(pro).isEqualTo(1.00);
    assertThat(kcal).isEqualTo(2.00);
}

@Test
    public void shouldReturnALLFoodItemWHenDataIsRequested(){
    ResponseEntity<String> response= restTemplate.getForEntity("/fooditem/00000000-0000-0000-0000-000000000001", String.class);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    DocumentContext documentContext = JsonPath.parse(response.getBody());
    String id = documentContext.read("$.id");
    assertThat(id).isEqualTo("00000000-0000-0000-0000-000000000001");

    String name = documentContext.read("$.name");
    assertThat(name).isEqualTo("chicken");

    Number kcal = documentContext.read("$.kcal");
    assertThat(kcal).isEqualTo(123.45);

    Number pro = documentContext.read("$.pro");
    assertThat(pro).isEqualTo(1.00);
}
    @Test
    public void shouldReturnAPageOfFoodItems(){
        ResponseEntity<String> response= restTemplate.getForEntity("/fooditem?page=0&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray page = documentContext.read("$.content");
        assertThat(page.size()).isEqualTo(1);
    }
}

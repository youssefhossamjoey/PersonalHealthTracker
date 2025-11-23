package com.example.personalhealthtracker.controllers;

import com.example.personalhealthtracker.TestAuthotizationUtil;
import com.example.personalhealthtracker.domain.dto.FoodItem;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FoodItemControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;
    private HttpHeaders header ;

    @BeforeEach
    public void setUp() {
        header= TestAuthotizationUtil.createAuthorization(restTemplate);
    }

    @Test
    public void FoodItemsCanBeCreated() {
        FoodItem foodItem = new FoodItem("chicken", 1.00, 2.00);
        HttpEntity<FoodItem> request = new HttpEntity<>(foodItem, header);
        ResponseEntity<Void> response = restTemplate.exchange("/fooditem", HttpMethod.POST, request, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI locationOfNewFoodItem = response.getHeaders().getLocation();
        HttpEntity<Void> request2 = new HttpEntity<>(null, header);
        ResponseEntity<String> getResponse = restTemplate.exchange(locationOfNewFoodItem, HttpMethod.GET,request2, String.class);
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
    public void shouldReturnALLFoodItemWHenDataIsRequested() {
        HttpEntity<Void> request2 = new HttpEntity<>(null, header);
        ResponseEntity<String> response = restTemplate.exchange("/fooditem/00000000-0000-0000-0000-000000000001",
                HttpMethod.GET,request2,String.class);
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
    public void shouldReturnAPageOfFoodItems() {
        HttpEntity<Void> request2 = new HttpEntity<>(null, header);
        ResponseEntity<String> response = restTemplate.exchange("/fooditem?page=0&size=1",
                HttpMethod.GET,request2, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray page = documentContext.read("$.content");
        assertThat(page.size()).isEqualTo(1);
    }
}

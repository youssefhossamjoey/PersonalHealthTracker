package com.example.personalhealthtracker.JsonTests;

import com.example.personalhealthtracker.domain.dto.FoodItem;
import java.util.UUID;
import com.example.personalhealthtracker.domain.entities.FoodItemEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class FoodItemJsonTest {
    @Autowired
    private JacksonTester<FoodItem> json;

    @Test
    void foodItemSerializationTest() throws IOException {
        FoodItem foodItem = new FoodItem(UUID.fromString("00000000-0000-0000-0000-000000000001"),"chicken",1.00,1.00);
        assertThat(json.write(foodItem)).isStrictlyEqualToJson("/expectedFoodItem.json");

        assertThat(json.write(foodItem)).hasJsonPathValue("@.id");
        assertThat(json.write(foodItem)).extractingJsonPathValue("@.id")
                .isEqualTo("00000000-0000-0000-0000-000000000001");

        assertThat(json.write(foodItem)).hasJsonPathStringValue("@.name");
        assertThat(json.write(foodItem)).extractingJsonPathStringValue("@.name")
                .isEqualTo("chicken");

        assertThat(json.write(foodItem)).hasJsonPathNumberValue("@.kcal");
        assertThat(json.write(foodItem)).extractingJsonPathNumberValue("@.kcal")
                .isEqualTo(1.00);

        assertThat(json.write(foodItem)).hasJsonPathNumberValue("@.pro");
        assertThat(json.write(foodItem)).extractingJsonPathNumberValue("@.pro")
                .isEqualTo(1.00);

    }
    @Test
    void foodItemDeserializationTest() throws IOException {
        String expected = """
                        {
                          "id": "00000000-0000-0000-0000-000000000001",
                          "name": "chicken",
                          "kcal": 1.00,
                          "pro": 1.00
                        }
           """;
        assertThat(json.parse(expected))
                .isEqualTo(new FoodItem(UUID.fromString("00000000-0000-0000-0000-000000000001"),"chicken",1.00,1.00));
        assertThat(json.parseObject(expected).getId()).isEqualTo(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        assertThat(json.parseObject(expected).getName()).isEqualTo("chicken");
        assertThat(json.parseObject(expected).getKcal()).isEqualTo(1.00);
        assertThat(json.parseObject(expected).getPro()).isEqualTo(1.00);
    }

}

package com.example.personalhealthtracker.services;

import com.example.personalhealthtracker.domain.entities.FoodItemEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FoodItemServiceTest {

    @Autowired
    private FoodItemService foodItemService;

    @Test
    public void FoodItemCanBeCreated() {
        FoodItemEntity item = new FoodItemEntity("chicken", 1.00, 1.00,null);
        assertNull(item.getId());
        FoodItemEntity saved = foodItemService.createFoodItem(item);
        assertNotNull(saved.getId());
        assertDoesNotThrow(() -> UUID.fromString(saved.getId().toString()));
    }

    @Test
    public void FoodItemCanBeCreatedAndFetched() {
        FoodItemEntity item = new FoodItemEntity("chicken", 1.00, 1.00,null);
        FoodItemEntity saved = foodItemService.createFoodItem(item);
        UUID id = saved.getId();
        Optional<FoodItemEntity> fetched = foodItemService.findOne(id);
        assertThat(fetched.isPresent());
        assertThat(fetched.get().getName()).isEqualTo("chicken");
    }

    @Test
    public void MultipleFoodItemCanBeCreatedAndFetched() {
//        FoodItemEntity item = new FoodItemEntity("chicken", 1.00, 1.00);
//        foodItemService.createFoodItem(item);
//        FoodItemEntity item2 = new FoodItemEntity("steak", 2.00, 2.00);
//        foodItemService.createFoodItem(item2);
//        FoodItemEntity item3 = new FoodItemEntity("cod", 3.00, 3.00);
//        foodItemService.createFoodItem(item3);
        List<FoodItemEntity> fetched = foodItemService.findAll();
        assertThat(fetched.size()).isEqualTo(3);
        assertThat(fetched.get(0).getName()).isEqualTo("chicken");
        assertThat(fetched.get(1).getName()).isEqualTo("steak");
        assertThat(fetched.get(2).getName()).isEqualTo("cod");
    }

    @Test
    @DirtiesContext
    public void MultiplePagedFoodItemCanBeCreatedAndFetched() {
//        foodItemService.createFoodItem(new FoodItemEntity("chicken", 1.00, 1.00));
//        foodItemService.createFoodItem(new FoodItemEntity("steak", 2.00, 2.00));
//        foodItemService.createFoodItem(new FoodItemEntity("cod", 3.00, 3.00));
        Page<FoodItemEntity> fetched = foodItemService.findAll(PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "name")));
        assertThat(fetched.getContent().size()).isEqualTo(1);
        assertThat(fetched.getTotalPages()).isEqualTo(3);
        assertThat(fetched.getContent().get(0).getName()).isEqualTo("chicken");
        assertThat(fetched.getNumber()).isEqualTo(0); // page index
        assertThat(fetched.getSize()).isEqualTo(1);   // page size requested
        assertThat(fetched.hasNext()).isTrue();       // page 0 has next page
    }
}

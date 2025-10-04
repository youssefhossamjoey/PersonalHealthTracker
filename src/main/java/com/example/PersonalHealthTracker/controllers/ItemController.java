package com.example.PersonalHealthTracker.controllers;

import com.example.PersonalHealthTracker.domain.dto.ItemDto;
import com.example.PersonalHealthTracker.domain.entities.ItemEntity;
import com.example.PersonalHealthTracker.mappers.Impl.ItemMapper;
import com.example.PersonalHealthTracker.services.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
@RestController
public class ItemController {
    private ItemService itemService;
    private ItemMapper itemMapper;

    public ItemController(ItemService itemService, ItemMapper itemMapper) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
    }
    @PostMapping(path = "/items")
    public ResponseEntity<ItemDto> createItem(@RequestBody ItemDto item){
        ItemEntity itemEntity = itemMapper.mapFrom(item);
        ItemEntity savedItemEntity= itemService.creteItem(itemEntity);
        return new ResponseEntity<>(itemMapper.mapTo(savedItemEntity), HttpStatus.CREATED);
    }

//    @GetMapping(path = "/items")
//    public List<ItemDto> listItems(){
//        List<ItemEntity> items =itemService.findAll();
//        return items.stream()
//                .map(itemMapper::mapTo)
//                .collect(Collectors.toList());
//    }

    @GetMapping(path = "/items")
    public Page<ItemDto> listItems(Pageable page){
        Page<ItemEntity> authors =itemService.findAll(page);
        return authors.map(itemMapper::mapTo);
    }
}

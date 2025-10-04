package com.example.PersonalHealthTracker.services;

import com.example.PersonalHealthTracker.domain.entities.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    ItemEntity creteItem(ItemEntity itemEntity);
    List<ItemEntity> findAll();
    Page<ItemEntity> findAll(Pageable page);

    List<ItemEntity> findAllByName(String name);
    Page<ItemEntity> findAllByName(String name,Pageable page);

    Optional<ItemEntity> findOne(Long id);

}

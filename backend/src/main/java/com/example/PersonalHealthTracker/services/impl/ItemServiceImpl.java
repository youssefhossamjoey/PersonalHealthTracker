package com.example.PersonalHealthTracker.services.impl;

import com.example.PersonalHealthTracker.domain.entities.ItemEntity;
import com.example.PersonalHealthTracker.repositories.ItemRepository;
import com.example.PersonalHealthTracker.services.ItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ItemServiceImpl implements ItemService {
    private ItemRepository itemRepository;

    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemEntity creteItem(ItemEntity item) {
        return itemRepository.save(item);
    }

    @Override
    public List<ItemEntity> findAll() {
        return StreamSupport
                .stream(itemRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ItemEntity> findAll(Pageable page) {
        return itemRepository.findAll(page) ;
    }

    @Override
    public List<ItemEntity> findAllByName(String name) {
        return StreamSupport
                .stream(itemRepository.findByItemNameContainingIgnoreCase(name).spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ItemEntity> findAllByName(String name, Pageable page) {
        return itemRepository.findByItemNameContainingIgnoreCase(name,page) ;
    }

    @Override
    public Optional<ItemEntity> findOne(Long id) {
        return itemRepository.findById(id);
    }
}

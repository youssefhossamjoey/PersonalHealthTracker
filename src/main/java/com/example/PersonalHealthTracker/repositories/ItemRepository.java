package com.example.PersonalHealthTracker.repositories;

import com.example.PersonalHealthTracker.domain.entities.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends CrudRepository<ItemEntity, Long>, PagingAndSortingRepository<ItemEntity, Long> {
    List<ItemEntity> findByItemNameContainingIgnoreCase(String name);
    Page<ItemEntity> findByItemNameContainingIgnoreCase(String name, Pageable pageable);
}

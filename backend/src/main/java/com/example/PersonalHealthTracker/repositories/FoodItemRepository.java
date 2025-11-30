package com.example.personalhealthtracker.repositories;

import com.example.personalhealthtracker.domain.entities.FoodItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FoodItemRepository extends CrudRepository<FoodItemEntity, UUID>, PagingAndSortingRepository<FoodItemEntity,UUID> {
    Optional<FoodItemEntity> findByIdAndOwner_Id(UUID id, UUID ownerId);
    List<FoodItemEntity> findAllByOwner_Id(UUID ownerId);
    Page<FoodItemEntity> findAllByOwner_Id(UUID ownerId, Pageable pageable);
    @Query("""
    SELECT f FROM FoodItemEntity f
    WHERE f.owner.id = :ownerId
      AND LOWER(f.name) LIKE CONCAT('%',LOWER(:query),'%')
""")
    Page<FoodItemEntity> search(UUID ownerId, String query, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Transactional
    void deleteByIdAndOwner_Id(UUID id, UUID ownerId);

    @Modifying
    @Transactional
    @Query(value = """
    DELETE FROM food_item
    WHERE owner_id = :ownerId
      AND id IN (:ids)
""", nativeQuery = true)
    void deleteAllByIdsAndOwner(UUID ownerId, List<UUID> ids);
}

package com.example.personalhealthtracker.repositories;

import com.example.personalhealthtracker.domain.dto.RecipeSummary;
import com.example.personalhealthtracker.domain.entities.RecipeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecipeRepository extends CrudRepository<RecipeEntity, UUID>, PagingAndSortingRepository<RecipeEntity, UUID> {
    Optional<RecipeEntity> findByIdAndOwner_Id(UUID id, UUID ownerId);

    List<RecipeEntity> findAllByOwner_Id(UUID ownerId);

    Page<RecipeEntity> findAllByOwner_Id(UUID ownerId, Pageable pageable);

    @Query("""
                SELECT r FROM RecipeEntity r
                WHERE r.owner.id = :ownerId
                  AND LOWER(r.name) LIKE CONCAT('%',LOWER(:query),'%')
            """)
    Page<RecipeEntity> search(UUID ownerId, String query, Pageable pageable);

    @Modifying(clearAutomatically = true)
    @Transactional
    void deleteByIdAndOwner_Id(UUID id, UUID ownerId);


    @Modifying
    @Transactional
    void deleteAllByIdInAndOwnerId(List<UUID> ids, UUID ownerId);

    @Query("""
            SELECT new com.example.personalhealthtracker.domain.dto.RecipeSummary(
                r.id,
                r.name,
                SUM(VALUE(ri) * fi.kcal) as kcal,
                SUM(VALUE(ri) * fi.pro) as pro
            )
            FROM RecipeEntity r
            JOIN r.items ri
            JOIN FoodItemEntity fi ON KEY(ri) = fi.id
            WHERE r.id = :id
            AND r.owner.id = :ownerId
            GROUP BY r.id, r.name
            """)
    Optional getRecipeSummary(UUID ownerId, UUID id);

    @Query("""
            SELECT new com.example.personalhealthtracker.domain.dto.RecipeSummary(
                r.id,
                r.name,
                SUM(VALUE(ri) * fi.kcal) as kcal,
                SUM(VALUE(ri) * fi.pro) as pro
            )
            FROM RecipeEntity r
            JOIN r.items ri
            JOIN FoodItemEntity fi ON KEY(ri) = fi.id
            WHERE r.owner.id = :ownerId
            AND LOWER(r.name) LIKE CONCAT('%',LOWER(:query),'%')
            GROUP BY r.id, r.name
            """)
    Page<RecipeSummary> searchRecipeSummary(UUID ownerId, String query, Pageable pageable);

    @Query("""
            SELECT new com.example.personalhealthtracker.domain.dto.RecipeSummary(
                r.id,
                r.name,
                SUM(VALUE(ri) * fi.kcal) as kcal,
                SUM(VALUE(ri) * fi.pro) as pro
            )
            FROM RecipeEntity r
            JOIN r.items ri
            JOIN FoodItemEntity fi ON KEY(ri) = fi.id
            WHERE r.owner.id = :ownerId
            GROUP BY r.id, r.name
            """)
    Page<RecipeSummary> searchRecipeSummary(UUID ownerId, Pageable pageable);
}

package com.example.personalhealthtracker.repositories;

import com.example.personalhealthtracker.domain.dto.RecipeSummary;
import com.example.personalhealthtracker.domain.entities.RecipeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecipeRepository extends JpaRepository<RecipeEntity, UUID> {
    Optional<RecipeEntity> findByIdAndOwner_Id(UUID id, UUID ownerId);

    boolean existsByRecipesId(UUID recipeId);

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
                SUM(ri.amount * fi.kcal),
                SUM(ri.amount * fi.pro)
            )
            FROM RecipeEntity r
            JOIN r.items ri
            JOIN ri.foodItem fi
            WHERE r.id = :id
            AND r.owner.id = :ownerId
            GROUP BY r.id, r.name
            """)
    Optional<RecipeSummary> getRecipeSummary(@Param("ownerId") UUID ownerId, @Param("id") UUID id);

    @Query("""
            SELECT new com.example.personalhealthtracker.domain.dto.RecipeSummary(
                r.id,
                r.name,
                SUM(ri.amount * fi.kcal),
                SUM(ri.amount * fi.pro)
            )
            FROM RecipeEntity r
            JOIN r.items ri
            JOIN ri.foodItem fi
            WHERE r.owner.id = :ownerId
            AND LOWER(r.name) LIKE CONCAT('%',LOWER(:query),'%')
            GROUP BY r.id, r.name
            """)
    Page<RecipeSummary> searchRecipeSummary(@Param("ownerId") UUID ownerId, @Param("query") String query, Pageable pageable);

    @Query("""
            SELECT new com.example.personalhealthtracker.domain.dto.RecipeSummary(
                r.id,
                r.name,
                SUM(ri.amount * fi.kcal),
                SUM(ri.amount * fi.pro)
            )
            FROM RecipeEntity r
            JOIN r.items ri
            JOIN ri.foodItem fi
            WHERE r.owner.id = :ownerId
            GROUP BY r.id, r.name
            """)
    Page<RecipeSummary> searchRecipeSummary(@Param("ownerId") UUID ownerId, Pageable pageable);
}

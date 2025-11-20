package com.example.personalhealthtracker.repositories;

import com.example.personalhealthtracker.domain.entities.UserAccountEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface UserAccountRepository extends CrudRepository<UserAccountEntity, UUID>, PagingAndSortingRepository<UserAccountEntity, UUID> {
}

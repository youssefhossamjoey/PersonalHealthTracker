package com.example.personalhealthtracker.mappers.impl;

import com.example.personalhealthtracker.domain.dto.UserAccount;
import com.example.personalhealthtracker.domain.entities.UserAccountEntity;
import com.example.personalhealthtracker.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserAccountMapper implements Mapper<UserAccountEntity, UserAccount> {
    private final ModelMapper modelMapper;

    public UserAccountMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public UserAccount mapTo(UserAccountEntity userAccountEntity) {
        return new UserAccount(
                userAccountEntity.getUsername(),
                null,
                userAccountEntity.getRole()
                );
    }

    @Override
    public UserAccountEntity mapFrom(UserAccount userAccount) {
        return modelMapper.map(userAccount, UserAccountEntity.class);
    }
}

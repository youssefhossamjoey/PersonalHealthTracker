package com.example.personalhealthtracker.security;

import com.example.personalhealthtracker.domain.entities.UserAccountEntity;
import com.example.personalhealthtracker.repositories.UserAccountRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserAccountDetailsService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    public UserAccountDetailsService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public UserAccountDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccountEntity user = userAccountRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User not found with username: "+ username));
        return new UserAccountDetails(user);
    }
}

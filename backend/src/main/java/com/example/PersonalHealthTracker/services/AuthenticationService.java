package com.example.personalhealthtracker.services;

import com.example.personalhealthtracker.security.UserAccountDetails;

public interface AuthenticationService {

    UserAccountDetails authenticate(String username, String Password);
    String generateToken(UserAccountDetails userAccountDetails);
    UserAccountDetails validateToken(String token);
}

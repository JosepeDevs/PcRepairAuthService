package com.josepdevs.Domain.repository;

import java.util.Optional;

import com.josepdevs.Domain.entities.AuthenticationData;

public interface AuthRepository {
	
	
    public AuthenticationData registerUserAuthData(AuthenticationData authData);
    
    public Optional<AuthenticationData> findByUsername(String username);
	
}

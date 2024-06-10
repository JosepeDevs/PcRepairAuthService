package com.josepdevs.Domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.josepdevs.Domain.entities.AuthenticationData;

public interface AuthRepository {
	
	
    public AuthenticationData registerUserAuthData(AuthenticationData authData, String jwtToken);
    
	public boolean login(AuthenticationData authData, String jwtToken);
    
    public Optional<AuthenticationData> findByUsername(String username);
    
	public boolean invalidateToken(AuthenticationData authData);
	
	public boolean isTokenInvalidated(String username);
    
	public boolean patchPassword(AuthenticationData authData, String digestedPsswrd);
	
	public boolean patchRole(AuthenticationData authData, String role);
	
	public List<AuthenticationData> getAll();

	
}

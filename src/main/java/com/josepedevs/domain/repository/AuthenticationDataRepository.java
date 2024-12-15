package com.josepedevs.domain.repository;

import com.josepedevs.domain.entity.AuthenticationData;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthenticationDataRepository {
	
	AuthenticationData registerUserAuthData(AuthenticationData authData, String jwtToken);
    
	boolean updateToken(AuthenticationData authData, String jwtToken);
    
    Optional<AuthenticationData> findByUsername(String username);
    
    Optional<AuthenticationData> findById(UUID id);

	Optional<AuthenticationData> findByEmail(String email);

	boolean invalidateToken(AuthenticationData authData);
	
	boolean isTokenInvalidated(String username);
    
	boolean patchPassword(AuthenticationData authData, String digestedPsswrd);
	
	boolean patchRole(AuthenticationData authData, String role);
	
	List<AuthenticationData> getAll();
	
	boolean deleteHard(UUID userId);
	
}

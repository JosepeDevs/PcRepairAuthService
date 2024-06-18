package com.josepedevs.Domain.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.josepedevs.Application.PatchPassword;
import com.josepedevs.Domain.entities.AuthenticationData;
import com.josepedevs.Domain.exceptions.UserNotFoundException;
import com.josepedevs.Domain.repository.AuthRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GetUserFromTokenUsernameService {
	
	private final AuthRepository repository;
	private final Logger logger = LoggerFactory.getLogger(PatchPassword.class);

	public AuthenticationData getUserFromTokenUsername(String username) {
		
		Optional<AuthenticationData> userDataAuth = repository.findByUsername(username); 
		AuthenticationData existingUser = userDataAuth.orElseThrow( () -> {
			logger.error("User was not found with username: "+username);
			throw new UserNotFoundException("The user does not exists.", "username");
			} 
		);	
		logger.trace("User found, returning AuthenticationData entity");
		return existingUser;
	}
	
}

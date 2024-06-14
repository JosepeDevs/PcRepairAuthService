package com.josepdevs.Domain.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.josepdevs.Application.PatchPassword;
import com.josepdevs.Domain.Exceptions.UserNotFoundException;
import com.josepdevs.Domain.entities.AuthenticationData;
import com.josepdevs.Domain.repository.AuthRepository;

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

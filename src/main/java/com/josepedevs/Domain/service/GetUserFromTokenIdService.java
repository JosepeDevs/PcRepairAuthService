package com.josepedevs.Domain.service;

import java.util.Optional;
import java.util.UUID;

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
public class GetUserFromTokenIdService {
	
	private final AuthRepository repository;
	private final Logger logger = LoggerFactory.getLogger(PatchPassword.class);

	public AuthenticationData getUserFromTokenId(UUID id) {
		
		Optional<AuthenticationData> userDataAuth = repository.findById(id); 
		AuthenticationData existingUser = userDataAuth.orElseThrow( () -> {
			logger.error("User was not found with id: "+id);
			throw new UserNotFoundException("The user does not exists.", "userId");
			} 
		);	
		logger.trace("User found, returning AuthenticationData entity");
		return existingUser;
	}
	
}

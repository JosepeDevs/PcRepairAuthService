package com.josepedevs.application.service;

import com.josepedevs.application.usecase.authenticationdata.PatchAuthenticationDataPasswordUseCaseImpl;
import com.josepedevs.domain.entity.AuthenticationData;
import com.josepedevs.domain.exceptions.UserNotFoundException;
import com.josepedevs.domain.repository.AuthenticationDataRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetUserFromTokenUsernameService {
	
	private final AuthenticationDataRepository repository;
	private final Logger logger = LoggerFactory.getLogger(PatchAuthenticationDataPasswordUseCaseImpl.class);

	public AuthenticationData getUserFromTokenUsername(String username) {
		
		final var userDataAuth = repository.findByUsername(username);
		final var existingUser = userDataAuth.orElseThrow( () -> {
			logger.error("User was not found with username: "+username);
			throw new UserNotFoundException("The user does not exists.", "username");
			} 
		);	
		logger.trace("User found, returning AuthenticationData entity");
		return existingUser;
	}
	
}

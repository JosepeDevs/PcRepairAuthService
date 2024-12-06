package com.josepedevs.application.service;

import com.josepedevs.domain.entity.AuthenticationData;
import com.josepedevs.domain.exceptions.UserNotFoundException;
import com.josepedevs.domain.repository.AuthenticationDataRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class GetUserFromTokenUsernameService {

	private static final String USER_WAS_NOT_FOUND_WITH_USERNAME = "User was not found with username: {}";
	private final AuthenticationDataRepository repository;

	public AuthenticationData getUserFromTokenUsername(String username) {
		
		final var userDataAuth = repository.findByUsername(username);
		final var existingUser = userDataAuth.orElseThrow( () -> {
            log.error(USER_WAS_NOT_FOUND_WITH_USERNAME, username);
			throw new UserNotFoundException("The user does not exists.", "username");
			} 
		);	
		log.trace("User found, returning AuthenticationData entity");
		return existingUser;
	}
	
}

package com.josepedevs.application.service;

import com.josepedevs.domain.entity.AuthenticationData;
import com.josepedevs.domain.exceptions.UserNotFoundException;
import com.josepedevs.domain.repository.AuthenticationDataRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class AuthDataFinder {

	private final AuthenticationDataRepository repository;

	private static final String USER_WAS_NOT_FOUND_WITH_USERNAME = "User was not found with username: {}";

	public AuthenticationData findById(UUID id) {
		final var userDataAuth = repository.findById(id);
		final var existingUser = userDataAuth.orElseThrow( () -> {
			log.error("User was not found with id: {}",id);
            return new UserNotFoundException("The user does not exists.");
			} 
		);	
		log.trace("User found, returning AuthenticationData entity");
		return existingUser;
	}

	public AuthenticationData findByUsername(String username) {
		final var userDataAuth = repository.findByUsername(username);
		final var existingUser = userDataAuth.orElseThrow( () -> {
					log.error(USER_WAS_NOT_FOUND_WITH_USERNAME, username);
					return new UserNotFoundException("The user does not exists.");
				}
		);
		log.trace("User found, returning AuthenticationData entity");
		return existingUser;
	}
	
}

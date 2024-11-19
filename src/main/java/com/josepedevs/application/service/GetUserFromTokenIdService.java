package com.josepedevs.application.service;

import com.josepedevs.application.usecase.user.PatchUserPasswordUseCaseImpl;
import com.josepedevs.domain.entity.AuthenticationData;
import com.josepedevs.domain.exceptions.UserNotFoundException;
import com.josepedevs.domain.repository.AuthenticationDataRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GetUserFromTokenIdService {
	
	private final AuthenticationDataRepository repository;
	private final Logger logger = LoggerFactory.getLogger(PatchUserPasswordUseCaseImpl.class);

	public AuthenticationData getUserFromTokenId(UUID id) {
		
		Optional<AuthenticationData> userDataAuth = repository.findById(id);
		final var existingUser = userDataAuth.orElseThrow( () -> {
			logger.error("User was not found with id: "+id);
			throw new UserNotFoundException("The user does not exists.", "userId");
			} 
		);	
		logger.trace("User found, returning AuthenticationData entity");
		return existingUser;
	}
	
}

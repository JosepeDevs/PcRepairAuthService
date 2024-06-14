package com.josepdevs.Application;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.josepdevs.Domain.Exceptions.UserNotFoundException;
import com.josepdevs.Domain.entities.AuthenticationData;
import com.josepdevs.Domain.repository.AuthRepository;
import com.josepdevs.Domain.service.GetUserFromTokenUsernameService;
import com.josepdevs.Domain.service.JwtTokenDataExtractorService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InvalidateToken {

	private final AuthRepository repository;
	private final JwtTokenDataExtractorService jwtReaderService;
	private final Logger logger = LoggerFactory.getLogger(InvalidateToken.class);
	private final GetUserFromTokenUsernameService getUserFromTokenUsernameService;

	public boolean invalidateToken(String jwtToken) {
		//here is being throw the error
		String username = jwtReaderService.extractUsername(jwtToken);
		AuthenticationData user = getUserFromTokenUsernameService.getUserFromTokenUsername(username);

		user.setCurrentToken("invalidated");
		boolean success = repository.invalidateToken(user);
		if( success ) {
			logger.info("The token was correctly invalidaded.");
			return true;
		} else {
			logger.info("The token could not be invalidated.");
			return false;
		}
	}
	
	
	
}

package com.josepedevs.application.usecase.user;

import com.josepedevs.application.service.GetUserFromTokenUsernameService;
import com.josepedevs.application.service.JwtTokenDataExtractorService;
import com.josepedevs.domain.repository.AuthenticationDataRepository;
import com.josepedevs.domain.usecase.InvalidateUserTokenUseCase;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class InvalidateUserTokenUseCaseImpl implements InvalidateUserTokenUseCase {

	private final AuthenticationDataRepository repository;
	private final JwtTokenDataExtractorService jwtReaderService;
	private final Logger logger = LoggerFactory.getLogger(InvalidateUserTokenUseCaseImpl.class);
	private final GetUserFromTokenUsernameService getUserFromTokenUsernameService;

	public Boolean apply(String jwtToken) {
		//here is being throw the error
		String username = jwtReaderService.extractUsername(jwtToken);
		final var user = getUserFromTokenUsernameService.getUserFromTokenUsername(username);

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

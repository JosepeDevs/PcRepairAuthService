package com.josepedevs.application.usecase.authenticationdata;

import com.josepedevs.application.service.GetUserFromTokenUsernameService;
import com.josepedevs.application.service.JwtTokenDataExtractorService;
import com.josepedevs.domain.repository.AuthenticationDataRepository;
import com.josepedevs.domain.usecase.InvalidateAuthenticationDataTokenUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class InvalidateAuthenticationDataTokenUseCaseImpl implements InvalidateAuthenticationDataTokenUseCase {

	private final AuthenticationDataRepository authenticationDataRepository;
	private final JwtTokenDataExtractorService jwtReaderService;
	private final GetUserFromTokenUsernameService getUserFromTokenUsernameService;

	public Boolean apply(String jwtToken) {

		final var username = jwtReaderService.extractUsername(jwtToken);
		final var authData = getUserFromTokenUsernameService.getUserFromTokenUsername(username);
		authData.setCurrentToken("invalidated");
		final var  success = authenticationDataRepository.invalidateToken(authData);
		if(success) {
			log.info("The token was correctly invalidated.");
			return true;
		}
		log.info("The token could not be invalidated.");
		return false;

	}
	
	
	
}

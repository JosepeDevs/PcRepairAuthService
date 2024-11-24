package com.josepedevs.application.usecase.authenticationdata;

import com.josepedevs.application.service.GetUserFromTokenUsernameService;
import com.josepedevs.application.service.JwtTokenDataExtractorService;
import com.josepedevs.domain.entity.AuthenticationData;
import com.josepedevs.domain.repository.AuthenticationDataRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InvalidateAuthenticationDataTokenUseCaseImplTest {

	@InjectMocks
	private InvalidateAuthenticationDataTokenUseCaseImpl useCase;

	@Mock
	private AuthenticationDataRepository repository;

	@Mock
	private JwtTokenDataExtractorService jwtReaderService;

	@Mock
	private  GetUserFromTokenUsernameService getUserFromTokenUsernameService;

	private final EasyRandom easyRandom = new EasyRandom();
	
	@Test
	void invalidateToken_GivenValidUser_ThenInvalidateTokenAndReturnTrue() {
		
		final var authData = easyRandom.nextObject(AuthenticationData.class);
	    
		when(jwtReaderService.extractUsername(authData.getCurrentToken())).thenReturn(authData.getUsername());
		when(repository.invalidateToken(authData)).thenReturn(true);
		when(getUserFromTokenUsernameService.getUserFromTokenUsername(authData.getUsername())).thenReturn(authData);
		
		final var actual = useCase.apply(authData.getCurrentToken());

		assertTrue(actual);
	}

	@Test
	void invalidateToken_GivenValidUser_ThenInvalidateTokenFailsAndReturnFalse() {

		final var authData = easyRandom.nextObject(AuthenticationData.class);

		when(jwtReaderService.extractUsername(authData.getCurrentToken())).thenReturn(authData.getUsername());
		when(repository.invalidateToken(authData)).thenReturn(false);
		when(getUserFromTokenUsernameService.getUserFromTokenUsername(authData.getUsername())).thenReturn(authData);

		final var actual = useCase.apply(authData.getCurrentToken());

		assertFalse(actual);
	}

}

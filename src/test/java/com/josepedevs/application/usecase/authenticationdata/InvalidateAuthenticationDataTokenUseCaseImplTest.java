package com.josepedevs.application.usecase.authenticationdata;

import com.josepedevs.application.service.JwtRoleValidator;
import com.josepedevs.domain.entity.AuthenticationData;
import com.josepedevs.domain.exceptions.UserNotFoundException;
import com.josepedevs.domain.repository.AuthenticationDataRepository;
import com.josepedevs.domain.request.InvalidateTokenRequest;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvalidateAuthenticationDataTokenUseCaseImplTest {

	@InjectMocks
	private InvalidateAuthenticationDataTokenUseCaseImpl useCase;

	@Mock
	private JwtRoleValidator jwtRoleValidator;

	@Mock
	private AuthenticationDataRepository repository;

	private final EasyRandom easyRandom = new EasyRandom();
	
	@Test
	void invalidateToken_GivenValidUser_ThenInvalidateTokenAndReturnTrue() {

		final var authData = easyRandom.nextObject(AuthenticationData.class);
		final var request = easyRandom.nextObject(InvalidateTokenRequest.class);

		when(jwtRoleValidator.isTokenFromAdmin(request.getJwtToken())).thenReturn(true);
		when(repository.findById(request.getAuthDataId())).thenReturn(Optional.of(authData));
		when(repository.invalidateToken(authData)).thenReturn(true);

		final var actual = useCase.apply(request);

		assertTrue(actual);
	}

	@Test
	void invalidateToken_GivenNonExistentUser_ThenThrowsUserNotFound() {

		final var request = easyRandom.nextObject(InvalidateTokenRequest.class);

		when(jwtRoleValidator.isTokenFromAdmin(request.getJwtToken())).thenReturn(true);
		when(repository.findById(request.getAuthDataId())).thenReturn(Optional.empty());
		// Act & Assert
		assertThrows(UserNotFoundException.class, () -> {
			useCase.apply(request);
		});
	}


	@Test
	void invalidateToken_GivenValidUser_ThenInvalidateTokenFailsAndReturnFalse() {

		final var authData = easyRandom.nextObject(AuthenticationData.class);
		final var request = easyRandom.nextObject(InvalidateTokenRequest.class);

		when(jwtRoleValidator.isTokenFromAdmin(request.getJwtToken())).thenReturn(true);
		when(repository.findById(request.getAuthDataId())).thenReturn(Optional.of(authData));
		when(repository.invalidateToken(authData)).thenReturn(false);

		final var actual = useCase.apply(request);

		assertFalse(actual);
	}

}

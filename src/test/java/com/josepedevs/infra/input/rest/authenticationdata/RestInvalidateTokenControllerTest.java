package com.josepedevs.infra.input.rest.authenticationdata;

import com.josepedevs.application.usecase.authenticationdata.InvalidateAuthenticationDataTokenUseCaseImpl;
import com.josepedevs.domain.request.InvalidateTokenRequest;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Objects;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) //tells JUnit to use Mockito for mocking dependencies
public class RestInvalidateTokenControllerTest {

		@InjectMocks
		private RestInvalidateTokenController controller;

		@Mock
		private InvalidateAuthenticationDataTokenUseCaseImpl invalidateUseCase;

		private final EasyRandom easyRandom = new EasyRandom();

		@Test
		void  invalidateToken_GivenTrueApply_ShouldReturnNoContent(){
			// Arrange
			final var jwtToken ="Bearer tokenValue";
			final var jwtTokenValue ="tokenValue";
			final var authDataId = UUID.randomUUID();
			final var request = easyRandom.nextObject(InvalidateTokenRequest.class).toBuilder().jwtToken(jwtTokenValue).authDataId(authDataId).build();
			when(invalidateUseCase.apply(request)).thenReturn(true);

			// Act
			final var finalResult = controller.invalidateToken(jwtToken, authDataId);

			// Assert
			Assertions.assertEquals(HttpStatus.NO_CONTENT, finalResult.getStatusCode());
			Assertions.assertTrue(Objects.requireNonNull(finalResult.getBody()).isEmpty());
		}

	@Test
	void  invalidateToken_GivenFalseApply_ShouldReturnNoContent(){
		// Arrange
		final var jwtToken ="Bearer tokenValue";
		final var jwtTokenValue ="tokenValue";
		final var authDataId = UUID.randomUUID();
		final var request = easyRandom.nextObject(InvalidateTokenRequest.class).toBuilder().jwtToken(jwtTokenValue).authDataId(authDataId).build();
		when(invalidateUseCase.apply(request)).thenReturn(false);

		// Act
		final var finalResult = controller.invalidateToken(jwtToken, authDataId);

		// Assert
		Assertions.assertEquals(HttpStatus.NO_CONTENT, finalResult.getStatusCode());
		Assertions.assertTrue(Objects.requireNonNull(finalResult.getBody()).isEmpty());
	}
}

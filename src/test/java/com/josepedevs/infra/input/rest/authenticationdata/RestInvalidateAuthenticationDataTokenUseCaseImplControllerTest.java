package com.josepedevs.infra.input.rest.authenticationdata;

import com.josepedevs.application.usecase.authenticationdata.InvalidateAuthenticationDataTokenUseCaseImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class) //tells JUnit to use Mockito for mocking dependencies
public class RestInvalidateAuthenticationDataTokenUseCaseImplControllerTest {

		@InjectMocks
		private RestInvalidateTokenController controller;

		@Mock
		private InvalidateAuthenticationDataTokenUseCaseImpl invalidateUseCase; //= mock(InvalidateToken.class, RETURNS_DEEP_STUBS);

		@Test
		void  invalidateToken_ShouldReturnResponseStatusOk(){
			// Arrange
			final var jwtToken ="Bearer tokenValue";
			final var userId = UUID.randomUUID();

			// Act
			final var finalResult = controller.invalidateToken(jwtToken, userId);

			// Assert
			assertEquals(HttpStatus.NO_CONTENT, finalResult.getStatusCode());
			assertTrue(Objects.requireNonNull(finalResult.getBody()).isEmpty());
		}
}

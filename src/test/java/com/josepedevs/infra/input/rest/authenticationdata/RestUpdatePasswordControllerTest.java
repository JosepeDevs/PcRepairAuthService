package com.josepedevs.infra.input.rest.authenticationdata;

import com.josepedevs.application.usecase.authenticationdata.PatchAuthenticationDataPasswordUseCaseImpl;
import com.josepedevs.domain.request.PatchUserPasswordRequest;
import com.josepedevs.infra.input.rest.authenticationdata.mapper.RestAuthenticationDataMapper;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RestUpdatePasswordControllerTest {

		@InjectMocks
		private RestUpdatePasswordController controller;

		@Mock
		private RestAuthenticationDataMapper mapper;

		@Mock
		private PatchAuthenticationDataPasswordUseCaseImpl patchAuthenticationDataPasswordUseCaseImplUseCase;

		private final EasyRandom easyRandom = new EasyRandom();

		@Test
		void newpassword_GivenNoChangeWasMade_ThenReturnsBadRequest (){
			// Arrange
			final var jwtToken ="Bearer tokenValue";
			final var jwtTokenValue ="tokenValue";
			final var newpsswrd ="123";
			final var authDataId = UUID.randomUUID();
			final var request = easyRandom.nextObject(PatchUserPasswordRequest.class)
					.toBuilder()
					.jwtToken(jwtTokenValue)
					.authDataId(authDataId)
					.newPassword(newpsswrd)
					.build();

			when(patchAuthenticationDataPasswordUseCaseImplUseCase.apply(request)).thenReturn(false);
			when(mapper.map(jwtTokenValue, authDataId, newpsswrd)).thenReturn(request);

			// Act
			ResponseEntity<Boolean> finalResult = controller.newpassword(jwtToken, authDataId, newpsswrd);

			// Assert
			assertEquals(HttpStatus.BAD_REQUEST,finalResult.getStatusCode());
			assertEquals(false,finalResult.getBody());

		}
		
		@Test
		void newpassword_GivenUpdatedPassword_ThenReturnNoResponse(){
			// Arrange
			final var jwtToken ="Bearer tokenValue";
			final var jwtTokenValue ="tokenValue";
			String  newpsswrd ="123";
			final var authDataId = UUID.randomUUID();
			final var request = easyRandom.nextObject(PatchUserPasswordRequest.class).toBuilder().jwtToken(jwtToken).authDataId(authDataId).newPassword(newpsswrd).build();

			when(patchAuthenticationDataPasswordUseCaseImplUseCase.apply(request)).thenReturn(true);
			when(mapper.map(jwtTokenValue, authDataId, newpsswrd)).thenReturn(request);
			// Act
			ResponseEntity<Boolean> finalResult = controller.newpassword(jwtToken, authDataId, newpsswrd);
			// Assert
			assertEquals(HttpStatus.NO_CONTENT,finalResult.getStatusCode());
			assertEquals(true,finalResult.getBody());

		}
}

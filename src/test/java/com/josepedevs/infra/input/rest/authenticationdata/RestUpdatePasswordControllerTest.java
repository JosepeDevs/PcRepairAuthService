package com.josepedevs.infra.input.rest.authenticationdata;

import com.josepedevs.application.usecase.authenticationdata.PatchAuthenticationDataPasswordUseCaseImpl;
import com.josepedevs.domain.request.PatchUserPasswordRequest;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RestUpdatePasswordControllerTest {

		@Mock
		PatchAuthenticationDataPasswordUseCaseImpl patchAuthenticationDataPasswordUseCaseImplUseCase;

		@InjectMocks
		RestUpdatePasswordController controller;

		private EasyRandom easyRandom = new EasyRandom();

		@Test
		void newpassword_ShouldReturnStatusResponseBadRequestIfNoChangeWasMade (){

			String jwtToken ="tokenValue";
			String  newpsswrd ="123";
			boolean psswrdWasNotChanged = true;
			boolean bodyResponse = false; // because it failed
			final var request = easyRandom.nextObject(PatchUserPasswordRequest.class);

			when(patchAuthenticationDataPasswordUseCaseImplUseCase.apply(request)).thenReturn(psswrdWasNotChanged);
			ResponseEntity<Boolean> finalResult = controller.newpassword(jwtToken, newpsswrd);
			
			assertEquals(HttpStatus.BAD_REQUEST,finalResult.getStatusCode());
			assertEquals(bodyResponse,finalResult.getBody());

		}
		
		@Test
		void newpassword_ShouldReturnStatusResponseNoResponseIfPasswordWasUpdated(){

			String jwtToken ="tokenValue";
			String  newpsswrd ="123";
			
			boolean psswrdWasNotChanged = false;
			boolean bodyResponse = true;
			final var request = easyRandom.nextObject(PatchUserPasswordRequest.class);

			when(patchAuthenticationDataPasswordUseCaseImplUseCase.apply(request)).thenReturn(psswrdWasNotChanged);
			ResponseEntity<Boolean> finalResult = controller.newpassword(jwtToken, newpsswrd);
			
			assertEquals(HttpStatus.NO_CONTENT,finalResult.getStatusCode());
			assertEquals(bodyResponse,finalResult.getBody());

		}
		
		
		
}

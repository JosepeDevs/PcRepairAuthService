package com.josepdevs.Infra.input.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.josepedevs.Application.InvalidateToken;
import com.josepedevs.Domain.entities.AuthenticationData;
import com.josepedevs.Infra.input.rest.InvalidateTokenController;

@ExtendWith(MockitoExtension.class) //tells JUnit to use Mockito for mocking dependencies
public class InvalidateTokenControllerTest {
	 
		@Mock
		private InvalidateToken invalidateUseCase; //= mock(InvalidateToken.class, RETURNS_DEEP_STUBS);
		
		@InjectMocks
		private InvalidateTokenController controller;
		
		@Test
		void  invalidateToken_ShouldReturnResponseStatusOk(){
			String jwtToken ="Bearer tokenValue";
			boolean result = true;
			
			//when use case, prepare return
			 AuthenticationData mockAuthenticationData = mock(AuthenticationData.class);
			ResponseEntity<String> finalResult = controller.invalidateToken(jwtToken);
			
			//token value was obtained correctly
			assertThat(jwtToken="tokenValue");
			//check that returns NO_CONTENT when result from usecase is TRUE
			assertThat(finalResult.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
			//check that no response is sent since it is a command, no return should be made
			assertThat(finalResult.getBody()).isEqualTo("");

		}
		
		
		
		
}

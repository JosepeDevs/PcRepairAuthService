package com.josepedevs.infra.input.rest.authenticationdata;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.josepedevs.application.usecase.authenticationdata.InvalidateAuthenticationDataTokenUseCaseImpl;
import com.josepedevs.infra.output.persistence.jpa.postgresql.authenticationdata.entity.AuthenticationDataEntity;

@ExtendWith(MockitoExtension.class) //tells JUnit to use Mockito for mocking dependencies
public class InvalidateAuthenticationDataTokenUseCaseImplControllerTest {
	 
		@Mock
		private InvalidateAuthenticationDataTokenUseCaseImpl invalidateUseCase; //= mock(InvalidateToken.class, RETURNS_DEEP_STUBS);
		
		@InjectMocks
		private RestInvalidateTokenController controller;
		
		@Test
		void  invalidateToken_ShouldReturnResponseStatusOk(){
			String jwtToken ="Bearer tokenValue";
			boolean result = true;
			
			//when use case, prepare return
			 AuthenticationDataEntity mockAuthenticationDataEntity = mock(AuthenticationDataEntity.class);
			ResponseEntity<String> finalResult = controller.invalidateToken(jwtToken);
			
			//token value was obtained correctly
			assertThat(jwtToken="tokenValue");
			//check that returns NO_CONTENT when result from usecase is TRUE
			assertThat(finalResult.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
			//check that no response is sent since it is a command, no return should be made
			assertThat(finalResult.getBody()).isEqualTo("");

		}
		
		
		
		
}

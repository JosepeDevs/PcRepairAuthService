package com.josepedevs.infra.input.rest.authenticationdata;

import com.josepedevs.application.usecase.authenticationdata.LoginAuthenticationDataUseCaseImpl;
import com.josepedevs.domain.exceptions.BusyOrDownServerException;
import com.josepedevs.domain.request.AuthenticationRequest;
import com.josepedevs.domain.request.AuthenticationResponse;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@RestController

@RequestMapping("api/v1/noauth")

@ExtendWith(MockitoExtension.class)
class RestLoginControllerTest {

	@InjectMocks
	private RestLoginController controller;

	@Mock
	private LoginAuthenticationDataUseCaseImpl loginAuthenticationDataUseCaseImplUseCase;

	private final EasyRandom easyRandom = new EasyRandom();

	@Test
	void login_GivenValidToken_ThenReturnsAuthenticationResponse (){
		// Arrange
		final var request = this.easyRandom.nextObject(AuthenticationRequest.class);
		final var response = this.easyRandom.nextObject(AuthenticationResponse.class);

		when(loginAuthenticationDataUseCaseImplUseCase.apply(request)).thenReturn(response);

		// Act
		final var actual = controller.login(request);

		// Assert
		assertEquals(HttpStatus.OK, actual.getStatusCode());
	}

	//AuthenticationException
	@Test
	void loginRetry_ShouldReturnStatusResponseProcessingWhenCustomExceptionHappens (){
        
		ResourceAccessException exception = new ResourceAccessException("message");
		ResponseEntity<String> finalResult = controller.loginRetry(exception);
		
		assertEquals(HttpStatus.PROCESSING, finalResult.getStatusCode());
		assertNotNull(finalResult.getBody());
	}
	
	@Test
	void loginRateLimiter(){
		BusyOrDownServerException exception = new BusyOrDownServerException("message");
		
		ResponseEntity<String> finalResult = controller.loginRateLimiter(exception);
		assertEquals(HttpStatus.PROCESSING, finalResult.getStatusCode());
		assertNotNull(finalResult.getBody());
	}
	
}


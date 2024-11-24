package com.josepedevs.infra.input.rest.authenticationdata;

import com.josepedevs.application.service.GetUserFromTokenUsernameService;
import com.josepedevs.application.service.JwtTokenIssuerService;
import com.josepedevs.application.usecase.authenticationdata.LoginAuthenticationDataUseCaseImpl;
import com.josepedevs.domain.exceptions.BusyOrDownServerException;
import com.josepedevs.domain.repository.AuthenticationDataRepository;
import com.josepedevs.domain.request.AuthenticationResponse;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RestController

@RequestMapping("api/v1/noauth")

@ExtendWith(MockitoExtension.class)
public class LoginAuthenticationDataUseCaseImplControllerTest {

	@InjectMocks
	private RestLoginController controller;

	@Mock
	private LoginAuthenticationDataUseCaseImpl loginAuthenticationDataUseCaseImplUseCase;

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private AuthenticationDataRepository repository;

	@Mock
	private JwtTokenIssuerService jwtService;
	@Mock
	private LoginAuthenticationDataUseCaseImpl useCase;

	@Mock
	private GetUserFromTokenUsernameService getUserFromTokenUsernameService;

	private final EasyRandom easyRandom = new EasyRandom();

	@Test
	void login_GivenValidToken_ThenReturnsAuthenticationResponse (){
		// Arrange
		final var result = this.easyRandom.nextObject(AuthenticationResponse.class);

		// Act
//		final var actual = loginAuthenticationDataUseCaseImplUseCase.apply();

		// Assert
//		assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
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
		BusyOrDownServerException exception = new BusyOrDownServerException("message", "parameter");
		
		ResponseEntity<String> finalResult = controller.loginRateLimiter(exception);
		assertEquals(HttpStatus.PROCESSING, finalResult.getStatusCode());
		assertNotNull(finalResult.getBody());
	}
	
}


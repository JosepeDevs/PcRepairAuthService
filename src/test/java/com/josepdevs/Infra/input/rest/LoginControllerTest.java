package com.josepdevs.Infra.input.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

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

import com.josepedevs.Application.Login;
import com.josepedevs.Domain.dto.AuthenticationRequest;
import com.josepedevs.Domain.dto.AuthenticationResponse;
import com.josepedevs.Domain.exceptions.BusyOrDownServerException;
import com.josepedevs.Infra.input.rest.LoginController;

@RestController

@RequestMapping("api/v1/noauth")

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

	@Mock
	Login loginUseCase;
	
	@InjectMocks
	LoginController controller;

	@Test
	void login_ShouldReturnStatusResponseOk (){
		
		AuthenticationResponse result = AuthenticationResponse.builder().token("tokenValue").build();
		AuthenticationRequest authData = AuthenticationRequest.builder().build();	
		when(loginUseCase.login(authData)).thenReturn(result);
		
		ResponseEntity<AuthenticationResponse> finalResult = controller.login(authData);

		
		assertThat(finalResult.getStatusCode()).isEqualTo(HttpStatus.OK);
	}
	
	
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


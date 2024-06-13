package com.josepdevs.Infra.input.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.josepdevs.Application.Register;
import com.josepdevs.Domain.Exceptions.BusyOrDownServerException;
import com.josepdevs.Domain.dto.AuthenticationResponse;
import com.josepdevs.Domain.dto.RegisterRequest;

@ExtendWith(MockitoExtension.class)
public class RegisterControllerTest {
	
	@Mock
	Register registerUseCase;
	
	@InjectMocks
	RegisterController controller;

	@Test
	void register_ShouldReturnCreatedStatusAndBodyNotBeNull (){
		
		RegisterRequest parameter = RegisterRequest.builder().email("sdasda").psswrd("dsaafsaf").username("nombre").build();
		AuthenticationResponse expectedResult = AuthenticationResponse.builder().token("tokenValue").build();
		
		when(registerUseCase.register(any(RegisterRequest.class))).thenReturn(expectedResult);
		
		ResponseEntity<AuthenticationResponse> finalResult = controller.register(parameter);
		
        verify(registerUseCase, times(1)).register(parameter);
        assertEquals(HttpStatus.CREATED, finalResult.getStatusCode());
        assertNotNull(finalResult.getBody());
		
	}

	@Test
	void  registerRetry_ShouldBeExecutedIfCustomExceptionHappens (){
		BusyOrDownServerException exception = new BusyOrDownServerException("mensaje","atributo");
		ResponseEntity<String> finalResult = controller.registerRetry(exception);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, finalResult.getStatusCode());
		assertNotNull(finalResult.getBody());
	}
}

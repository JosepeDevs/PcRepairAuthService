package com.josepdevs.Infra.input.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.josepedevs.Application.Register;
import com.josepedevs.Domain.dto.AuthenticationResponse;
import com.josepedevs.Domain.dto.RegisterRequest;
import com.josepedevs.Domain.exceptions.BusyOrDownServerException;
import com.josepedevs.Infra.input.rest.RegisterController;

@ExtendWith(MockitoExtension.class)
public class RegisterControllerTest {
	
	@Mock
	Register registerUseCase;
	
	@InjectMocks
	RegisterController controller;

	@Test
	void register_ShouldReturnCreatedStatusAndBodyNotBeNull (){
		
		RegisterRequest parameter = RegisterRequest.builder().email("sdasda").psswrd("dsaafsaf").username("nombre").build();
		UUID expectedResult = UUID.randomUUID();
		
		when(registerUseCase.register(any(RegisterRequest.class))).thenReturn(expectedResult);
		
		ResponseEntity<UUID> finalResult = controller.register(parameter);
		
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

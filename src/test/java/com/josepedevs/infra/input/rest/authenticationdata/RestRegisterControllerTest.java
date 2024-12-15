package com.josepedevs.infra.input.rest.authenticationdata;

import com.josepedevs.application.usecase.authenticationdata.RegisterAuthenticationDataUseCaseImpl;
import com.josepedevs.domain.exceptions.BusyOrDownServerException;
import com.josepedevs.domain.request.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RestRegisterControllerTest {
	
	@Mock
	RegisterAuthenticationDataUseCaseImpl registerAuthenticationDataUseCaseImplUseCase;
	
	@InjectMocks
	RestRegisterController controller;

	@Test
	void register_ShouldReturnCreatedStatusAndBodyNotBeNull (){
		
		RegisterRequest parameter = RegisterRequest.builder().email("sdasda").psswrd("dsaafsaf").username("nombre").build();
		UUID expectedResult = UUID.randomUUID();
		
		when(registerAuthenticationDataUseCaseImplUseCase.apply(any(RegisterRequest.class))).thenReturn(expectedResult);
		
		ResponseEntity<UUID> finalResult = controller.register(parameter);
		
        verify(registerAuthenticationDataUseCaseImplUseCase, times(1)).apply(parameter);
        assertEquals(HttpStatus.CREATED, finalResult.getStatusCode());
        assertNotNull(finalResult.getBody());
		
	}

	@Test
	void  registerRetry_ShouldBeExecutedIfCustomExceptionHappens (){
		BusyOrDownServerException exception = new BusyOrDownServerException("mensaje");
		ResponseEntity<String> finalResult = controller.registerRetry(exception);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, finalResult.getStatusCode());
		assertNotNull(finalResult.getBody());
	}
}

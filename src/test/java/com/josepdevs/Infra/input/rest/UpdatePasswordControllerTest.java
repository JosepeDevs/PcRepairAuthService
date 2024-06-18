package com.josepdevs.Infra.input.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.josepedevs.Application.PatchPassword;
import com.josepedevs.Infra.input.rest.UpdatePasswordController;

@ExtendWith(MockitoExtension.class)
public class UpdatePasswordControllerTest {

		@Mock
		PatchPassword patchPasswordUseCase;

		@InjectMocks
		UpdatePasswordController controller;
		
		@Test
		void newpassword_ShouldReturnStatusResponseBadRequestIfNoChangeWasMade (){

			String jwtToken ="tokenValue";
			String  newpsswrd ="123";
			boolean psswrdWasNotChanged = true;
			boolean bodyResponse = false; // because it failed
			
			when(patchPasswordUseCase.patchPassword(any(String.class),any(String.class))).thenReturn(psswrdWasNotChanged);
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
			
			when(patchPasswordUseCase.patchPassword(any(String.class),any(String.class))).thenReturn(psswrdWasNotChanged);
			ResponseEntity<Boolean> finalResult = controller.newpassword(jwtToken, newpsswrd);
			
			assertEquals(HttpStatus.NO_CONTENT,finalResult.getStatusCode());
			assertEquals(bodyResponse,finalResult.getBody());

		}
		
		
		
}

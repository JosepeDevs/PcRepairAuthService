package com.josepdevs.Infra.input.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import com.josepdevs.Application.PatchRoleUseCase;

@ExtendWith(MockitoExtension.class)
public class UpdateRoleControllerTest {
	
	@Mock
	PatchRoleUseCase patchRoleUseCase;

	@InjectMocks
	UpdateRoleController controller;
	
	  @Test
	    void patchRole_ShouldReturnNoContentIfRoleChanged() {
	        String jwtToken = "Bearer tokenValue";
	        String tokenValue = "tokenValue";
	        String role = "admin"; 
	        String id = UUID.randomUUID().toString(); 
	        
	        UpdateRoleRequest request = new UpdateRoleRequest(id, role); 

	        boolean roleChanged = true;

	        when(patchRoleUseCase.patchRole(tokenValue, UUID.fromString(id), role)).thenReturn(roleChanged);

	        // When
	        ResponseEntity<Boolean> finalResult = controller.patchRole(jwtToken, request);

	        // Then
	        verify(patchRoleUseCase, times(1)).patchRole(tokenValue, UUID.fromString(id), role);
	        assertEquals(HttpStatus.NO_CONTENT, finalResult.getStatusCode());
			assertEquals(roleChanged,finalResult.getBody());
    }

	@Test
	void patchRole_ShouldReturnResponseStatusBadRequestIfRoleWasNotChanged (){
        String jwtToken = "Bearer tokenValue";
        String tokenValue = "tokenValue";
        String role = "admin"; 
        String id = UUID.randomUUID().toString(); 
        
        UpdateRoleRequest request = new UpdateRoleRequest(id, role); 

        boolean roleChanged = false;

        when(patchRoleUseCase.patchRole(tokenValue, UUID.fromString(id), role)).thenReturn(roleChanged);

        ResponseEntity<Boolean> finalResult = controller.patchRole(jwtToken, request);

        verify(patchRoleUseCase, times(1)).patchRole(tokenValue, UUID.fromString(id), role);
		assertEquals(HttpStatus.BAD_REQUEST, finalResult.getStatusCode()); 
		assertEquals(roleChanged,finalResult.getBody());
	}
	
}

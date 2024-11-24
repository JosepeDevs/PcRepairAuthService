package com.josepedevs.infra.input.rest.authenticationdata;

import com.josepedevs.application.usecase.authenticationdata.PatchAuthenticationDataRoleUseCaseImpl;
import com.josepedevs.domain.request.PatchUserRoleRequest;
import com.josepedevs.domain.request.UpdateRoleRequest;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RestUpdateRoleControllerTest {
	
	@Mock
	PatchAuthenticationDataRoleUseCaseImpl patchAuthenticationDataRoleUseCaseImpl;

	@InjectMocks
	RestUpdateRoleController controller;

	private EasyRandom easyRandom = new EasyRandom();

	  @Test
	    void patchRole_ShouldReturnNoContentIfRoleChanged() {
	        String jwtToken = "Bearer tokenValue";
	        String tokenValue = "tokenValue";
	        String role = "admin"; 
	        String id = UUID.randomUUID().toString();

		  final var request = PatchUserRoleRequest.builder()
				  .jwtToken(jwtToken)
				  .updateRoleRequest(UpdateRoleRequest.builder().id(id).role(role).build())
				  .build();

	        boolean roleChanged = true;

	        when(patchAuthenticationDataRoleUseCaseImpl.apply(request)).thenReturn(roleChanged);

	        // When
	        ResponseEntity<Boolean> finalResult = controller.patchRole(request.getJwtToken(), request.getUpdateRoleRequest());

	        // Then
	        verify(patchAuthenticationDataRoleUseCaseImpl, times(1)).apply(request);
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
		final var patchRequest = this.easyRandom.nextObject(PatchUserRoleRequest.class);
        boolean roleChanged = false;

        when(patchAuthenticationDataRoleUseCaseImpl.apply(patchRequest)).thenReturn(roleChanged);

        ResponseEntity<Boolean> finalResult = controller.patchRole(jwtToken, request);

        verify(patchAuthenticationDataRoleUseCaseImpl, times(1)).apply(patchRequest);
		assertEquals(HttpStatus.BAD_REQUEST, finalResult.getStatusCode()); 
		assertEquals(roleChanged,finalResult.getBody());
	}
	
}

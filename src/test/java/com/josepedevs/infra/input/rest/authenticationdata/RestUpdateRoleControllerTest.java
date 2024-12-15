package com.josepedevs.infra.input.rest.authenticationdata;

import com.josepedevs.application.usecase.authenticationdata.PatchAuthenticationDataRoleUseCaseImpl;
import com.josepedevs.domain.request.PatchUserRoleRequest;
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
	
	@InjectMocks
	private RestUpdateRoleController controller;

	@Mock
	private PatchAuthenticationDataRoleUseCaseImpl patchAuthenticationDataRoleUseCaseImpl;

	  @Test
	    void patchRole_ShouldReturnNoContentIfRoleChanged() {
	        String jwtToken = "Bearer tokenValue";
	        String jwtTokenValue = "tokenValue";
	        String role = "admin";
	        String id = UUID.randomUUID().toString();

		  final var request = PatchUserRoleRequest.builder()
				  .jwtToken(jwtTokenValue)
				  .id(id)
				  .role(role)
				  .build();

	        Boolean roleChanged = true;

	        when(patchAuthenticationDataRoleUseCaseImpl.apply(request)).thenReturn(roleChanged);

	        ResponseEntity<Boolean> finalResult = controller.patchRole(jwtToken, request);

	        assertEquals(HttpStatus.NO_CONTENT, finalResult.getStatusCode());
			assertEquals(roleChanged,finalResult.getBody());
    }

	@Test
	void patchRole_ShouldReturnResponseStatusBadRequestIfRoleWasNotChanged (){
		String jwtToken = "Bearer tokenValue";
		String jwtTokenValue = "tokenValue";
		String role = "admin";
		String id = UUID.randomUUID().toString();

		final var request = PatchUserRoleRequest.builder()
				.jwtToken(jwtTokenValue)
				.id(id)
				.role(role)
				.build();

		Boolean roleChanged = false;

		when(patchAuthenticationDataRoleUseCaseImpl.apply(request)).thenReturn(roleChanged);

		ResponseEntity<Boolean> finalResult = controller.patchRole(jwtToken, request);

		assertEquals(HttpStatus.BAD_REQUEST, finalResult.getStatusCode());
		assertEquals(roleChanged,finalResult.getBody());
	}
	
}

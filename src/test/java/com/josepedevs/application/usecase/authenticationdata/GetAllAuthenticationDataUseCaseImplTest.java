package com.josepedevs.application.usecase.authenticationdata;

import com.josepedevs.application.service.JwtTokenValidations;
import com.josepedevs.domain.entity.AuthenticationData;
import com.josepedevs.domain.exceptions.UserNotFoundException;
import com.josepedevs.domain.repository.AuthenticationDataRepository;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetAllAuthenticationDataUseCaseImplTest {

	@InjectMocks
	private GetAllAuthenticationDataUseCaseImpl useCase;

	@Mock
	private AuthenticationDataRepository repository;

	@Mock
	private JwtTokenValidations jwtValidations;

	private final EasyRandom easyRandom = new EasyRandom();

	@Test
	void apply_GivenAdminRoleAndValidToken_ThenReturnsAuthDataList() {

		final var authData = this.easyRandom.nextObject(AuthenticationData.class);
		final var authDataList = List.of(authData);

		when(repository.getAll()).thenReturn(authDataList);
		when(jwtValidations.isAdminTokenCompletelyValidated(authData.getCurrentToken())).thenReturn(true);

		final var finalResult = useCase.apply(authData.getCurrentToken());

		assertEquals(authDataList, finalResult);
	}
	
	@Test
	void apply_GivenNonFoundUserOrNotAdminRole_ThenReturnsException() {

		final var authData = this.easyRandom.nextObject(AuthenticationData.class);

		when(jwtValidations.isAdminTokenCompletelyValidated(authData.getCurrentToken())).thenReturn(false);

		 assertThrows(UserNotFoundException.class, () -> {
			 this.useCase.apply(authData.getCurrentToken());
         });
	}

}

package com.josepdevs.Application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.josepedevs.infra.output.persistence.jpa.postgresql.authenticationdata.entity.AuthenticationDataEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.josepedevs.application.usecase.user.GetAllusersUseCaseImpl;
import com.josepedevs.domain.exceptions.UserNotFoundException;
import com.josepedevs.domain.repository.AuthenticationDataRepository;
import com.josepedevs.application.service.GetUserFromTokenUsernameService;
import com.josepedevs.application.service.JwtTokenDataExtractorService;
import com.josepedevs.application.service.JwtTokenValidations;

@ExtendWith(MockitoExtension.class)
public class GetAllusersUseCaseImplTest {

	@Mock
	AuthenticationDataRepository repository;
	
	@Mock
	JwtTokenDataExtractorService jwtReaderService;

	@Mock
	JwtTokenValidations jwtValidations;
	
	GetUserFromTokenUsernameService getUserFromTokenUsernameService = mock(GetUserFromTokenUsernameService.class);

	@InjectMocks
	GetAllusersUseCaseImpl useCase;
	
	@Test
	void getAll_ShouldReturnAllUsersIfUserIsAdmin() {

		String jwtToken ="tokenValue";
		String username ="specificUserName";
		UUID userId = UUID.randomUUID();
		boolean success = true;

		
		AuthenticationDataEntity authenticationDataEntity = AuthenticationDataEntity.builder()
															      .idUser(userId)
																  .email("hola@adios.com")
																  .username(username)
																  .role("ADMIN")
																  .psswrd(username)
																  .currentToken(jwtToken)
																  .active(true)
																  .build();
																  
		List<AuthenticationDataEntity> mockDataList = List.of(
				AuthenticationDataEntity.builder()
					.idUser(UUID.fromString("d006b05b-0781-4016-874d-fcacc892f51e"))
					.username("pepito")
					.email("pepi@ito.com")
					.psswrd("123")
					.role("ADMIN")
					.currentToken("invalidated")
					.active(true)
					.build()
			,
				AuthenticationDataEntity.builder()
					.idUser(UUID.fromString("499e0779-0115-4110-87ae-87c804a4a7ff"))
					.username("Astronauta")
					.email("espacial@user.com")
					.psswrd("1234")
					.role("USER")
					.currentToken("$2a$10$fTW3mvR1ZeXalGlCzpLknuAilhC9JA/.X6xeW6gcvFQxV.ex1/jIe")
					.active(false)
					.build()
			,
				AuthenticationDataEntity.builder()
					.idUser(UUID.fromString("3cfd698e-fa1a-41df-9527-6316593cfc46"))
					.username("LegolasXXX")
					.email("LOTR@4ever.com")
					.psswrd("4ever")
					.role("EDITOR")
					.currentToken("tokenNoValido")
					.active(true)
					.build()
			,
				AuthenticationDataEntity.builder()
					.idUser(UUID.fromString("3cfd698e-fa1a-41df-9527-6316593cfc46"))
					.username("LegolasXXX")
					.email("LOTR@4ever.com")
					.psswrd("4ever")
					.role("EDITOR")
					.currentToken("ey43rewfs.f2d323f2f2df")
					.active(true)
					.build()
		);
		
		Optional<AuthenticationDataEntity> userDataAuth =  Optional.ofNullable(authenticationDataEntity);

		when(jwtReaderService.extractUsername(jwtToken)).thenReturn(username);
		when(repository.getAll()).thenReturn(mockDataList);
		when(getUserFromTokenUsernameService.getUserFromTokenUsername(username)).thenReturn(authenticationDataEntity);
		when(jwtValidations.isAdminTokenCompletelyValidated(jwtToken)).thenReturn(success);

		
		List<AuthenticationDataEntity> finalResult = useCase.getAll(jwtToken);

		assertThat(finalResult).isInstanceOf(List.class);
	    assertThat(finalResult.size() != 0);

	}
	
	@Test
	void getAll_ShouldReturnEmptyListIfNotAdmin() {

		String jwtToken ="tokenValue";
		String username ="specificUserName";
		boolean success = false;

		UUID userId = UUID.randomUUID();
		
		when(jwtReaderService.extractUsername(jwtToken)).thenReturn(username);
		
		AuthenticationDataEntity authenticationDataEntity = AuthenticationDataEntity.builder()
															      .idUser(userId)
																  .email("hola@adios.com")
																  .username(username)
																  .role("not admin")
																  .psswrd(username)
																  .currentToken(jwtToken)
																  .active(true)
																  .build();
																  
		Optional<AuthenticationDataEntity> userDataAuth =  Optional.ofNullable(authenticationDataEntity);

		when(getUserFromTokenUsernameService.getUserFromTokenUsername(username)).thenReturn(authenticationDataEntity);
		when(jwtReaderService.extractUsername(jwtToken)).thenReturn(username);
		when(jwtValidations.isAdminTokenCompletelyValidated(jwtToken)).thenReturn(success);

		 List<AuthenticationDataEntity> resultList = useCase.getAll(jwtToken);
		 assertThat(resultList.isEmpty());

	}
	
	@Test
	void getAll_ShouldReturnExceptionIfOptionalDidNotFindUser() {

		String username ="specificUserName";
		
        Mockito.when(repository.findByUsername(username)).thenReturn(Optional.empty());

		 assertThrows(UserNotFoundException.class, () -> {
	             repository.findByUsername(username).orElseThrow(() -> {
	                throw new UserNotFoundException("The user was not found or the token does not containe the required data.", "Username");
            });
         });

	}
		
		 
}

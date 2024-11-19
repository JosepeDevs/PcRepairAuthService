package com.josepdevs.Application;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.josepedevs.infra.output.persistence.jpa.postgresql.authenticationdata.entity.AuthenticationDataEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.josepedevs.application.usecase.user.InvalidateUserTokenUseCaseImpl;
import com.josepedevs.domain.repository.AuthenticationDataRepository;
import com.josepedevs.application.service.GetUserFromTokenUsernameService;
import com.josepedevs.application.service.JwtTokenDataExtractorService;

@ExtendWith(MockitoExtension.class)
public class InvalidateUserTokenUseCaseImplTest {
	
	@Mock
	AuthenticationDataRepository repository;

	private JwtTokenDataExtractorService jwtReaderService = mock(JwtTokenDataExtractorService.class);
	private  GetUserFromTokenUsernameService getUserFromTokenUsernameService = mock(GetUserFromTokenUsernameService.class);

	@InjectMocks
	InvalidateUserTokenUseCaseImpl useCase;
	
   @Mock
   TestEntityManager entityManager;
	
	 @BeforeEach
	   void setUp() {
		   //explicitly triggers initialization  of annotations @Mock, @Spy, @Captor, etc.
		   //separates concerns by keeping the setup logic in one place.
	       MockitoAnnotations.openMocks(this);
	       
	       List<AuthenticationDataEntity> mockDataList = List.of(
	    		   AuthenticationDataEntity.builder()
					  .idUser(UUID.randomUUID())
					  .email("hola@adios.com")
					  .username("specificUsername")
					  .role("not admin")
					  .psswrd("123hashed")
					  .currentToken("tokenValue")
					  .active(true)
					  .build()
			);
	       for(AuthenticationDataEntity user :mockDataList ){
	    	   entityManager.persistAndFlush(user);
	       }
	       
	   }
	
	@Test
	void invalidateToken() {
		
		String jwtToken = "tokenValue";
		String username ="specificUsername";
		AuthenticationDataEntity mockAuthenticationDataEntity = mock(AuthenticationDataEntity.class);

	    AuthenticationDataEntity.builder()
				  .idUser(UUID.randomUUID())
				  .email("hola@adios.com")
				  .username(username)
				  .role("not admin")
				  .psswrd("123hashed")
				  .currentToken(jwtToken)
				  .active(true)
				  .build();
	    
		Optional<AuthenticationDataEntity> optionaluser = Optional.of(mockAuthenticationDataEntity);
		boolean success = true;
		
		when(jwtReaderService.extractUsername(jwtToken)).thenReturn(username);
		when(getUserFromTokenUsernameService.getUserFromTokenUsername(username)).thenReturn(mockAuthenticationDataEntity);
		
		boolean finalResult = useCase.invalidateToken(jwtToken);
	}
	
	
	
}

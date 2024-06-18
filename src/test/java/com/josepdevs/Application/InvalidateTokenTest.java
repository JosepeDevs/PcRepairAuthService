package com.josepdevs.Application;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.josepedevs.Application.InvalidateToken;
import com.josepedevs.Domain.entities.AuthenticationData;
import com.josepedevs.Domain.repository.AuthRepository;
import com.josepedevs.Domain.service.GetUserFromTokenUsernameService;
import com.josepedevs.Domain.service.JwtTokenDataExtractorService;

@ExtendWith(MockitoExtension.class)
public class InvalidateTokenTest {
	
	@Mock
	AuthRepository repository;

	private JwtTokenDataExtractorService jwtReaderService = mock(JwtTokenDataExtractorService.class);
	private  GetUserFromTokenUsernameService getUserFromTokenUsernameService = mock(GetUserFromTokenUsernameService.class);

	@InjectMocks
	InvalidateToken useCase;
	
   @Mock
   TestEntityManager entityManager;
	
	 @BeforeEach
	   void setUp() {
		   //explicitly triggers initialization  of annotations @Mock, @Spy, @Captor, etc.
		   //separates concerns by keeping the setup logic in one place.
	       MockitoAnnotations.openMocks(this);
	       
	       List<AuthenticationData> mockDataList = List.of(
	    		   AuthenticationData.builder()
					  .idUser(UUID.randomUUID())
					  .email("hola@adios.com")
					  .username("specificUsername")
					  .role("not admin")
					  .psswrd("123hashed")
					  .currentToken("tokenValue")
					  .active(true)
					  .build()
			);
	       for(AuthenticationData user :mockDataList ){
	    	   entityManager.persistAndFlush(user);
	       }
	       
	   }
	
	@Test
	void invalidateToken() {
		
		String jwtToken = "tokenValue";
		String username ="specificUsername";
		AuthenticationData mockAuthenticationData = mock(AuthenticationData.class);

	    AuthenticationData.builder()
				  .idUser(UUID.randomUUID())
				  .email("hola@adios.com")
				  .username(username)
				  .role("not admin")
				  .psswrd("123hashed")
				  .currentToken(jwtToken)
				  .active(true)
				  .build();
	    
		Optional<AuthenticationData> optionaluser = Optional.of(mockAuthenticationData);
		boolean success = true;
		
		when(jwtReaderService.extractUsername(jwtToken)).thenReturn(username);
		when(getUserFromTokenUsernameService.getUserFromTokenUsername(username)).thenReturn(mockAuthenticationData);
		
		boolean finalResult = useCase.invalidateToken(jwtToken);
	}
	
	
	
}

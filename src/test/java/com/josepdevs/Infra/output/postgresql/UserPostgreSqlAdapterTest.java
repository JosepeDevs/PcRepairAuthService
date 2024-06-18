package com.josepdevs.Infra.output.postgresql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import com.josepedevs.Domain.entities.AuthenticationData;
import com.josepedevs.Infra.output.AuthJpaRepository;
import com.josepedevs.Infra.output.postgresql.UserPostgreSqlAdapter;

@ExtendWith(MockitoExtension.class)
public class UserPostgreSqlAdapterTest {

	final AuthJpaRepository userJpaRepository = mock(AuthJpaRepository.class);
	
   @InjectMocks
    UserPostgreSqlAdapter adapter;
   
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
			,
    		   AuthenticationData.builder()
					.idUser(UUID.fromString("d006b05b-0781-4016-874d-fcacc892f51e"))
					.username("pepito")
					.email("pepi@ito.com")
					.psswrd("123")
					.role("ADMIN")
					.currentToken("invalidated")
					.active(true)
					.build()
			,
				AuthenticationData.builder()
					.idUser(UUID.fromString("499e0779-0115-4110-87ae-87c804a4a7ff"))
					.username("Astronauta")
					.email("espacial@user.com")
					.psswrd("1234")
					.role("USER")
					.currentToken("$2a$10$fTW3mvR1ZeXalGlCzpLknuAilhC9JA/.X6xeW6gcvFQxV.ex1/jIe")
					.active(false)
					.build()
			,
				AuthenticationData.builder()
					.idUser(UUID.fromString("3cfd698e-fa1a-41df-9527-6316593cfc46"))
					.username("LegolasXXX")
					.email("LOTR@4ever.com")
					.psswrd("4ever")
					.role("EDITOR")
					.currentToken("tokenNoValido")
					.active(true)
					.build()
			,
				AuthenticationData.builder()
					.idUser(UUID.fromString("3cfd698e-fa1a-41df-9527-6316593cfc46"))
					.username("LegolasXXX")
					.email("LOTR@4ever.com")
					.psswrd("4ever")
					.role("EDITOR")
					.currentToken("ey43rewfs.f2d323f2f2df")
					.active(true)
					.build()
		);
       for(AuthenticationData user :mockDataList ){
    	   entityManager.persistAndFlush(user);
       }
       
   }

	@Test
	void registerUserAuthData_shouldSaveCorrectlyData() {

		String jwtToken ="tokenValue";
		String username ="specificUserName";
		UUID id = UUID.randomUUID();
		AuthenticationData authenticationData = AuthenticationData.builder()
				  .email("hola@adios.com")
				  .username(username)
				  .role("not admin")
				  .psswrd(username)
				  .currentToken(jwtToken)
				  .active(true)
				  .build();
		
		AuthenticationData authenticationDataWithId = AuthenticationData.builder()
				  .idUser(id)
				  .email("hola@adios.com")
				  .username(username)
				  .role("not admin")
				  .psswrd(username)
				  .currentToken(jwtToken)
				  .active(true)
				  .build();
				  
		// this is commented because the mock repository already exposes this behaviour: if I pass a user without ID the return of save is that same user
		//without ID. save user with Id returns that same user with the same ID
       //therefore this is commented because it is not necesary, but still is a good way to intercept what is being sent to inspect it.
		// when(userJpaRepository.save(any(AuthenticationData.class))).thenAnswer( invocation -> invocation.getArgument(0)); // incercepted userdata should not have id, it is generated in the database
		AuthenticationData finalResult = adapter.registerUserAuthData(authenticationData, jwtToken);
		
		 assertNotNull(finalResult);
		assertNull(finalResult.getIdUser()); 
		
		AuthenticationData finalResultWithExpectedId = adapter.registerUserAuthData(authenticationDataWithId, jwtToken);

	 	 assertNotNull(finalResultWithExpectedId.getIdUser());
	 	 finalResult.setIdUser(id);
	 	 assertThat(finalResult.equals(finalResultWithExpectedId));	
	}

	@Test
	void  findByUsername_shouldReturnUserIfFound() {
		String username ="specificUsername";
		AuthenticationData authenticationDataWithId = AuthenticationData.builder()
				  .idUser(UUID.randomUUID())
				  .email("hola@adios.com")
				  .username(username)
				  .role("not admin")
				  .psswrd("123hashed")
				  .currentToken("tokenValue")
				  .active(true)
				  .build();
		Optional<AuthenticationData> optionaluser = Optional.of(authenticationDataWithId);
		when(userJpaRepository.findByUsername(username)).thenReturn(optionaluser);
		Optional<AuthenticationData> finalResult = adapter.findByUsername(username);
		AuthenticationData extractedUser = finalResult.get();
		assertNotNull(finalResult);
		assertNotNull(extractedUser);
	    assertTrue(finalResult.isPresent());
	}

	@Test
	void  findByUsername_shouldReturnEmptyOptionalIUsernamefNotFound() {
		String username ="specificUsername";
		Optional<AuthenticationData> emptyOptionaluser =  Optional.empty();
		when(userJpaRepository.findByUsername(username)).thenReturn(emptyOptionaluser);
		Optional<AuthenticationData> finalResult = adapter.findByUsername(username);
	    assertTrue(finalResult.isEmpty());
	    assertTrue(emptyOptionaluser.isEmpty());

	}
	
	
	//PENDING
	/*
	@Test 
	public  login_should(AuthenticationData authData, String jwtToken) {
		
		
		boolean finalResult = adapter.login(authData, jwtToken)
		String previousToken = authData.getCurrentToken();
		authData.setCurrentToken(jwtToken);
        AuthenticationData savedUser = userJpaRepository.save(authData);
        if (savedUser.getCurrentToken().equals(jwtToken)) {
        	return true;
        } else {
        	return false;
        }  
	}

	@Override
	public boolean patchPassword(AuthenticationData authData, String digestedPsswrd) {
		
		authData.setPsswrd(digestedPsswrd);
		AuthenticationData savedUser = userJpaRepository.save(authData);
        if( savedUser == authData) {
        	return false;
        } else {
        	return true;
        }  
		
	}

	@Override
	public boolean patchRole(AuthenticationData authData, String role) {
		
		authData.setCurrentToken(role);
        AuthenticationData savedUser = userJpaRepository.save(authData);
        if( savedUser == authData) {
        	return false;
        } else {
        	return true;
        }  
        
	}

	@Override
	public List<AuthenticationData> getAll() {
		return userJpaRepository.findAll();
	}

	@Override
	public boolean invalidateToken(AuthenticationData authData) {

		authData.setCurrentToken("invalidated");
		
        AuthenticationData savedUser = userJpaRepository.save(authData);
        if( savedUser == authData) {
        	return false;
        } else {
        	return true;
        } 
	}
*/
	
	@Test
	void isTokenInvalidated_shouldBeFalseIfTokenIsValid() {
		String token = "tokenValue";
		String username ="specificUsername";
		
	    AuthenticationData mockAuthenticationData = mock(AuthenticationData.class);

	    AuthenticationData.builder()
				  .idUser(UUID.randomUUID())
				  .email("hola@adios.com")
				  .username(username)
				  .role("not admin")
				  .psswrd("123hashed")
				  .currentToken(token)
				  .active(true)
				  .build();
	    
		Optional<AuthenticationData> optionaluser = Optional.of(mockAuthenticationData);
		when(userJpaRepository.findByUsername(username)).thenReturn(optionaluser);
		when(mockAuthenticationData.getCurrentToken()).thenReturn(token);
		boolean finalResult = adapter.isTokenInvalidated(username);
		assertFalse(finalResult);
		
	}

	@Test
	void isTokenInvalidated_shouldBeTrueIfTokenIsNull() {
		String token = null;
		String username ="specificUsername";
		
	    AuthenticationData mockAuthenticationData = mock(AuthenticationData.class);


	    AuthenticationData.builder()
				  .idUser(UUID.randomUUID())
				  .email("hola@adios.com")
				  .username(username)
				  .role("not admin")
				  .psswrd("123hashed")
				  .currentToken(token)
				  .active(true)
				  .build();
	    
		Optional<AuthenticationData> optionaluser = Optional.of(mockAuthenticationData);
		when(userJpaRepository.findByUsername(username)).thenReturn(optionaluser);
		when(mockAuthenticationData.getCurrentToken()).thenReturn(token);
		boolean finalResult = adapter.isTokenInvalidated(username);
		assertTrue(finalResult);
		
	}
	
	@Test
	void isTokenInvalidated_shouldBeTrueIfTokenIsInvalid() {
		String token = "invalidated";
		String username ="specificUsername";
		
	    AuthenticationData mockAuthenticationData = mock(AuthenticationData.class);

	    AuthenticationData.builder()
				  .idUser(UUID.randomUUID())
				  .email("hola@adios.com")
				  .username(username)
				  .role("not admin")
				  .psswrd("123hashed")
				  .currentToken(token)
				  .active(true)
				  .build();
	    
		Optional<AuthenticationData> optionaluser = Optional.of(mockAuthenticationData);
		when(userJpaRepository.findByUsername(username)).thenReturn(optionaluser);
		when(mockAuthenticationData.getCurrentToken()).thenReturn(token);
		boolean finalResult = adapter.isTokenInvalidated(username);
		assertTrue(finalResult);
		
	}
}

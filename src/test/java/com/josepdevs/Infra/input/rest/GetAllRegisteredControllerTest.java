package com.josepdevs.Infra.input.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.Test;

import com.josepedevs.Application.GetAllRegisteredUseCase;
import com.josepedevs.Domain.entities.AuthenticationData;
import com.josepedevs.Domain.exceptions.RequestNotPermittedException;
import com.josepedevs.Infra.input.rest.GetAllRegisteredController;

@ExtendWith(MockitoExtension.class) //tells JUnit to use Mockito for mocking dependencies
public class GetAllRegisteredControllerTest {

    @Mock
    private GetAllRegisteredUseCase useCase; //mock dependencies

    @InjectMocks
    private GetAllRegisteredController controller; //class being tested

    @Test
    void getAll_ShouldReturnAListAndResponseStatusOk() throws Exception {
    	//prepare inputs
    	//we will have a token (it will be validated in other tests), here any string will do
    	String jwtToken = "$2a$10$fTW3mvR1ZeXalGlCzpLknuAilhC9JA/.X6xeW6gcvFQxV.ex1/jIe";
    	//we will have a list of AutheticationData that will be returned if token is valid
		List<AuthenticationData> mockDataList = List.of(
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
									
        
        //prepare return results from dependencies, no matter what I write in the token it will return the specified data
        //this is the Getmapping
		when(useCase.getAll(anyString())).thenReturn(mockDataList);

		//here controller calls the actual method that its executed 
        ResponseEntity<List<AuthenticationData>> result = controller.getAllRegistered(jwtToken);
        //since we prepared an List of AuthenticationData, no matter what I write, instead of jwtToken, it will 
        //return the same list
        
        //then we check that the status code of result is X
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        //we check that the body of the response contains the prepared list
        assertThat(result.getBody()).isEqualTo(mockDataList);
        
        //if the code and the returned list are the expected, then the code is wokring fine
    }

    @Test
    void getFallbackRegisteredBulkHead_ShouldReturnErrorMessageAndResponseStatusTooManyRequest429() {
    	//simulate the condition under which the fallback method should be invoked
    	//create an instance of the exception but do not throw it
    	RequestNotPermittedException exception = new RequestNotPermittedException("Texto de error", "Parametro");
    	
    	//call the controller and pass the created exception as parameter
        ResponseEntity<String> result = controller.getFallbackRegisteredBulkHead(exception);

        //then  check that indeed it returns the expected response when that kind of exception happens
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.TOO_MANY_REQUESTS);
        //check that the response body contains the prepared response
        assertNotNull(result.getBody());
    }
    
    
 
    
}



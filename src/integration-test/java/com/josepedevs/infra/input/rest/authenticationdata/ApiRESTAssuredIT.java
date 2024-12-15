package com.josepedevs.infra.input.rest.authenticationdata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.josepedevs.infra.output.persistence.jpa.postgresql.authenticationdata.entity.AuthenticationDataEntity;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class ApiRESTAssuredIT {

	private static final String REGISTER_URL = "http://localhost:7777/authmanager/api/v1/noauth/register";
	private static final String LOGIN_URL = "http://localhost:6666/authmanager/api/v1/noauth/authenticate";
	private static final String DELETE_URL = "http://localhost:6666/authmanager/api/v1/noauth/authenticate";
	private static final String GET_ALL_URL = "http://localhost:6666/authmanager/api/v1/admin/getall";
	private static final String PATCH_ROLE = "http://localhost:6666/authmanager/api/v1/admin/patchrole";
	private static final String NEW_PSSWRD = "http://localhost:6666/authmanager/newpassword?newpsswrd=123";
	private static final String INVALIDATE_TOKEN = "http://localhost:6666/authmanager/api/v1/admin/invalidate";
	private static String token;
	private static String uuid;

	private static final String LOGIN_BODY = "{\"username\":\"josepe\",\"password\":\"123\"}";

	private static final String REGISTER_BODY = "{\"username\":\"agradecido\",\"email\":\"y@emocionado.com\",\"password\":\"123\"}";

	/**
	 * Test of a pack, if separated or modified the database could retain registered users from tests
	 */
	@Test
	void register_shouldCreateUserAndReturn201() {

		 Response response = RestAssured.given()
				.contentType(ContentType.JSON)
				.body(REGISTER_BODY)
		.when()
			.post(REGISTER_URL);
		 
		 uuid = response.body().toString();
        
		 response.then()
	           .statusCode(201); 
	}
	
	/**
	 * Test of a pack, if separated or modified the database could retain registered users from tests
	 */
	@Test
	void login_shouldAuthenticateAndReturn200() {
		
		 Response response = RestAssured.given()
				.contentType(ContentType.JSON)
				.body(LOGIN_BODY)
		.when()
			.post(LOGIN_URL);
       
           response.jsonPath().getString("token");
           response.then().statusCode(200);
	}

	/**
	 * Test of a pack, if separated or modified the database could retain registered users from tests
	 */
	@Test
	void invalidate_GivenAuthData_ThenReturnWithInvalidatedToken() {

		Response response = RestAssured.given()
				.contentType(ContentType.JSON)
				.body(LOGIN_BODY)
				.when()
				.patch(INVALIDATE_TOKEN);

		response.jsonPath().getString("token");
		response.then().statusCode(200);
	}
	
	/**
	 * Test of a pack, if separated or modified the database could retain registered users from tests
	 */
	@Test
	void deleteHard_shouldDeleteUserIfTokenBearerIsAdmin() {
		
		 Response response = RestAssured.given()
					.contentType(ContentType.JSON)
				     .headers("Authorization", "Bearer " + token)
			.when()
				.post(DELETE_URL);
			 
			 final var uuid = response.body().asString(); //save value in static field to be used in other tests
	        
			 response.then()
		           .statusCode(201); // Asserting created
	}
	
	
	@Test
    void login_BadRequestIfNoBodySpecified() {
    	
		token = RestAssured.given()
				.contentType(ContentType.JSON)
		.when()
			.post(LOGIN_URL)
        .then()
	           .statusCode(400) 
	           .extract().jsonPath().getString("token");	//since response is Json formed just specify key and it will extract value
    }
	
	@Test
    void login_RejectedIfWrongCredentials() {
		
		String incorrectLoginCredentials = "{\n" +
				  "  \"username\": \" josepe \",\n" +
				  "  \"password\": \" 123523\"\n" +
				"}";
		
		 RestAssured.given()
				.contentType(ContentType.JSON)
				.body(incorrectLoginCredentials)
		.when()
			.post(LOGIN_URL)
        .then()
	           .statusCode(403) ;
    }
	
	@Test
	void getAll_shouldReturnAllDataIfTokenIsFromAnAdmin() throws JsonMappingException, JsonProcessingException {
		
		 Response response = RestAssured.given()
					.contentType(ContentType.JSON)
				     .headers("Authorization", "Bearer " + token)
			.when()
				.post(GET_ALL_URL);
			 
			 String responseString = response.body().asString(); //save value in static field to be used in other tests
		        ObjectMapper objectMapper = new ObjectMapper();
		            Map<String, AuthenticationDataEntity> responseMap = objectMapper.readValue(responseString, new TypeReference<Map<String, AuthenticationDataEntity>>(){});
			 response.then()
		           .statusCode(200); // Asserting created
	}
	
   
	/*	@Test
	    void getAll_RestAssured() {
	    	Response response = RestAssured.get("localhost:7777/authmanager/api/v1/admin/getall");
	    	response.peek(); // print to console the response, response object is returned
	    	response.prettyPrint();// print to c000onsole the response pretty is a String object
			response.then()
			.statusCode(200);
			
			RestAssured.given()
						.pathParam("idUser","id del usuario")
						//.auth().basic("userName","password") //this is a challenge to the server, then server request authentication and since it is expecting, minimizes credential unintended  exposure of data
						//.auth().preemtive().basic("userName","password") //sends credential wihtout waiting a request from server 
					.when()
						.get(urlConPlaceHolders)//get request
					.then()
					.body("authenticated", equalTo(true))
						.statusCode(200);
	    }
		*/
}

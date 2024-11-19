package com.josepdevs.Infra.input.rest;

import java.util.Map;

import com.josepedevs.infra.output.persistence.jpa.postgresql.authenticationdata.entity.AuthenticationDataEntity;
import org.junit.Before;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@ExtendWith(MockitoExtension.class)
public class ApiRESTAssuredTest {
	
	
	private final String registerUrl = "http://localhost:7777/authmanager/api/v1/noauth/register";
	private final String loginUrl = "http://localhost:6666/authmanager/api/v1/noauth/authenticate";
	private final String deleteUrl = "http://localhost:6666/authmanager/api/v1/noauth/authenticate";
	private final String getAllUrl = "http://localhost:6666/authmanager/api/v1/admin/getall";
	private final String patchRole = "http://localhost:6666/authmanager/api/v1/admin/patchrole";
	private final String newPsswrd = "http://localhost:6666/authmanager/newpassword?newpsswrd=123";
	private final String invalidateToken = "http://localhost:6666/authmanager/api/v1/admin/invalidate";
	private static String token;
	private static String uuid;
	
	private static final String loginBody = "{\"username\":\"josepe\",\"password\":\"123\"}";

	private static final String registerBody = "{\"username\":\"agradecido\",\"email\":\"y@emocionado.com\",\"password\":\"123\"}";

	@Before
	public void setUp() throws Exception {
	   // RestAssured.port = 4444;
	}

	
	/**
	 * Test 1 of 3 (pack, if separed or modified the database could retain registered users from tests
	 */
	@Test(priority=1)
	void register_shouldCreateUserAndReturn201() {

		 Response response = RestAssured.given()
				.contentType(ContentType.JSON)
				.body(registerBody)
		.when()
			.post(registerUrl);
		 
		 String uuid = response.body().toString();
        
		 response.then()
	           .statusCode(201); 
	}
	
	/**
	 * Test 2 of 3 (pack, if separed or modified the database could retain registered users from tests
	 */
	@Test(priority=2)
	void login_shouldAuthenticateAndReturn200() {
		
		 Response response = RestAssured.given()
				.contentType(ContentType.JSON)
				.body(loginBody)
		.when()
			.post(loginUrl);
       
           response.jsonPath().getString("token");
           response.then().statusCode(200);
	}
	
	/**
	 * Test 3 of 3 (pack, if separed or modified the database could retain registered users from tests
	 */
	@Test(priority=3)
	void deleteHard_shouldDeleteUserIfTokenBearerIsAdmin() {
		
		 Response response = RestAssured.given()
					.contentType(ContentType.JSON)
				     .headers("Authorization", "Bearer " + token)
			.when()
				.post(deleteUrl);
			 
			 uuid = response.body().asString(); //save value in static field to be used in other tests
	        
			 response.then()
		           .statusCode(201); // Asserting created
	}
	
	
	@Test
    void login_BadRequestIfNoBodySpecified() {
    	
		token = RestAssured.given()
				.contentType(ContentType.JSON)
		.when()
			.post(loginUrl)
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
			.post(loginUrl)
        .then()
	           .statusCode(403) ;
    }
	
	@Test
	void getAll_shouldReturnAllDataIfTokenIsFromAnAdmin() throws JsonMappingException, JsonProcessingException {
		
		 Response response = RestAssured.given()
					.contentType(ContentType.JSON)
				     .headers("Authorization", "Bearer " + token)
			.when()
				.post(getAllUrl);
			 
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

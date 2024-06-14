package com.josepdevs.Infra.input.rest;

import static org.hamcrest.CoreMatchers.equalTo;

import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class RestAssuredExamples {
	
	
	final private String urlConPlaceHolders="localhost:7777/personmanager/persons/{idUser}";
	final private String urlDigestAuth="localhost:7777/authmanager/{qop}/{user}/{password}";
	  
	
	final private String body = "{\n" +
			  "  \"username\": \" {USERNAME} \",\n" +
			  "  \"email\": \" {EMAIL} \",\n" +
			  "  \"password\": \" {PASSWORD}\"\n" +
			"}";
	
	
	@Test
	void registerCorrectly() {
		
		String url = "http://localhost:7777/authmanager/api/v1/noauth/register";
		
		RestAssured.given()
		.contentType(ContentType.JSON)
		.body(body)
		.pathParam("USERNAME","agradecido") //this forces to check that both the request and the server are expecting the same digestion hashing
		.pathParam("EAMIL","y@emocionado.com")
		.pathParam("PASSWORD","123")
		.when()
			.post(urlDigestAuth)//get request
		.jsonPath()
		.get("token");
	}
	
	@Test
	void getAll_authenticated() {
		
		String url = "localhost:7777/authmanager/api/v1/admin/getall";
		
		String token = RestAssured.given()
		.contentType(ContentType.JSON)
		.body(body)
		.pathParam("USERNAME","agradecido") //this forces to check that both the request and the server are expecting the same digestion hashing
		.pathParam("EAMIL","y@emocionado.com")
		.pathParam("PASSWORD","123")
		.when()
			.post(urlDigestAuth)//get request
		.jsonPath()
		.get("token");
		
		RestAssured.given()
		 .contentType("application/json")
	     .headers("Authorization", "Bearer " + token)
	     .when()
	     .get("localhost:7777/authmanager/api/v1/admin/getall")
	     .then()
	     .assertThat()
	     .statusCode(HttpStatus.SC_OK);
	}
	
   
		@Test
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
		
		@Test
	    void loginRejectedIfNoAuthSpecified() {
	    	
			
			RestAssured.given()
						.pathParam("qop","auth") //this forces to check that both the request and the server are expecting the same digestion hashing
						.pathParam("user","username")
						.pathParam("password","123")
					.when()
						.get(urlDigestAuth)//get request
					.then()
						.statusCode(401);//if no auth was included expect rejection
	    }
		
		@Test
	    void loginRejectedIfWrongCredentials() {
	    	
			
			RestAssured.given()
						.pathParam("qop","auth") //this forces to check that both the request and the server are expecting the same digestion hashing
						.pathParam("user","username")
						.pathParam("password","123")
						.auth().digest("username", "abc") //behind the scenes this requests a challenge from the server, preemtive does not work with digest
					.when()
						.get(urlDigestAuth)//get request
					.then()
						.statusCode(403);//if no auth was included expect rejection
	    }
		
		@Test
	    void cotillearResponse() {
	    	Response response = RestAssured.get("localhost:7777/authmanager/api/v1/admin/getall");
	    	response.peek(); // print to console the response, response object is returned
	    	response.prettyPrint();// print to c000onsole the response pretty is a String object
			response.then()
			.statusCode(200);
			
	    }
	    
}

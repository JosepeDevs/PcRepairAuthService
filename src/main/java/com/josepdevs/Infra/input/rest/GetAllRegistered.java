package com.josepdevs.Infra.input.rest;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.josepdevs.Application.GetAllRegisteredUseCase;
import com.josepdevs.Domain.dto.AuthenticationData;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.AllArgsConstructor;

@RequestMapping("api/v1/admin")
@RestController
@AllArgsConstructor
@Hidden //this avoids OpenAPI /Swagger to map this controller
public class GetAllRegistered {
	
	private final GetAllRegisteredUseCase useCase;
	
	@GetMapping("/getall")
	public ResponseEntity<List<AuthenticationData>> getAllRegistered (@RequestHeader("Authorization") String jwtToken){
		//we arrive here after all the filters have passed correctly
		//"Bearer " are 7 digits, with this we get in a string the token value and replace white spaces, just in case
		jwtToken = jwtToken.substring(7).replace (" ","");

		//if we are calling this is because it got over my filters, i do not need to pollute with checks here
		return ResponseEntity.ok(useCase.getAll(jwtToken));

	}
	
}

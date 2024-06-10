package com.josepdevs.Infra.input.rest;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.josepdevs.Application.GetAllRegisteredUseCase;
import com.josepdevs.Domain.Exceptions.RequestNotPermittedException;
import com.josepdevs.Domain.entities.AuthenticationData;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import lombok.AllArgsConstructor;

@RequestMapping("api/v1/admin")
@RestController
@AllArgsConstructor
public class GetAllRegisteredController {
	
	private final GetAllRegisteredUseCase useCase;
	private final Logger logger = LoggerFactory.getLogger(GetAllRegisteredController.class);
	
	@GetMapping("/getall")
	@Bulkhead(name="getAllBH",fallbackMethod="getFallbackRegisteredBulkHead")
	public ResponseEntity<List<AuthenticationData>> getAllRegistered (@RequestHeader("Authorization") String jwtToken){
		//we arrive here after all the filters have passed correctly
		//"Bearer " are 7 digits, with this we get in a string the token value and replace white spaces, just in case
		jwtToken = jwtToken.substring(7).replace (" ","");
		logger.info("token extracted, from endpoint /getall");
		//if we are calling this is because it got over my filters, i do not need to pollute with checks here
		return ResponseEntity.ok(useCase.getAll(jwtToken));

	}
	
	public ResponseEntity<String> getFallbackRegisteredBulkHead (RequestNotPermittedException exception){
        logger.info("Bulkhead has been applied, no further calls are getting accepted");

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Too many requests : No further request will be accepted. Please try again in some minutes ");

	}
	
}

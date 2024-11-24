package com.josepedevs.infra.input.rest.authenticationdata;


import com.josepedevs.application.usecase.authenticationdata.GetAllAuthenticationDataUseCaseImpl;
import com.josepedevs.domain.entity.AuthenticationData;
import com.josepedevs.domain.exceptions.RequestNotPermittedException;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("api/v1/admin")
@RestController
@AllArgsConstructor
public class RestGetAllRegisteredController {
	
	private final GetAllAuthenticationDataUseCaseImpl useCase;
	private final Logger logger = LoggerFactory.getLogger(RestGetAllRegisteredController.class);
	
	@GetMapping("/getall")
	@Bulkhead(name="getAllBH",fallbackMethod="getFallbackRegisteredBulkHead")
	public ResponseEntity<List<AuthenticationData>> getAllRegistered (@RequestHeader("Authorization") String jwtToken){
		jwtToken = jwtToken.substring(7).replace (" ","");
		return ResponseEntity.ok(useCase.apply(jwtToken));
	}
	
	public ResponseEntity<String> getFallbackRegisteredBulkHead (RequestNotPermittedException exception){
        logger.info("Bulkhead has been applied, no further calls are getting accepted");

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Too many requests : No further request will be accepted. Please try again in some minutes ");

	}
	
}
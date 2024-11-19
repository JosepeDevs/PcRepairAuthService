package com.josepedevs.infra.input.rest.user;


import com.josepedevs.application.usecase.user.GetAllusersUseCaseImpl;
import com.josepedevs.domain.exceptions.RequestNotPermittedException;
import com.josepedevs.infra.output.persistence.jpa.postgresql.authenticationdata.entity.AuthenticationDataEntity;
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
public class GetAllRegisteredController {
	
	private final GetAllusersUseCaseImpl useCase;
	private final Logger logger = LoggerFactory.getLogger(GetAllRegisteredController.class);
	
	@GetMapping("/getall")
	@Bulkhead(name="getAllBH",fallbackMethod="getFallbackRegisteredBulkHead")
	public ResponseEntity<List<AuthenticationDataEntity>> getAllRegistered (@RequestHeader("Authorization") String jwtToken){
		//we arrive here after all the filters have passed correctly
		//"Bearer " are 7 digits, with this we get in a string the token value and replace white spaces, just in case
		jwtToken = jwtToken.substring(7).replace (" ","");
		logger.info("/getall --> token extracted from Bearer token.");
		//if we are calling this is because it got over my filters, i do not need to pollute with checks here
		return ResponseEntity.ok(useCase.apply(jwtToken));

	}
	
	public ResponseEntity<String> getFallbackRegisteredBulkHead (RequestNotPermittedException exception){
        logger.info("Bulkhead has been applied, no further calls are getting accepted");

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Too many requests : No further request will be accepted. Please try again in some minutes ");

	}
	
}
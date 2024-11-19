package com.josepedevs.infra.input.rest.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.josepedevs.application.usecase.user.InvalidateUserTokenUseCaseImpl;

import lombok.AllArgsConstructor;

@RequestMapping("api/v1/admin")
@RestController
@AllArgsConstructor
public class InvalidateTokenController {
	 
		private final InvalidateUserTokenUseCaseImpl invalidateUseCase;
		private final Logger logger = LoggerFactory.getLogger(InvalidateTokenController.class);

		@PatchMapping("/invalidate")
		
		public ResponseEntity<String> invalidateToken(@RequestHeader("Authorization") String jwtToken){
			//"Bearer " are 7 digits, with this we get in a string the token value and replace white spaces, just in case
			jwtToken = jwtToken.substring(7).replace (" ","");

			boolean wasInvalidated = invalidateUseCase.apply(jwtToken);
			logger.info("Success invalidating token: "+ wasInvalidated);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");

		}
}

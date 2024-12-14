package com.josepedevs.infra.input.rest.authenticationdata;

import com.josepedevs.application.usecase.authenticationdata.InvalidateAuthenticationDataTokenUseCaseImpl;
import com.josepedevs.domain.request.InvalidateTokenRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("api/v1/admin")
@RestController
@AllArgsConstructor
@Slf4j
public class RestInvalidateTokenController {
	 
		private final InvalidateAuthenticationDataTokenUseCaseImpl invalidateUseCase;

		@PatchMapping("/invalidate")
		public ResponseEntity<String> invalidateToken(@RequestHeader("Authorization") String jwtToken, @RequestParam UUID authDataId){
			//"Bearer " are 7 digits, with this we get in a string the token value and replace white spaces, just in case
			jwtToken = jwtToken.substring(7).trim();

			boolean wasInvalidated = invalidateUseCase.apply(InvalidateTokenRequest.builder().jwtToken(jwtToken).authDataId(authDataId).build());
			log.info("Success invalidating token: {}", wasInvalidated);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
		}
}

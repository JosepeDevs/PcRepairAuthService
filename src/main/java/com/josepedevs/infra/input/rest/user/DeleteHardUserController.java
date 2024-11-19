package com.josepedevs.infra.input.rest.user;

import com.josepedevs.application.usecase.user.DeleteHardUseCaseImpl;
import com.josepedevs.infra.input.rest.user.mapper.RestAuthenticationDataMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
public class DeleteHardUserController {

		private final DeleteHardUseCaseImpl deleteUseCase;
		private final Logger logger = LoggerFactory.getLogger(DeleteHardUserController.class);
		private final RestAuthenticationDataMapper mapper;

		@DeleteMapping("/deletehard")
		public ResponseEntity<Boolean> newpassword (@RequestHeader("Authorization") String jwtToken, @RequestParam String  userId){
			//"Bearer " are 7 digits, with this we get in a string the token value and replace white spaces, just in case
			jwtToken = jwtToken.substring(7).replace (" ","");

			boolean wasCorrectlyDeleted = deleteUseCase.apply(
					mapper.map(jwtToken, UUID.fromString(userId)));

			if(	wasCorrectlyDeleted ) {
				logger.info("User login data was deleted.");
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(true);
			} else {
				logger.info("The user was not deleted.");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
			}

		}
		
		
}

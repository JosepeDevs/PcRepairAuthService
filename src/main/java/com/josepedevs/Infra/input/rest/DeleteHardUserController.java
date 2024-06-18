package com.josepedevs.Infra.input.rest;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.josepedevs.Application.DeleteHardUseCase;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
public class DeleteHardUserController {

		private final DeleteHardUseCase deleteUseCase;
		private final Logger logger = LoggerFactory.getLogger(DeleteHardUserController.class);

		//solo necesito la contraseña nueva porque también necesitaremos que venga con un token para darlo por válido
		@DeleteMapping("/deletehard")
		public ResponseEntity<Boolean> newpassword (@RequestHeader("Authorization") String jwtToken, @RequestParam String  userId){
			//we arrive here after all the filters have passed correctly
			//"Bearer " are 7 digits, with this we get in a string the token value and replace white spaces, just in case
			jwtToken = jwtToken.substring(7).replace (" ","");

			boolean wasCorrectlyDeleted = deleteUseCase.deleteHard(jwtToken, UUID.fromString(userId));
			if(	wasCorrectlyDeleted ) {
				logger.info("User login data was deleted.");
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(true);
			} else {
				logger.info("The user was not deleted.");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
			}

		}
		
		
}

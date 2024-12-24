package com.josepedevs.infra.input.rest.authenticationdata;

import com.josepedevs.application.usecase.authenticationdata.DeleteHardAuthenticationDataUseCaseImpl;
import com.josepedevs.infra.input.rest.authenticationdata.mapper.RestAuthenticationDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
@Slf4j
public class RestDeleteHardUserController {

	private final DeleteHardAuthenticationDataUseCaseImpl deleteUseCase;
	private final RestAuthenticationDataMapper mapper;

	@DeleteMapping("/deletehard")
	public ResponseEntity<Boolean> newPassword(@RequestHeader("Authorization") String jwtToken, @RequestParam String  userId){
		//"Bearer " are 7 digits, with this we get in a string the token value and replace white spaces, just in case
		jwtToken = jwtToken.substring(7).replace (" ","");

		final var wasCorrectlyDeleted = deleteUseCase.apply(
				mapper.map(jwtToken, UUID.fromString(userId)));

		if(	wasCorrectlyDeleted ) {
			log.info("User login data was deleted.");
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(true);
		}
		log.info("The user was not deleted.");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
	}
}
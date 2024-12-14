package com.josepedevs.infra.input.rest.authenticationdata;

import com.josepedevs.application.usecase.authenticationdata.PatchAuthenticationDataPasswordUseCaseImpl;
import com.josepedevs.infra.input.rest.authenticationdata.mapper.RestAuthenticationDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("api/v1/")
public class RestUpdatePasswordController {

		private final PatchAuthenticationDataPasswordUseCaseImpl patchAuthenticationDataPasswordUseCaseImplUseCase;
		private final RestAuthenticationDataMapper mapper;

		@PatchMapping("/newpassword")
		public ResponseEntity<Boolean> newpassword (@RequestHeader("Authorization") String jwtToken,
													@RequestParam UUID authDataId,
													@RequestParam String newpsswrd){
			//"Bearer " are 7 digits, with this we get in a string the token value and replace white spaces, just in case
			jwtToken = jwtToken.substring(7).trim();

			boolean psswrdChanged = patchAuthenticationDataPasswordUseCaseImplUseCase.apply(
							mapper.map(jwtToken, authDataId, newpsswrd)
			);

			if(	psswrdChanged ) {
				log.info("Password updated correctly.");
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(true);
			}
			log.info("Password was not updated.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
		}
}
package com.josepedevs.infra.input.rest.authenticationdata;

import com.josepedevs.application.usecase.authenticationdata.PatchAuthenticationDataPasswordUseCaseImpl;
import com.josepedevs.infra.input.rest.authenticationdata.mapper.RestAuthenticationDataMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RestUpdatePasswordController {

		private final PatchAuthenticationDataPasswordUseCaseImpl patchAuthenticationDataPasswordUseCaseImplUseCase;
		private final RestAuthenticationDataMapper mapper;
		private final Logger logger = LoggerFactory.getLogger(RestUpdatePasswordController.class);

		//solo necesito la contraseña nueva porque también necesitaremos que venga con un token para darlo por válido
		@PatchMapping("/newpassword")
		public ResponseEntity<Boolean> newpassword (@RequestHeader("Authorization") String jwtToken, @RequestParam String  newpsswrd){
			//we arrive here after all the filters have passed correctly
			//"Bearer " are 7 digits, with this we get in a string the token value and replace white spaces, just in case
			jwtToken = jwtToken.substring(7).replace (" ","");

			boolean psswrdNotChanged = patchAuthenticationDataPasswordUseCaseImplUseCase.apply(
					mapper.map(jwtToken, newpsswrd)
			);

			if(	psswrdNotChanged ) {
				logger.info("Password was not updated.");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
			} else {
				logger.info("Password updated correctly.");
				return ResponseEntity.status(HttpStatus.NO_CONTENT).body(true);
			}

		}
		
		
}

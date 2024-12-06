package com.josepedevs.infra.input.rest.authenticationdata;

import com.josepedevs.application.usecase.authenticationdata.PatchAuthenticationDataRoleUseCaseImpl;
import com.josepedevs.domain.request.UpdateRoleRequest;
import com.josepedevs.infra.input.rest.authenticationdata.mapper.RestAuthenticationDataMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/v1/admin")
@RestController
@AllArgsConstructor
public class RestUpdateRoleController {

	private final PatchAuthenticationDataRoleUseCaseImpl patchAuthenticationDataRoleUseCaseImpl;
	private final RestAuthenticationDataMapper mapper;
	private final Logger logger = LoggerFactory.getLogger(RestUpdateRoleController.class);

	//solo necesito la contraseña nueva porque también necesitaremos que venga con un token para darlo por válido
	@PatchMapping("/patchrole")
	public ResponseEntity<Boolean> patchRole (@RequestHeader("Authorization") String jwtToken, @RequestBody UpdateRoleRequest request){

		//we arrive here after all the filters have passed correctly
		//"Bearer " are 7 digits, with this we get in a string the token value and replace white spaces, just in case
		jwtToken = jwtToken.substring(7).replace (" ","");
		boolean roleChanged = patchAuthenticationDataRoleUseCaseImpl.apply(
				mapper.map(jwtToken, request));
		if(	roleChanged ) {
			logger.info("Role updated correctly.");
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(true);
		} else {
			logger.info("Role was not updated.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
		}
	}
	
}


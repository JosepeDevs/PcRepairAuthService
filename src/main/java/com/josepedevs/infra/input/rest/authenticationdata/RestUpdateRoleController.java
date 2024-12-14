package com.josepedevs.infra.input.rest.authenticationdata;

import com.josepedevs.application.usecase.authenticationdata.PatchAuthenticationDataRoleUseCaseImpl;
import com.josepedevs.domain.request.PatchUserRoleRequest;
import com.josepedevs.infra.input.rest.authenticationdata.mapper.RestAuthenticationDataMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("api/v1/admin")
@RestController
@AllArgsConstructor
@Slf4j
public class RestUpdateRoleController {

	private final PatchAuthenticationDataRoleUseCaseImpl patchAuthenticationDataRoleUseCaseImpl;

	//solo necesito la contraseña nueva porque también necesitaremos que venga con un token para darlo por válido
	@PatchMapping("/patchrole")
	public ResponseEntity<Boolean> patchRole (@RequestHeader("Authorization") String jwtToken, @RequestBody PatchUserRoleRequest request){

		//we arrive here after all the filters have passed correctly
		//"Bearer " are 7 digits, with this we get in a string the token value and replace white spaces, just in case
		jwtToken = jwtToken.substring(7).replace (" ","");
		boolean roleChanged = patchAuthenticationDataRoleUseCaseImpl.apply(request.toBuilder().jwtToken(jwtToken).build());
		if(	roleChanged ) {
			log.info("Role updated correctly.");
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(true);
		} else {
			log.info("Role was not updated.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
		}
	}
	
}


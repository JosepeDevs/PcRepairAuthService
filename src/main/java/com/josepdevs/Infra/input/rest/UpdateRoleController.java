package com.josepdevs.Infra.input.rest;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.josepdevs.Application.PatchRoleUseCase;
import com.josepdevs.Infra.output.postgresql.UserPostgreSqlAdapter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@RequestMapping("api/v1/admin")
@RestController
@AllArgsConstructor
public class UpdateRoleController {

	private final PatchRoleUseCase patchRoleUseCase;
	private final Logger logger = LoggerFactory.getLogger(UpdateRoleController.class);

	//solo necesito la contraseña nueva porque también necesitaremos que venga con un token para darlo por válido
	@PatchMapping("/patchrole")
	public ResponseEntity<Boolean> patchRole (@RequestHeader("Authorization") String jwtToken, @RequestBody UpdateRoleRequest request){
		//we arrive here after all the filters have passed correctly
		//"Bearer " are 7 digits, with this we get in a string the token value and replace white spaces, just in case
		jwtToken = jwtToken.substring(7).replace (" ","");

		boolean roleChanged = patchRoleUseCase.patchRole(jwtToken, UUID.fromString(request.getId()), request.getRole());
		if(	roleChanged ) {
			logger.info("Role updated correctly.");
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(true);
		} else {
			logger.info("Role was not updated.");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
		}
	}
	
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class UpdateRoleRequest {
	
	String id;
	String role;
	
}

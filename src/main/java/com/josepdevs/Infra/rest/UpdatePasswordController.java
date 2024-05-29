package com.josepdevs.Infra.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.josepdevs.Application.PatchPassword;
import com.josepdevs.Domain.dto.AuthenticationResponse;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Hidden //this avoids OpenAPI /Swagger to map this controller
public class UpdatePasswordController {

		/*private final PatchPassword patchPasswordUseCase;
		
		//solo necesito la contraseña nueva porque también necesitaremos que venga con un token para darlo por válido
		@PostMapping("/newpassword")
		public ResponseEntity<AuthenticationResponse> newpassword (@RequestHeader @RequestParam String newpassword){
			return ResponseEntity.ok(patchPasswordUseCase.patchPassword(newpassword));

		}*/
		
		
}

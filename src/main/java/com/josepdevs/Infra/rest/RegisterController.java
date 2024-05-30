package com.josepdevs.Infra.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.josepdevs.Application.Register;
import com.josepdevs.Domain.dto.AuthenticationResponse;
import com.josepdevs.Domain.dto.RegisterRequest;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/noauth")
@RequiredArgsConstructor
@Hidden //this avoids OpenAPI /Swagger to map this controller
public class RegisterController {
	
	private final Register registerUseCase;
	
	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> register (@RequestBody RegisterRequest request){
		return ResponseEntity.ok(registerUseCase.register(request));
	}
	
}

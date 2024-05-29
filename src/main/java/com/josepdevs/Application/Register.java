package com.josepdevs.Application;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.josepdevs.Domain.dto.AuthenticationData;
import com.josepdevs.Domain.dto.AuthenticationResponse;
import com.josepdevs.Domain.dto.RegisterRequest;
import com.josepdevs.Domain.dto.valueobjects.Role;
import com.josepdevs.Domain.service.JwtTokenReaderAndIssuerService;
import com.josepdevs.Infra.output.AuthJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Register {

	private final AuthJpaRepository repository;
	
	private final PasswordEncoder passwordEncoder;

	private final JwtTokenReaderAndIssuerService jwtService;
	
	public AuthenticationResponse register(RegisterRequest request) {
		AuthenticationData userAuthData = AuthenticationData.builder()
													.email(request.getEmail())
													.username(request.getUsername())
													.psswrd(passwordEncoder.encode(request.getPsswrd()))
													.role(Role.USER)
													.build();
		repository.save(userAuthData);
		var jwtToken = jwtService.generateToken(userAuthData);
		return AuthenticationResponse.builder()
				.token(jwtToken)
				.build();
	}
	
	
}

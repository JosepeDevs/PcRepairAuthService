package com.josepdevs.Application;

import java.util.HashMap;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.josepdevs.Domain.Exceptions.UserAlreadyExistsException;
import com.josepdevs.Domain.dto.AuthenticationData;
import com.josepdevs.Domain.dto.AuthenticationResponse;
import com.josepdevs.Domain.dto.RegisterRequest;
import com.josepdevs.Domain.dto.valueobjects.Role;
import com.josepdevs.Domain.service.JwtTokenIssuerService;
import com.josepdevs.Infra.output.AuthJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Register {

	private final AuthJpaRepository repository;
	
	private final PasswordEncoder passwordEncoder;

	private final JwtTokenIssuerService jwtService;
	
	public AuthenticationResponse register(RegisterRequest request) {
	
		String username = request.getUsername();
		String email = request.getEmail();
		
		if(repository.findByUsername(username).isPresent()) {
			throw new UserAlreadyExistsException("That username is not available.", username);
		} else if(repository.findByEmail(email).isPresent()) {
			throw new UserAlreadyExistsException("That email is not available.", email);
		} else {
			AuthenticationData userAuthData = AuthenticationData.builder()
					.email(request.getEmail())
					.username(username)
					.psswrd(passwordEncoder.encode(request.getPsswrd()))
					.role(Role.USER)
					.build();
			repository.save(userAuthData);
			var jwtToken = jwtService.generateBasicToken(userAuthData);
			return AuthenticationResponse.builder()
					.token(jwtToken)
					.build();
		}
	}
	
	
}

package com.josepdevs.Application;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.josepdevs.Domain.Exceptions.UserAlreadyExistsException;
import com.josepdevs.Domain.config.JwtValidationFilter;
import com.josepdevs.Domain.dto.AuthenticationResponse;
import com.josepdevs.Domain.dto.RegisterRequest;
import com.josepdevs.Domain.dto.valueobjects.Role;
import com.josepdevs.Domain.entities.AuthenticationData;
import com.josepdevs.Domain.service.JwtTokenIssuerService;
import com.josepdevs.Infra.output.AuthJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Register {

	private final AuthJpaRepository repository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenIssuerService jwtService;
	private final Logger logger = LoggerFactory.getLogger(Register.class);

	public AuthenticationResponse register(RegisterRequest request) {
	
		String username = request.getUsername();
		String email = request.getEmail();
		
		if(repository.findByUsername(username).isPresent()) {
			logger.error("the username was already in use.");
			throw new UserAlreadyExistsException("That username is not available/allowed.", "username");
		} else if(repository.findByEmail(email).isPresent()) {
			logger.error("email was already in use.");
			throw new UserAlreadyExistsException("That email is not available/allowed.", "email");
		} else {
			AuthenticationData userAuthData = AuthenticationData.builder()
					.email(request.getEmail())
					.username(username)
					.psswrd(passwordEncoder.encode(request.getPsswrd()))
					.role("USER")
					.build();
			var jwtToken = jwtService.generateBasicToken(userAuthData);
			userAuthData.setCurrentToken(jwtToken);
			repository.save(userAuthData);
			logger.trace("returning generated token.");
			return AuthenticationResponse.builder()
					.token(jwtToken)
					.build();
		}
	}
	
	
}

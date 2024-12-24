package com.josepedevs.application.usecase.authenticationdata;

import com.josepedevs.application.service.JwtIssuerService;
import com.josepedevs.domain.entity.AuthenticationData;
import com.josepedevs.domain.exceptions.UserAlreadyExistsException;
import com.josepedevs.domain.repository.AuthenticationDataRepository;
import com.josepedevs.domain.request.RegisterRequest;
import com.josepedevs.domain.usecase.RegisterAuthenticationDataUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegisterAuthenticationDataUseCaseImpl implements RegisterAuthenticationDataUseCase {

	private final AuthenticationDataRepository repository;
	private final PasswordEncoder passwordEncoder;
	private final JwtIssuerService jwtService;

	@Override
	public UUID apply(RegisterRequest request) {

		final var username = request.getUsername();
		final var email = request.getEmail();
		
		if(repository.findByUsername(username).isPresent()) {
			log.error("the username was already in use.");
			throw new UserAlreadyExistsException("That username is not available/allowed.");
		} else if(repository.findByEmail(email).isPresent()) {
			log.error("email was already in use.");
			throw new UserAlreadyExistsException("That email is not available/allowed.");
		}
		final var userAuthData = AuthenticationData.builder()
				.email(request.getEmail())
				.username(username)
				.psswrd(passwordEncoder.encode(request.getPsswrd()))
				.role("USER")
				.active(true)
				.build();
		var jwtToken = jwtService.generateBasicToken(userAuthData);
		userAuthData.setCurrentToken(jwtToken);
		final var user = repository.registerUserAuthData(userAuthData, jwtToken);
		log.trace("returning generated UUID.");

		return user.getIdUser();
	}

	
	
}

package com.josepedevs.application.usecase.authenticationdata;

import com.josepedevs.application.service.JwtTokenValidations;
import com.josepedevs.domain.entity.AuthenticationData;
import com.josepedevs.domain.exceptions.UserNotFoundException;
import com.josepedevs.domain.repository.AuthenticationDataRepository;
import com.josepedevs.domain.usecase.GetAllAuthenticationDataUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetAllAuthenticationDataUseCaseImpl implements GetAllAuthenticationDataUseCase {

	private final AuthenticationDataRepository repository;
	private final JwtTokenValidations jwtValidations;

	public List<AuthenticationData> apply(String jwtToken) {

		log.info("Checking if user in token is an admin: {}.", jwtToken);
		final var isUserAnAdmin = jwtValidations.isAdminTokenCompletelyValidated(jwtToken);

		if(!isUserAnAdmin){
			throw new UserNotFoundException("The user was not found or the token did not contain the required data.");
		}
		return repository.getAll();

	}
}

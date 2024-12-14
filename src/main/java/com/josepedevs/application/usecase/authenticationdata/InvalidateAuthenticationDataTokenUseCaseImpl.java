package com.josepedevs.application.usecase.authenticationdata;

import com.josepedevs.application.service.JwtRoleValidator;
import com.josepedevs.domain.exceptions.UserNotFoundException;
import com.josepedevs.domain.repository.AuthenticationDataRepository;
import com.josepedevs.domain.request.InvalidateTokenRequest;
import com.josepedevs.domain.usecase.InvalidateAuthenticationDataTokenUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class InvalidateAuthenticationDataTokenUseCaseImpl implements InvalidateAuthenticationDataTokenUseCase  {

	private final AuthenticationDataRepository authenticationDataRepository;
	private final JwtRoleValidator jwtRoleValidator;

	public Boolean apply(InvalidateTokenRequest request) {
		jwtRoleValidator.isTokenFromAdmin(request.getJwtToken());

		final var optAuthData = authenticationDataRepository.findById(request.getAuthDataId());
		if(optAuthData.isEmpty()) {
			throw new UserNotFoundException("User not found.");
		}
		final var  success = authenticationDataRepository.invalidateToken(optAuthData.get());
		if(success) {
			log.info("The token was correctly invalidated.");
			return true;
		}
		log.info("The token could not be invalidated.");
		return false;

	}
}

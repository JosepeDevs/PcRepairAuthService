package com.josepedevs.application.usecase.authenticationdata;

import com.josepedevs.application.service.JwtMasterValidator;
import com.josepedevs.domain.exceptions.TokenNotValidException;
import com.josepedevs.domain.repository.AuthenticationDataRepository;
import com.josepedevs.domain.request.DeleteHardUserRequest;
import com.josepedevs.domain.usecase.DeleteHardAuthenticationDataUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteHardAuthenticationDataUseCaseImpl implements DeleteHardAuthenticationDataUseCase {

	private final AuthenticationDataRepository repository;
	private final JwtMasterValidator jwtMasterValidator;

	@Override
	public Boolean apply(DeleteHardUserRequest deleteHardUserRequest) {
		final var isValidToken = jwtMasterValidator.isAdminTokenCompletelyValidated(deleteHardUserRequest.getJwtToken());
		if(!isValidToken) {
			throw new TokenNotValidException("The token is not valid, might be because it expired, did not have the required role or was invalidated.");
		}
		final var wasDeletedCorrectly = repository.deleteHard(deleteHardUserRequest.getUserId());
		if(wasDeletedCorrectly) {
			log.info("User was deleted correctly.");
			return true;
		}
		log.info("User could not be deleted");
		return false;
	}
}

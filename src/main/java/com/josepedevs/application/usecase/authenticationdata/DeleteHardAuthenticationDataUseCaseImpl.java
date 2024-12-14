package com.josepedevs.application.usecase.authenticationdata;

import com.josepedevs.application.service.JwtMasterValidator;
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
		jwtMasterValidator.isAdminTokenCompletelyValidated(deleteHardUserRequest.getJwtToken());
		final var wasDeletedCorrectly = repository.deleteHard(deleteHardUserRequest.getUserId());

		if( wasDeletedCorrectly ) {
			log.info("User was deleted correctly.");
			return true;
		}
		log.info("User could not be deleted");
		return false;
	}
}

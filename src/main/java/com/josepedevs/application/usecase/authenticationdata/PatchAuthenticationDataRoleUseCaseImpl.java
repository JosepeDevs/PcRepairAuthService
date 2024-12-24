package com.josepedevs.application.usecase.authenticationdata;

import com.josepedevs.application.service.AuthDataFinder;
import com.josepedevs.application.service.JwtMasterValidator;
import com.josepedevs.domain.exceptions.TokenNotValidException;
import com.josepedevs.domain.repository.AuthenticationDataRepository;
import com.josepedevs.domain.request.PatchUserRoleRequest;
import com.josepedevs.domain.usecase.PatchAuthenticationDataRoleUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
@Slf4j
public class PatchAuthenticationDataRoleUseCaseImpl implements PatchAuthenticationDataRoleUseCase {

	private final AuthenticationDataRepository repository;
	private final AuthDataFinder authDataFinder;
	private final JwtMasterValidator jwtMasterValidator;

	@Override
	public Boolean apply(PatchUserRoleRequest patchUserRoleRequest) {
		final var isValidToken = jwtMasterValidator.isAdminTokenCompletelyValidated(patchUserRoleRequest.getJwtToken());
		if(!isValidToken) {
			throw new TokenNotValidException("The token is not valid");
		}
		final var newRole = patchUserRoleRequest.getRole();
		final var existingUserToBeChanged = authDataFinder.findById(UUID.fromString(patchUserRoleRequest.getId()));
		existingUserToBeChanged.setRole(newRole);
		final var savedUser = repository.patchRole(existingUserToBeChanged, newRole);
		if(newRole.equals(savedUser.getRole())) {
			log.info("Role updated correctly");
			return true;
		}
		log.info("The role was not updated.");
		return false;
	}

}
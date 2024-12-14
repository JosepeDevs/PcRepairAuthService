package com.josepedevs.application.usecase.authenticationdata;

import com.josepedevs.application.service.AuthDataFinder;
import com.josepedevs.application.service.JwtMasterValidator;
import com.josepedevs.domain.exceptions.TokenNotValidException;
import com.josepedevs.domain.mapper.AuthDataMapper;
import com.josepedevs.domain.repository.AuthenticationDataRepository;
import com.josepedevs.domain.request.PatchUserRoleRequest;
import com.josepedevs.domain.usecase.PatchAuthenticationDataRoleUseCase;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@AllArgsConstructor
@Service
@Slf4j
public class PatchAuthenticationDataRoleUseCaseImpl implements PatchAuthenticationDataRoleUseCase {

	private final AuthenticationDataRepository repository;
	private final AuthDataFinder authDataFinder;
	private final JwtMasterValidator jwtMasterValidator;
	private final AuthDataMapper authDataMapper;

	@Override
	public Boolean apply(PatchUserRoleRequest patchUserRoleRequest) {
		final var isValidToken = jwtMasterValidator.isAdminTokenCompletelyValidated(patchUserRoleRequest.getJwtToken());
		if(!isValidToken) {
			throw new TokenNotValidException("The token is not valid");
		}
		final var  authData = authDataMapper.map(patchUserRoleRequest);
		final var id = authData.getIdUser();
		final var role = authData.getRole();

		final var existingUserToBeChanged = authDataFinder.findById(id);

		final var initialRole = existingUserToBeChanged.getRole();
		existingUserToBeChanged.setRole(role);
		final var savedUser = repository.registerUserAuthData(existingUserToBeChanged, patchUserRoleRequest.getJwtToken());
		if(Objects.equals(initialRole, savedUser.getRole())) {
			log.info("The role was not updated ");
			return false;
		}
		log.info("Role updated correctly");
		return true;
		}

}
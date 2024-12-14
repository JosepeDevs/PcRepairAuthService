package com.josepedevs.application.usecase.authenticationdata;

import com.josepedevs.domain.exceptions.UserNotFoundException;
import com.josepedevs.domain.repository.AuthenticationDataRepository;
import com.josepedevs.domain.request.PatchUserPasswordRequest;
import com.josepedevs.domain.usecase.PatchAuthenticationDataPasswordUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatchAuthenticationDataPasswordUseCaseImpl implements PatchAuthenticationDataPasswordUseCase {

	private final AuthenticationDataRepository authenticationDataRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Boolean apply(PatchUserPasswordRequest patchUserPasswordRequest) {

		final var userDataAuth = authenticationDataRepository.findById(patchUserPasswordRequest.getAuthDataId());
		if(userDataAuth.isEmpty()) {
			throw new UserNotFoundException("User not found.");
		}

		final var  hashedPassword = passwordEncoder.encode(patchUserPasswordRequest.getNewPassword());
		boolean isUserSavedWithChanges = authenticationDataRepository.patchPassword(userDataAuth.get(), hashedPassword);

		if( isUserSavedWithChanges ) {
			log.info("User updated password correctly.");
			return true;
		}
		log.info("User could not update password or no changes were detected");
		return false;
	}
}
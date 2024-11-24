package com.josepedevs.application.usecase.authenticationdata;

import com.josepedevs.application.service.GetUserFromTokenUsernameService;
import com.josepedevs.application.service.JwtTokenDataExtractorService;
import com.josepedevs.domain.repository.AuthenticationDataRepository;
import com.josepedevs.domain.request.PatchUserPasswordRequest;
import com.josepedevs.domain.usecase.PatchAuthenticationDataPasswordUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatchAuthenticationDataPasswordUseCaseImpl implements PatchAuthenticationDataPasswordUseCase {

	private final AuthenticationDataRepository repository;
	private final JwtTokenDataExtractorService jwtReaderService;
	private final PasswordEncoder passwordEncoder;
	private final Logger logger = LoggerFactory.getLogger(PatchAuthenticationDataPasswordUseCaseImpl.class);
	private final GetUserFromTokenUsernameService getUserFromTokenUsernameService;

	@Override
	public Boolean apply(PatchUserPasswordRequest patchUserPasswordRequest) {

		final var  username = jwtReaderService.extractUsername(patchUserPasswordRequest.getJwtToken());
		final var userDataAuth = getUserFromTokenUsernameService.getUserFromTokenUsername(username);

		final var  hashedPassword = passwordEncoder.encode(patchUserPasswordRequest.getNewPassword());
		boolean isUserSavedWithChanges = repository.patchPassword(userDataAuth, hashedPassword);
		if( isUserSavedWithChanges ) {
			logger.info("User updated password correctly.");
			return true;
		} else { 
			logger.info("User could not update password or no changes were detected");
			return false;
		}
	}



}

package com.josepedevs.infra.output.persistence.jpa.postgresql;

import com.josepedevs.domain.entity.AuthenticationData;
import com.josepedevs.domain.exceptions.UserNotFoundException;
import com.josepedevs.domain.repository.AuthenticationDataRepository;
import com.josepedevs.infra.output.persistence.jpa.postgresql.authenticationdata.mapper.JpaAuthenticationDataMapper;
import com.josepedevs.infra.output.persistence.jpa.postgresql.authenticationdata.repository.JpaAuthenticationDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class JpaAuthenticationDataPostgreSqlAdapter implements AuthenticationDataRepository {

    private final JpaAuthenticationDataRepository userJpaRepository;
	private final JpaAuthenticationDataMapper mapper;

	//////////////Commands////////////////

	@Override
    public AuthenticationData registerUserAuthData(AuthenticationData userAuthData, String jwtToken) {
			log.info("registering user.");
			userJpaRepository.save(
					mapper.map(userAuthData));
	        return userAuthData;
	}

	@Override
	public Optional<AuthenticationData> findByUsername(String username) {
		log.info("checking user by username.");
        return 	userJpaRepository.findByUsername(username).map(mapper::map);
	}

	public boolean updateToken(AuthenticationData authData, String jwtToken) {
        final var optUser = userJpaRepository.findById(authData.getIdUser());
		if(optUser.isEmpty()) {
			log.warn("User not found.");
			return false;
		}
		final var authDataToBeSaved = authData.toBuilder().currentToken(jwtToken).build();
		final var savedUser = userJpaRepository.save(mapper.map(authDataToBeSaved));
		final var updatedSuccessfully=  savedUser.getCurrentToken().equals(jwtToken);
        if (updatedSuccessfully) {
			log.info("User logged in correctly.");
        	return true;
        }
		log.warn("Login rejected.");
		return false;
	}

	@Override
	public boolean patchPassword(AuthenticationData authData, String digestedPsswrd) {
		final var savedAuthData = authData.toBuilder().psswrd(digestedPsswrd).build();
		final var savedUser = mapper.map(
													userJpaRepository.save(
															mapper.map(savedAuthData)));
        if( savedUser == authData) {
			log.info("Password did not change.");
			return false;
        }
		log.info("Password updated.");
		return true;
	}

	@Override
	public boolean patchRole(AuthenticationData authData, String role) {
		final var savedAuthData = authData.toBuilder().role(role).build();
        final var savedUser = mapper.map(
												userJpaRepository.save(
														mapper.map(savedAuthData)));
        if( savedUser == authData) {
			log.error("Role was not updated.");
        	return false;
        }
		log.info("Role updated corretly.");
		return true;
	}

	@Override
	public List<AuthenticationData> getAll() {
		log.info("Accessing all authentication data.");
		 return userJpaRepository.findAll()
				 .stream()
				 .map(mapper::map)
				 .toList();
	}

	@Override
	public boolean invalidateToken(AuthenticationData authData) {
		final var newAuthData = authData.toBuilder().currentToken("invalidated").build();
        final var savedUser = mapper.map(
													userJpaRepository.save(
															mapper.map(newAuthData)));

		if(Objects.equals(savedUser.getCurrentToken(), authData.getCurrentToken())) {
			log.error("token was not invalidated.");
        	return false;
        }

		log.info("token invalidated correctly");
		return true;
	}

	@Override
	public boolean isTokenInvalidated(String username) {
		final var user = userJpaRepository.findByUsername(username);
		final var existentUser = user.orElseThrow( () -> new UserNotFoundException("User not found."));
		String token = existentUser.getCurrentToken();
		log.info("Checking value of token to determine if it is invalidated: {}", token);
		return ( token == null || token.equals("invalidated") );
	}
 
	@Override
	public boolean deleteHard(UUID userId) {
		userJpaRepository.deleteById(userId);
		final var userDataAuth = userJpaRepository.findById(userId);
        return userDataAuth.isEmpty();
	}

	@Override
	public Optional<AuthenticationData> findById(UUID id) {
		return userJpaRepository.findById(id)
				.map(mapper::map);
	}

	@Override
	public Optional<AuthenticationData> findByEmail(String email) {
		return userJpaRepository.findByEmail(email).map(mapper::map);
	}

}

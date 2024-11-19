package com.josepedevs.infra.output.persistence.jpa.postgresql;

import com.josepedevs.domain.entity.AuthenticationData;
import com.josepedevs.domain.exceptions.UserNotFoundException;
import com.josepedevs.domain.repository.AuthenticationDataRepository;
import com.josepedevs.infra.output.persistence.jpa.postgresql.authenticationdata.mapper.JpaAuthenticationDataMapper;
import com.josepedevs.infra.output.persistence.jpa.postgresql.authenticationdata.repository.JpaAuthenticationDataRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JpaAuthenticationDataPostgreSqlAdapter implements AuthenticationDataRepository {

    private final JpaAuthenticationDataRepository userJpaRepository;
	private final JpaAuthenticationDataMapper mapper;
	private final Logger logger = LoggerFactory.getLogger(JpaAuthenticationDataPostgreSqlAdapter.class);

    
	//////////////Commands////////////////

	@Override
    public AuthenticationData registerUserAuthData(AuthenticationData userAuthData, String jwtToken) {
			logger.info("registering user.");
			userJpaRepository.save(
					mapper.map(userAuthData));
	        return userAuthData;
	}

	@Override
	public Optional<AuthenticationData> findByUsername(String username) {
		logger.info("checking user by username.");
        return 	userJpaRepository.findByUsername(username).map(mapper::map);
	}

	@Override 
	public boolean login(AuthenticationData authData, String jwtToken) {
		authData.setCurrentToken(jwtToken);
        final var savedUser = userJpaRepository.save(mapper.map(authData));
        if (savedUser.getCurrentToken().equals(jwtToken)) {
    		logger.info("User logged in correctly.");
        	return true;
        } else {
        	logger.info("Login rejected.");
        	return false;
        }  
	}

	@Override
	public boolean patchPassword(AuthenticationData authData, String digestedPsswrd) {
		
		authData.setPsswrd(digestedPsswrd);
		final var savedUser = mapper.map(
				userJpaRepository.save(
						mapper.map(authData)));
        if( savedUser == authData) {
        	logger.info("Passwod updated.");
        	return false;
        } else {
        	logger.info("Password did not change.");
        	return true;
        }  
		
	}

	@Override
	public boolean patchRole(AuthenticationData authData, String role) {
		
		authData.setCurrentToken(role);
        final var savedUser = mapper.map(
				userJpaRepository.save(
						mapper.map(authData)));
        if( savedUser == authData) {
        	logger.error("Role was not updated.");
        	return false;
        } else {
        	logger.info("Role updated corretly.");
        	return true;
        }  
        
	}

	@Override
	public List<AuthenticationData> getAll() {
		logger.info("Accessing all authentication data.");
		 return userJpaRepository.findAll()
				 .stream()
				 .map(mapper::map)
				 .collect(Collectors.toList());
	}

	@Override
	public boolean invalidateToken(AuthenticationData authData) {

		authData.setCurrentToken("invalidated");
		
        final var savedUser = mapper.map(
				userJpaRepository.save(
						mapper.map(authData)));
        if( savedUser == authData) {
        	logger.error("token was not invalidated.");
        	return false;
        } else {
        	logger.info("token invalidated correctly");
        	return true;
        } 
	}

	@Override
	public boolean isTokenInvalidated(String username) {
		final var user = userJpaRepository.findByUsername(username);
		final var existentUser = user.orElseThrow( () -> new UserNotFoundException("User not found.","Username"));
		String token = existentUser.getCurrentToken();
		logger.info("Checking value of token to determine if it is invalidated: "+ token);
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

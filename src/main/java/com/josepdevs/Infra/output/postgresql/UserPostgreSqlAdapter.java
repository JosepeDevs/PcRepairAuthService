package com.josepdevs.Infra.output.postgresql;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.josepdevs.Domain.Exceptions.UserNotFoundException;
import com.josepdevs.Domain.entities.AuthenticationData;
import com.josepdevs.Domain.repository.AuthRepository;
import com.josepdevs.Infra.output.AuthJpaRepository;

@Repository
public class UserPostgreSqlAdapter implements AuthRepository{

    private final AuthJpaRepository userJpaRepository;
	private final Logger logger = LoggerFactory.getLogger(UserPostgreSqlAdapter.class);

	
    public UserPostgreSqlAdapter(AuthJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }
    
	//////////////Commands////////////////

	@Override
    public AuthenticationData registerUserAuthData(AuthenticationData userAuthData, String jwtToken) {
			logger.info("registering user.");
			userJpaRepository.save(userAuthData);

	        //after save it should contain the UUID, in the future this will return bool, and the event will publish the UUID
	        return userAuthData;
	}

	@Override
	public Optional<AuthenticationData> findByUsername(String username) {
		logger.info("checking user by username.");
        return userJpaRepository.findByUsername(username);
	}

	@Override 
	public boolean login(AuthenticationData authData, String jwtToken) {
		String previousToken = authData.getCurrentToken();
		authData.setCurrentToken(jwtToken);
        AuthenticationData savedUser = userJpaRepository.save(authData);
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
		AuthenticationData savedUser = userJpaRepository.save(authData);
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
        AuthenticationData savedUser = userJpaRepository.save(authData);
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
		return userJpaRepository.findAll();
	}

	@Override
	public boolean invalidateToken(AuthenticationData authData) {

		authData.setCurrentToken("invalidated");
		
        AuthenticationData savedUser = userJpaRepository.save(authData);
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
		Optional<AuthenticationData> user = userJpaRepository.findByUsername(username); 
		AuthenticationData existentUser = user.orElseThrow( () -> new UserNotFoundException("User not found.","Username"));
		String token = existentUser.getCurrentToken();
		logger.info("Checking value of token to determine if it is invalidated: "+ token);
		return ( token == null || token.equals("invalidated") );
	}


}

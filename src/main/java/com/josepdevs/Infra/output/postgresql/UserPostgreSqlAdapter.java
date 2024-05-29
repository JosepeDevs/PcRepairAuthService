package com.josepdevs.Infra.output.postgresql;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.josepdevs.Domain.dto.AuthenticationData;
import com.josepdevs.Domain.repository.AuthRepository;
import com.josepdevs.Infra.output.AuthJpaRepository;

@Repository
public class UserPostgreSqlAdapter implements AuthRepository{

    private final AuthJpaRepository userJpaRepository;
	
    public UserPostgreSqlAdapter(AuthJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }
    
	//////////////Commands////////////////

	@Override
    public AuthenticationData registerUserAuthData(AuthenticationData userAuthData) {
	        userJpaRepository.save(userAuthData);
	        //after save it should contain the UUID, in the future this will return bool, and the event will publish the UUID
	        return userAuthData;
	}

	@Override
	public Optional<AuthenticationData> findByUsername(String username) {
        return userJpaRepository.findByUsername(username);
	}

}

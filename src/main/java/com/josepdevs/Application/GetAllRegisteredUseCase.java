package com.josepdevs.Application;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.josepdevs.Domain.Exceptions.InadequateRoleException;
import com.josepdevs.Domain.Exceptions.UserNotFoundException;
import com.josepdevs.Domain.dto.AuthenticationData;
import com.josepdevs.Domain.service.JwtTokenReaderService;
import com.josepdevs.Infra.output.AuthJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetAllRegisteredUseCase {

	private final AuthJpaRepository repository;
	private final JwtTokenReaderService jwtReaderService;

	public List<AuthenticationData> getAll(String jwtToken) {
		
		String username = jwtReaderService.extractUsername(jwtToken);
		Optional<AuthenticationData> userDataAuth = repository.findByUsername(username); 
		AuthenticationData existingUser = userDataAuth.orElseThrow( () ->
		new UserNotFoundException("ha intentado cambiuar la contraseña de un usuario que no existe o el token con las credenciales no lo contenia.", username) );	
		if(existingUser.getRole().toString().equals("ADMIN")){
			return repository.findAll();
		} else {
			throw new InadequateRoleException("You do not have the required authority to access this resource.", existingUser.getRole().toString());
		}
	}
}

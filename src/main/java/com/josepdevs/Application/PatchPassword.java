package com.josepdevs.Application;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.josepdevs.Domain.Exceptions.UserNotFoundException;
import com.josepdevs.Domain.entities.AuthenticationData;
import com.josepdevs.Domain.service.JwtTokenReaderService;
import com.josepdevs.Infra.output.AuthJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatchPassword {

	private final AuthJpaRepository repository;
	private final JwtTokenReaderService jwtReaderService;
	private final PasswordEncoder passwordEncoder;

	public boolean patchPassword(String jwtToken, String newpassword) {
		//here is being throw the error
		String username = jwtReaderService.extractUsername(jwtToken);
		Optional<AuthenticationData> userDataAuth = repository.findByUsername(username); 
		AuthenticationData existingUser = userDataAuth.orElseThrow( () ->
		new UserNotFoundException("ha intentado cambiuar la contraseña de un usuario que no existe o el token con las credenciales no lo contenia.", "username") );	
		existingUser.setPsswrd(passwordEncoder.encode(newpassword));
		
		AuthenticationData savedUser = repository.save(existingUser);
		if(savedUser == existingUser) {
			return false;
		} else {
			return true;
		}
	}
	

}

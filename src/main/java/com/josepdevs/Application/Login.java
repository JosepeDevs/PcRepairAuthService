package com.josepdevs.Application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.josepdevs.Domain.dto.AuthenticationRequest;
import com.josepdevs.Domain.dto.AuthenticationResponse;
import com.josepdevs.Domain.dto.valueobjects.Role;
import com.josepdevs.Domain.service.JwtTokenIssuerService;
import com.josepdevs.Infra.output.AuthJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Login {

	private final AuthenticationManager authenticationManager;
	private final AuthJpaRepository repository;
	private final JwtTokenIssuerService jwtService;


	public AuthenticationResponse login(AuthenticationRequest request) {
	    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPsswrd()));
	    var userDataAuth = repository.findByUsername(request.getUsername()).orElseThrow(); // throws NoSuchElementException
	    
	    // domain logic forces one role per user then it would be this:
	    Role roles = userDataAuth.getRole();
	    String permissions = roles.getPermissions();
	    Map<String, Object> extraClaims = new HashMap<>();
	    //By planning ahead which endopoints are allowed for each role I can avoid having to implement permissions, thank you OAS
	    //extraClaims.put("permissions", permissions);
	    extraClaims.put("authorities", userDataAuth.getAuthorities()); 
	    
	    
	    //if multiple roles can be added to a user
	    /*
	    List<Role> roles = userDataAuth.getRole(); 
    	Map<String,Object> extraClaims = new HashMap<>();
    	StringBuilder rolesSB = new StringBuilder();
    	StringBuilder permissionsSB = new StringBuilder();
    	//builds something like "User, Editor," includes a comma always at the end too
    	for (Role role : roles){
    		rolesSB.append(role.toString());
    		rolesSB.append(", ");
    		permissionsSB.append(role.getPermissions());
    		permissionsSB.append(", ");
    	};
    	//remove the comma in the last position
    	int lastRoleIndex = rolesSB.length() - 1;
    	if (lastRoleIndex >= 0) {
    		rolesSB.deleteCharAt(lastRoleIndex);
    	}
    	//remove the comma in the last position
    	int lastPermissionIndex = permissionsSB.length() - 1;
    	if (lastPermissionIndex >= 0) {
    		permissionsSB.deleteCharAt(lastPermissionIndex);
    	}
    	extraClaims.put("Roles", rolesSB.toString());
    	extraClaims.put("Permissions", permissionsSB.toString());
	    */
	    var jwtToken = jwtService.generateToken(extraClaims, userDataAuth);
	    return AuthenticationResponse.builder()
	           .token(jwtToken)
	           .build();
	}
}

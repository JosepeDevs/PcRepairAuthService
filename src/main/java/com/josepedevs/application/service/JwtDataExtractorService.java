package com.josepedevs.application.service;

import com.josepedevs.domain.entity.valueobjects.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@AllArgsConstructor
@Slf4j
public class JwtDataExtractorService {
	
	private final JwtIssuerService jwtIssuerService;

	/**
	 * Mount the parses with the secret key , the parser then will extract all claims from JWT
	 * @param jwtToken
	 * @return Claims (all the payLoad/Body)
	 */
	public Claims extractAllClaims(String jwtToken) {
	    SecretKey secretSigningKey = jwtIssuerService.getSecretSigningKey();
		final var  jwsParser = Jwts .parser().verifyWith(secretSigningKey).build();
		final var  jwsClaims = jwsParser.parseSignedClaims(jwtToken);
	    log.trace("Extracting all claims from token.");
		return jwsClaims.getPayload();
	}

	/**
	 * //Function from java.util returns type T and take Claims type as parameter and returns type T
	 * @param <T>
	 * @param jwtToken
	 * @param claimsTypeResolver
	 * @return the claim in the correspodent type
	 */
	public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsTypeResolver) {
		final var  allClaims = extractAllClaims(jwtToken);
	    log.trace("Extracting specific type of claim from token");
		return claimsTypeResolver.apply(allClaims);
	}

	/**
	 * Given a token extracts all claims, then extracts claim of type equal to getSubject (String)
	 * @param jwtToken
	 * @return
	 */
	public String extractUsername(String jwtToken) {
	    log.trace("Extracting subject");
		return extractClaim(jwtToken, Claims::getSubject);
	}
	
	/**
	 * Given a token extracts all roles as a List of Role
	 * @param jwtToken
	 * @return
	 */
	public  Map<String,Object> extractRolesAndPermissions(String jwtToken) {
		final var allClaims = extractAllClaims(jwtToken);
        List<?> rolesRaw = (List<?>) allClaims.get("roles");

        // Check if the retrieved list is not null and contains only Role instances
        if (rolesRaw == null || ! rolesRaw.stream().allMatch(obj -> obj instanceof Role)) {
        	// if null or if not all objects in list are Roles return empty map (no extra claims will be added)
    	    log.error("Not all claims of Roles were of type roles");
        	return new HashMap<>();
        }
		// Perform the unchecked cast safely knowing all elements are Role instances
		final var  roles = (List<Role>) rolesRaw;
		Map<String,Object> extraClaims = new HashMap<>();
		final var  rolesSB = new StringBuilder();
		final var  permissionsSB = new StringBuilder();
		//builds something like "User, Editor," includes a comma always at the end too
		for (Role role : roles){
			rolesSB.append(role.toString());
			rolesSB.append(", ");
			permissionsSB.append(role.getPermissions());
			permissionsSB.append(", ");
		}
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
		log.trace("The user was assigned the following roles: {} ", rolesSB);
		log.trace("The user was assigned the following permissions: {}", permissionsSB);
		extraClaims.put("Roles", rolesSB.toString());
		extraClaims.put("Permissions", permissionsSB.toString());
		return extraClaims;

    }
	
	/**
	 * Given a token, extract expiration, the type handler will make it to a Date type
	 * @param jwtToken
	 * @return Claims:Expiration
	 */
	protected Date extractExpiration(String jwtToken) {
	    log.trace("Checking if token was expired");
		return extractClaim(jwtToken, Claims::getExpiration);
	}

}
package com.josepdevs.Domain.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
@ConfigurationProperties(prefix = "myconfig.security.jwt")
public class JwtTokenReaderAndIssuerService {
	
	//this make it take the value from the application.yml
	@Value("${myconfig.security.jwt.SECRET_KEY}")
	private  String SECRET_KEY;
	
	@Value("${myconfig.security.jwt.expirationMinutes}")
	private int expirationMinutes;
	
	private SecretKey getSecretSigningKey() {
		//Base64 is a binary-to-text used FOR TRANSPORT
		byte[] keyByte = Decoders.BASE64.decode(SECRET_KEY);
		//Hash-based Message Authentication Code, later used to verify the sender
		return Keys.hmacShaKeyFor(keyByte);
	}
	
	/**
	 * parse a JSON Web Token (JWT) and extract all its claims
	 * @param jwtToken
	 * @return Claims (all the payLoad/Body)
	 */
	private Claims extractAllClaims(String jwtToken) {
	    SecretKey secretSigningKey = getSecretSigningKey();

	    //montamos el parser, añadiendo la secret key
	    JwtParser jwsParser = Jwts .parser().verifyWith(secretSigningKey).build();
	    // el parser, ya con la key, extraerá los Claims del JWT
	    Jws<Claims> jwsClaims = jwsParser.parseSignedClaims(jwtToken);
	    //extraemos de los claims el payload
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
		final Claims allClaims = extractAllClaims(jwtToken);
		return claimsTypeResolver.apply(allClaims);
	}
	
	
	/**
	 * Given a token extracts all claims, then extracts claim of type equal to getSubject (String)
	 * @param jwtToken
	 * @return
	 */
	public String extractUsername(String jwtToken) {
		return extractClaim(jwtToken, Claims::getSubject);
	}
	
	/**
	 * 
	 * @param extraClaims (for example authorities)
	 * @param userDetails 
	 * @return String token value with claims included
	 */
	public String generateToken ( Map<String,Object> extraClaims, UserDetails userDetails) {
		return Jwts
				.builder()
				.claims(extraClaims)
				.subject(userDetails.getUsername())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date( System.currentTimeMillis() + 1 * 1000 * 60 * expirationMinutes) ) //9 minutes expiration
				.signWith(getSecretSigningKey(), Jwts.SIG.HS256)
				.compact();
	}

	/**
	 *todo allow extraclaims
	 * create token without any extraclaims
	 * @param userDetails
	 * @return String with generated token (no extraclaims, only user details)
	 */
	public String generateToken(UserDetails userDetails) {
		return generateToken( new HashMap<>() , userDetails );
	}

	/**
	 * Given a token, extract expiration, the type hander will make it to a Date type
	 * @param jwtToken
	 * @return
	 */
	private Date extractExpiration(String jwtToken) {
		return extractClaim(jwtToken, Claims::getExpiration);
	}
	
	/**
	 * Gets the expiration of the token and checks current time to return true if is still valid
	 * @param jwtToken
	 * @return boolean : true if is valid, false if not.
	 */
	private boolean isTokenExpired(String jwtToken) {
		return extractExpiration(jwtToken).before(new Date());
	}
	
	
	/**
	 * check that user details in token is equal to userDetails and that the token is not expired
	 * @param jwtToken
	 * @param userdetails
	 * @return
	 */
	public boolean isTokenUserDetailsValid(String jwtToken, UserDetails userDetails) {
		final String username = extractUsername(jwtToken);
		return (username.equals(userDetails.getUsername()) &&  ! isTokenExpired(jwtToken) );
	}

	

}

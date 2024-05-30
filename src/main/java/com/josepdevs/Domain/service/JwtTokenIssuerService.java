package com.josepdevs.Domain.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
@ConfigurationProperties(prefix = "myconfig.security.jwt")
public class JwtTokenIssuerService {
	
	//this make it take the value from the application.yml
	@Value("${myconfig.security.jwt.SECRET_KEY}")
	private  String SECRET_KEY;
	
	@Value("${myconfig.security.jwt.expirationMinutes}")
	private int expirationMinutes;
	
	private JwtTokenReaderService tokenReaderService;
	
	//not private but package limited to be calleable by jwtToken Reader service
	SecretKey getSecretSigningKey() {
		//Base64 is a binary-to-text used FOR TRANSPORT
		byte[] keyByte = Decoders.BASE64.decode(SECRET_KEY);
		//Hash-based Message Authentication Code, later used to verify the sender
		return Keys.hmacShaKeyFor(keyByte);
	}
	
	/**
	 * 
	 * @param extraClaims (for example authorities)
	 * @param userDetails 
	 * @return String token value with claims included
	 */
	public String generateToken( Map<String,Object> extraClaims, UserDetails userDetails) {
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
	public String generateBasicToken(UserDetails userDetails) {
		return generateToken( new HashMap<>(), userDetails );
	}


	

}

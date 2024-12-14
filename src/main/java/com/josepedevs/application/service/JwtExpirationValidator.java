package com.josepedevs.application.service;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@AllArgsConstructor
public class JwtExpirationValidator {

    private final JwtDataExtractorService jwtDataExtractorService;

    /**
     * Validates that token is neither expired nor invalidated
     * @param jwtToken bearer token string
     * @return boolean : true if is valid, false if not.
     */
    public boolean isTokenExpired(String jwtToken) {
        if(jwtDataExtractorService.extractExpiration(jwtToken).before(new Date())){
            throw new ExpiredJwtException(null, jwtDataExtractorService.extractAllClaims(jwtToken), "The token was expired.");
        }
        return false;
    }

}
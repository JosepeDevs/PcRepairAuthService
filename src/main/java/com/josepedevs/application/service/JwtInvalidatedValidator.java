package com.josepedevs.application.service;

import com.josepedevs.domain.exceptions.TokenNotValidException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@AllArgsConstructor
public class JwtInvalidatedValidator {

    private final JwtDataExtractorService jwtDataExtractorService;
    private final AuthDataFinder authDataFinder;

    public boolean isTokenInvalidated(String jwtToken) {
        final var username = jwtDataExtractorService.extractUsername(jwtToken);
        final var user = authDataFinder.findByUsername(username);
        final var token = user.getCurrentToken();
        if (Objects.equals(token, "invalid") || token == null) {
            throw new TokenNotValidException("Your token was invalidated and access is not permitted.");
        }
        return true;
    }
}

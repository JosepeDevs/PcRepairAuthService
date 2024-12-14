package com.josepedevs.application.service;

import com.josepedevs.domain.exceptions.InadequateRoleException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class JwtRoleValidator {

    private final JwtDataExtractorService jwtDataExtractorService;
    private final AuthDataFinder authDataFinder;

    /**
     * check if token belongs to an admin
     * @param jwtToken token value
     * @return true if token belongs to admin
     */
    public boolean isTokenFromAdmin(String jwtToken) {
        final String username = jwtDataExtractorService.extractUsername(jwtToken);
        final var user = authDataFinder.findByUsername(username);
        if ( !user.getRole().equals("ADMIN") ){
            throw new InadequateRoleException("You do not have the required authority to access this resource.");
        }
        return true;
    }

}

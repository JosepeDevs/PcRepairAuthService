package com.josepedevs.application.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class JwtMasterValidator {

    private final JwtRoleValidator jwtRoleValidator;
    private final JwtExpirationValidator jwtExpirationValidator;
    private final JwtInvalidatedValidator jwtInvalidatedValidator;

    /**
     *  Checks that the user from the token is and admin and that it is not expired nor invalidated
     * @param jwtToken token string
     * @return true if all is valid, false if not
     */
    public boolean isAdminTokenCompletelyValidated(String jwtToken) {
        return (
                !jwtExpirationValidator.isTokenExpired(jwtToken) &&
                jwtInvalidatedValidator.isTokenInvalidated(jwtToken) &&
                jwtRoleValidator.isTokenFromAdmin(jwtToken)
        );
    }

}

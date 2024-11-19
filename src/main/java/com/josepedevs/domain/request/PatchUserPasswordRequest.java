package com.josepedevs.domain.request;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public class PatchUserPasswordRequest {

    private final String jwtToken;

    private final String newPassword;

}

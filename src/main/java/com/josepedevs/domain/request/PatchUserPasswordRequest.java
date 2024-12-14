package com.josepedevs.domain.request;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder(toBuilder = true)
public class PatchUserPasswordRequest {

    private final String jwtToken;

    private final UUID authDataId;

    private final String newPassword;

}

package com.josepedevs.domain.request;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public class PatchUserRoleRequest {

    private final String jwtToken;

    private final UpdateRoleRequest updateRoleRequest;

}

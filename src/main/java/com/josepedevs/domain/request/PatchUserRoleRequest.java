package com.josepedevs.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
public class PatchUserRoleRequest {

    private final String jwtToken;
    private final String id;
    private final String role;

}

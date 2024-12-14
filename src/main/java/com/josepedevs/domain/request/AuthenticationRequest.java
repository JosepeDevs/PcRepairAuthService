package com.josepedevs.domain.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class AuthenticationRequest {
	private final String jwtToken	;
	private final String username;
	private final String psswrd;
}

package com.josepedevs.domain.usecase;

import com.josepedevs.domain.request.PatchUserPasswordRequest;

import java.util.function.Function;

public interface PatchAuthenticationDataPasswordUseCase extends Function<PatchUserPasswordRequest, Boolean> {
}

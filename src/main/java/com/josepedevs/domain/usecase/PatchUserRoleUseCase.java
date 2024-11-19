package com.josepedevs.domain.usecase;

import com.josepedevs.domain.request.PatchUserRoleRequest;

import java.util.function.Function;

public interface PatchUserRoleUseCase extends Function<PatchUserRoleRequest, Boolean> {
}

package com.josepedevs.infra.input.rest.authenticationdata.mapper;

import com.josepedevs.domain.request.DeleteHardUserRequest;
import com.josepedevs.domain.request.PatchUserPasswordRequest;
import com.josepedevs.domain.request.PatchUserRoleRequest;
import com.josepedevs.domain.request.UpdateRoleRequest;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(
        componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface RestAuthenticationDataMapper {

    DeleteHardUserRequest map(String jwtToken, UUID userId);

    PatchUserPasswordRequest map(String jwtToken, String newPassword);

    PatchUserRoleRequest map(String jwtToken, UpdateRoleRequest updateRoleRequest);

}

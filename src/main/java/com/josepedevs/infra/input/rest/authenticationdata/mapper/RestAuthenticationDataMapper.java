package com.josepedevs.infra.input.rest.authenticationdata.mapper;

import com.josepedevs.domain.request.DeleteHardUserRequest;
import com.josepedevs.domain.request.PatchUserPasswordRequest;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(
        componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class RestAuthenticationDataMapper {

    public abstract DeleteHardUserRequest map(String jwtToken, UUID userId);

    public abstract PatchUserPasswordRequest map(String jwtToken, UUID authDataId, String newPassword);

}

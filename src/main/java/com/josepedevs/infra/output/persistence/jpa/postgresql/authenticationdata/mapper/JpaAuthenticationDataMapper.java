package com.josepedevs.infra.output.persistence.jpa.postgresql.authenticationdata.mapper;

import com.josepedevs.domain.entity.AuthenticationData;
import com.josepedevs.infra.output.persistence.jpa.postgresql.authenticationdata.entity.AuthenticationDataEntity;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class JpaAuthenticationDataMapper {

    public abstract AuthenticationDataEntity map(AuthenticationData data);

    public abstract AuthenticationData map(AuthenticationDataEntity data);

}

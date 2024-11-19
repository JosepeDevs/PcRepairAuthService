package com.josepedevs.infra.output.persistence.jpa.postgresql.authenticationdata.mapper;

import com.josepedevs.domain.entity.AuthenticationData;
import com.josepedevs.infra.output.persistence.jpa.postgresql.authenticationdata.entity.AuthenticationDataEntity;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface JpaAuthenticationDataMapper {

    AuthenticationDataEntity map(AuthenticationData data);

    AuthenticationData map(AuthenticationDataEntity data);

}

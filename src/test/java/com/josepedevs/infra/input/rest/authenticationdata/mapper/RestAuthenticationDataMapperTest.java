package com.josepedevs.infra.input.rest.authenticationdata.mapper;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class RestAuthenticationDataMapperTest {

    private RestAuthenticationDataMapper mapper = Mappers.getMapper(RestAuthenticationDataMapper.class);
    private final EasyRandom easyRandom = new EasyRandom();

    @Test
    void map_GivenTokenAndId_ThenReturnsDeleteRequest() {
        final var token = easyRandom.nextObject(String.class);
        final var id = easyRandom.nextObject(UUID.class);

        final var mapped = mapper.map(token, id);

        assertNotNull(mapped);
        assertEquals(mapped.getJwtToken(), token);
        assertEquals(mapped.getUserId(), id);
    }

    @Test
    void map_GivenBlankTokenAndId_ThenReturnsBlankDeleteRequest() {
        final var token = "";
        final UUID id = null;

        final var mapped = mapper.map(token, id);

        assertNotNull(mapped);
        assertEquals(mapped.getJwtToken(), token);
        assertEquals(mapped.getUserId(), id);
    }

    @Test
    void map_GivenTokenAndAuthDataIdAndPassword_ThenReturnsPatchPasswordRequest() {
        final var token = easyRandom.nextObject(String.class);
        final var psswrd = easyRandom.nextObject(String.class);
        final var id = easyRandom.nextObject(UUID.class);

        final var mapped = mapper.map(token, id, psswrd);

        assertNotNull(mapped);
        assertEquals(mapped.getJwtToken(), token);
        assertEquals(mapped.getAuthDataId(), id);
        assertEquals(mapped.getNewPassword(), psswrd);
    }

    @Test
    void map_GivenBlankTokenAndAuthDataAndPassword_ThenReturnsPatchPasswordRequest() {
        final var token = "";
        final var psswrd = "";
        final UUID id = null;

        final var mapped = mapper.map(token, id, psswrd);

        assertNotNull(mapped);
        assertEquals(mapped.getJwtToken(), token);
        assertEquals(mapped.getAuthDataId(), id);
        assertEquals(mapped.getNewPassword(), psswrd);
    }
}
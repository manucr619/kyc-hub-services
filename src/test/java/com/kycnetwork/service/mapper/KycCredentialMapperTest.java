package com.kycnetwork.service.mapper;

import static com.kycnetwork.domain.KycCredentialAsserts.*;
import static com.kycnetwork.domain.KycCredentialTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class KycCredentialMapperTest {

    private KycCredentialMapper kycCredentialMapper;

    @BeforeEach
    void setUp() {
        kycCredentialMapper = new KycCredentialMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getKycCredentialSample1();
        var actual = kycCredentialMapper.toEntity(kycCredentialMapper.toDto(expected));
        assertKycCredentialAllPropertiesEquals(expected, actual);
    }
}

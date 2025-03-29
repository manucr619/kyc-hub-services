package com.kycnetwork.service.mapper;

import static com.kycnetwork.domain.KycConsentAsserts.*;
import static com.kycnetwork.domain.KycConsentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class KycConsentMapperTest {

    private KycConsentMapper kycConsentMapper;

    @BeforeEach
    void setUp() {
        kycConsentMapper = new KycConsentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getKycConsentSample1();
        var actual = kycConsentMapper.toEntity(kycConsentMapper.toDto(expected));
        assertKycConsentAllPropertiesEquals(expected, actual);
    }
}

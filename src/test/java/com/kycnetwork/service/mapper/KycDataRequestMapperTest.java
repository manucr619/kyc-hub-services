package com.kycnetwork.service.mapper;

import static com.kycnetwork.domain.KycDataRequestAsserts.*;
import static com.kycnetwork.domain.KycDataRequestTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class KycDataRequestMapperTest {

    private KycDataRequestMapper kycDataRequestMapper;

    @BeforeEach
    void setUp() {
        kycDataRequestMapper = new KycDataRequestMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getKycDataRequestSample1();
        var actual = kycDataRequestMapper.toEntity(kycDataRequestMapper.toDto(expected));
        assertKycDataRequestAllPropertiesEquals(expected, actual);
    }
}

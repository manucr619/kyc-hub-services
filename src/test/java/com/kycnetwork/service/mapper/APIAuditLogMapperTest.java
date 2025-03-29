package com.kycnetwork.service.mapper;

import static com.kycnetwork.domain.APIAuditLogAsserts.*;
import static com.kycnetwork.domain.APIAuditLogTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class APIAuditLogMapperTest {

    private APIAuditLogMapper aPIAuditLogMapper;

    @BeforeEach
    void setUp() {
        aPIAuditLogMapper = new APIAuditLogMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAPIAuditLogSample1();
        var actual = aPIAuditLogMapper.toEntity(aPIAuditLogMapper.toDto(expected));
        assertAPIAuditLogAllPropertiesEquals(expected, actual);
    }
}

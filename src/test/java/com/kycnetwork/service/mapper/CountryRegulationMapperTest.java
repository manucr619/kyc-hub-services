package com.kycnetwork.service.mapper;

import static com.kycnetwork.domain.CountryRegulationAsserts.*;
import static com.kycnetwork.domain.CountryRegulationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CountryRegulationMapperTest {

    private CountryRegulationMapper countryRegulationMapper;

    @BeforeEach
    void setUp() {
        countryRegulationMapper = new CountryRegulationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCountryRegulationSample1();
        var actual = countryRegulationMapper.toEntity(countryRegulationMapper.toDto(expected));
        assertCountryRegulationAllPropertiesEquals(expected, actual);
    }
}

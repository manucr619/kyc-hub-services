package com.kycnetwork.domain;

import static com.kycnetwork.domain.CountryRegulationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kycnetwork.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CountryRegulationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CountryRegulation.class);
        CountryRegulation countryRegulation1 = getCountryRegulationSample1();
        CountryRegulation countryRegulation2 = new CountryRegulation();
        assertThat(countryRegulation1).isNotEqualTo(countryRegulation2);

        countryRegulation2.setId(countryRegulation1.getId());
        assertThat(countryRegulation1).isEqualTo(countryRegulation2);

        countryRegulation2 = getCountryRegulationSample2();
        assertThat(countryRegulation1).isNotEqualTo(countryRegulation2);
    }
}

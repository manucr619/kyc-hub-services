package com.kycnetwork.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.kycnetwork.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CountryRegulationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CountryRegulationDTO.class);
        CountryRegulationDTO countryRegulationDTO1 = new CountryRegulationDTO();
        countryRegulationDTO1.setId(1L);
        CountryRegulationDTO countryRegulationDTO2 = new CountryRegulationDTO();
        assertThat(countryRegulationDTO1).isNotEqualTo(countryRegulationDTO2);
        countryRegulationDTO2.setId(countryRegulationDTO1.getId());
        assertThat(countryRegulationDTO1).isEqualTo(countryRegulationDTO2);
        countryRegulationDTO2.setId(2L);
        assertThat(countryRegulationDTO1).isNotEqualTo(countryRegulationDTO2);
        countryRegulationDTO1.setId(null);
        assertThat(countryRegulationDTO1).isNotEqualTo(countryRegulationDTO2);
    }
}

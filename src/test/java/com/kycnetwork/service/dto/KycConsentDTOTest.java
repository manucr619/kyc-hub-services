package com.kycnetwork.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.kycnetwork.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class KycConsentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(KycConsentDTO.class);
        KycConsentDTO kycConsentDTO1 = new KycConsentDTO();
        kycConsentDTO1.setId(1L);
        KycConsentDTO kycConsentDTO2 = new KycConsentDTO();
        assertThat(kycConsentDTO1).isNotEqualTo(kycConsentDTO2);
        kycConsentDTO2.setId(kycConsentDTO1.getId());
        assertThat(kycConsentDTO1).isEqualTo(kycConsentDTO2);
        kycConsentDTO2.setId(2L);
        assertThat(kycConsentDTO1).isNotEqualTo(kycConsentDTO2);
        kycConsentDTO1.setId(null);
        assertThat(kycConsentDTO1).isNotEqualTo(kycConsentDTO2);
    }
}

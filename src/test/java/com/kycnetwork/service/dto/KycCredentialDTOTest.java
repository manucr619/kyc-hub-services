package com.kycnetwork.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.kycnetwork.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class KycCredentialDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(KycCredentialDTO.class);
        KycCredentialDTO kycCredentialDTO1 = new KycCredentialDTO();
        kycCredentialDTO1.setId(1L);
        KycCredentialDTO kycCredentialDTO2 = new KycCredentialDTO();
        assertThat(kycCredentialDTO1).isNotEqualTo(kycCredentialDTO2);
        kycCredentialDTO2.setId(kycCredentialDTO1.getId());
        assertThat(kycCredentialDTO1).isEqualTo(kycCredentialDTO2);
        kycCredentialDTO2.setId(2L);
        assertThat(kycCredentialDTO1).isNotEqualTo(kycCredentialDTO2);
        kycCredentialDTO1.setId(null);
        assertThat(kycCredentialDTO1).isNotEqualTo(kycCredentialDTO2);
    }
}

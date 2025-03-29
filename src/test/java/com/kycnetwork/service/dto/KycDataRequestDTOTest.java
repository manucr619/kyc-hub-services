package com.kycnetwork.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.kycnetwork.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class KycDataRequestDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(KycDataRequestDTO.class);
        KycDataRequestDTO kycDataRequestDTO1 = new KycDataRequestDTO();
        kycDataRequestDTO1.setId(1L);
        KycDataRequestDTO kycDataRequestDTO2 = new KycDataRequestDTO();
        assertThat(kycDataRequestDTO1).isNotEqualTo(kycDataRequestDTO2);
        kycDataRequestDTO2.setId(kycDataRequestDTO1.getId());
        assertThat(kycDataRequestDTO1).isEqualTo(kycDataRequestDTO2);
        kycDataRequestDTO2.setId(2L);
        assertThat(kycDataRequestDTO1).isNotEqualTo(kycDataRequestDTO2);
        kycDataRequestDTO1.setId(null);
        assertThat(kycDataRequestDTO1).isNotEqualTo(kycDataRequestDTO2);
    }
}

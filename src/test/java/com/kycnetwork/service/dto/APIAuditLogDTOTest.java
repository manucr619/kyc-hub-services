package com.kycnetwork.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.kycnetwork.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class APIAuditLogDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(APIAuditLogDTO.class);
        APIAuditLogDTO aPIAuditLogDTO1 = new APIAuditLogDTO();
        aPIAuditLogDTO1.setId(1L);
        APIAuditLogDTO aPIAuditLogDTO2 = new APIAuditLogDTO();
        assertThat(aPIAuditLogDTO1).isNotEqualTo(aPIAuditLogDTO2);
        aPIAuditLogDTO2.setId(aPIAuditLogDTO1.getId());
        assertThat(aPIAuditLogDTO1).isEqualTo(aPIAuditLogDTO2);
        aPIAuditLogDTO2.setId(2L);
        assertThat(aPIAuditLogDTO1).isNotEqualTo(aPIAuditLogDTO2);
        aPIAuditLogDTO1.setId(null);
        assertThat(aPIAuditLogDTO1).isNotEqualTo(aPIAuditLogDTO2);
    }
}

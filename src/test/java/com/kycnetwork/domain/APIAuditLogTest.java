package com.kycnetwork.domain;

import static com.kycnetwork.domain.APIAuditLogTestSamples.*;
import static com.kycnetwork.domain.BankTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kycnetwork.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class APIAuditLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(APIAuditLog.class);
        APIAuditLog aPIAuditLog1 = getAPIAuditLogSample1();
        APIAuditLog aPIAuditLog2 = new APIAuditLog();
        assertThat(aPIAuditLog1).isNotEqualTo(aPIAuditLog2);

        aPIAuditLog2.setId(aPIAuditLog1.getId());
        assertThat(aPIAuditLog1).isEqualTo(aPIAuditLog2);

        aPIAuditLog2 = getAPIAuditLogSample2();
        assertThat(aPIAuditLog1).isNotEqualTo(aPIAuditLog2);
    }

    @Test
    void bankTest() {
        APIAuditLog aPIAuditLog = getAPIAuditLogRandomSampleGenerator();
        Bank bankBack = getBankRandomSampleGenerator();

        aPIAuditLog.setBank(bankBack);
        assertThat(aPIAuditLog.getBank()).isEqualTo(bankBack);

        aPIAuditLog.bank(null);
        assertThat(aPIAuditLog.getBank()).isNull();
    }
}

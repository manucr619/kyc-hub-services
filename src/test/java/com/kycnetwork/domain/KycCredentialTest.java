package com.kycnetwork.domain;

import static com.kycnetwork.domain.BankTestSamples.*;
import static com.kycnetwork.domain.CustomerTestSamples.*;
import static com.kycnetwork.domain.KycCredentialTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kycnetwork.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class KycCredentialTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(KycCredential.class);
        KycCredential kycCredential1 = getKycCredentialSample1();
        KycCredential kycCredential2 = new KycCredential();
        assertThat(kycCredential1).isNotEqualTo(kycCredential2);

        kycCredential2.setId(kycCredential1.getId());
        assertThat(kycCredential1).isEqualTo(kycCredential2);

        kycCredential2 = getKycCredentialSample2();
        assertThat(kycCredential1).isNotEqualTo(kycCredential2);
    }

    @Test
    void issuerTest() {
        KycCredential kycCredential = getKycCredentialRandomSampleGenerator();
        Bank bankBack = getBankRandomSampleGenerator();

        kycCredential.setIssuer(bankBack);
        assertThat(kycCredential.getIssuer()).isEqualTo(bankBack);

        kycCredential.issuer(null);
        assertThat(kycCredential.getIssuer()).isNull();
    }

    @Test
    void customerTest() {
        KycCredential kycCredential = getKycCredentialRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        kycCredential.setCustomer(customerBack);
        assertThat(kycCredential.getCustomer()).isEqualTo(customerBack);

        kycCredential.customer(null);
        assertThat(kycCredential.getCustomer()).isNull();
    }
}

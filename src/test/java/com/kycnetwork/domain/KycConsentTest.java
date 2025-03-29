package com.kycnetwork.domain;

import static com.kycnetwork.domain.BankTestSamples.*;
import static com.kycnetwork.domain.CustomerTestSamples.*;
import static com.kycnetwork.domain.KycConsentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kycnetwork.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class KycConsentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(KycConsent.class);
        KycConsent kycConsent1 = getKycConsentSample1();
        KycConsent kycConsent2 = new KycConsent();
        assertThat(kycConsent1).isNotEqualTo(kycConsent2);

        kycConsent2.setId(kycConsent1.getId());
        assertThat(kycConsent1).isEqualTo(kycConsent2);

        kycConsent2 = getKycConsentSample2();
        assertThat(kycConsent1).isNotEqualTo(kycConsent2);
    }

    @Test
    void customerTest() {
        KycConsent kycConsent = getKycConsentRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        kycConsent.setCustomer(customerBack);
        assertThat(kycConsent.getCustomer()).isEqualTo(customerBack);

        kycConsent.customer(null);
        assertThat(kycConsent.getCustomer()).isNull();
    }

    @Test
    void issuerBankTest() {
        KycConsent kycConsent = getKycConsentRandomSampleGenerator();
        Bank bankBack = getBankRandomSampleGenerator();

        kycConsent.setIssuerBank(bankBack);
        assertThat(kycConsent.getIssuerBank()).isEqualTo(bankBack);

        kycConsent.issuerBank(null);
        assertThat(kycConsent.getIssuerBank()).isNull();
    }

    @Test
    void recipientBankTest() {
        KycConsent kycConsent = getKycConsentRandomSampleGenerator();
        Bank bankBack = getBankRandomSampleGenerator();

        kycConsent.setRecipientBank(bankBack);
        assertThat(kycConsent.getRecipientBank()).isEqualTo(bankBack);

        kycConsent.recipientBank(null);
        assertThat(kycConsent.getRecipientBank()).isNull();
    }
}

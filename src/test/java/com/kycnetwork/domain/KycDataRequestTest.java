package com.kycnetwork.domain;

import static com.kycnetwork.domain.BankTestSamples.*;
import static com.kycnetwork.domain.CustomerTestSamples.*;
import static com.kycnetwork.domain.KycConsentTestSamples.*;
import static com.kycnetwork.domain.KycDataRequestTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kycnetwork.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class KycDataRequestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(KycDataRequest.class);
        KycDataRequest kycDataRequest1 = getKycDataRequestSample1();
        KycDataRequest kycDataRequest2 = new KycDataRequest();
        assertThat(kycDataRequest1).isNotEqualTo(kycDataRequest2);

        kycDataRequest2.setId(kycDataRequest1.getId());
        assertThat(kycDataRequest1).isEqualTo(kycDataRequest2);

        kycDataRequest2 = getKycDataRequestSample2();
        assertThat(kycDataRequest1).isNotEqualTo(kycDataRequest2);
    }

    @Test
    void customerTest() {
        KycDataRequest kycDataRequest = getKycDataRequestRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        kycDataRequest.setCustomer(customerBack);
        assertThat(kycDataRequest.getCustomer()).isEqualTo(customerBack);

        kycDataRequest.customer(null);
        assertThat(kycDataRequest.getCustomer()).isNull();
    }

    @Test
    void fromBankTest() {
        KycDataRequest kycDataRequest = getKycDataRequestRandomSampleGenerator();
        Bank bankBack = getBankRandomSampleGenerator();

        kycDataRequest.setFromBank(bankBack);
        assertThat(kycDataRequest.getFromBank()).isEqualTo(bankBack);

        kycDataRequest.fromBank(null);
        assertThat(kycDataRequest.getFromBank()).isNull();
    }

    @Test
    void toBankTest() {
        KycDataRequest kycDataRequest = getKycDataRequestRandomSampleGenerator();
        Bank bankBack = getBankRandomSampleGenerator();

        kycDataRequest.setToBank(bankBack);
        assertThat(kycDataRequest.getToBank()).isEqualTo(bankBack);

        kycDataRequest.toBank(null);
        assertThat(kycDataRequest.getToBank()).isNull();
    }

    @Test
    void consentTest() {
        KycDataRequest kycDataRequest = getKycDataRequestRandomSampleGenerator();
        KycConsent kycConsentBack = getKycConsentRandomSampleGenerator();

        kycDataRequest.setConsent(kycConsentBack);
        assertThat(kycDataRequest.getConsent()).isEqualTo(kycConsentBack);

        kycDataRequest.consent(null);
        assertThat(kycDataRequest.getConsent()).isNull();
    }
}

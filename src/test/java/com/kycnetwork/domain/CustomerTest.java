package com.kycnetwork.domain;

import static com.kycnetwork.domain.CountryRegulationTestSamples.*;
import static com.kycnetwork.domain.CustomerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kycnetwork.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustomerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Customer.class);
        Customer customer1 = getCustomerSample1();
        Customer customer2 = new Customer();
        assertThat(customer1).isNotEqualTo(customer2);

        customer2.setId(customer1.getId());
        assertThat(customer1).isEqualTo(customer2);

        customer2 = getCustomerSample2();
        assertThat(customer1).isNotEqualTo(customer2);
    }

    @Test
    void countryRegulationTest() {
        Customer customer = getCustomerRandomSampleGenerator();
        CountryRegulation countryRegulationBack = getCountryRegulationRandomSampleGenerator();

        customer.setCountryRegulation(countryRegulationBack);
        assertThat(customer.getCountryRegulation()).isEqualTo(countryRegulationBack);

        customer.countryRegulation(null);
        assertThat(customer.getCountryRegulation()).isNull();
    }
}

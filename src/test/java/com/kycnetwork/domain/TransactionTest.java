package com.kycnetwork.domain;

import static com.kycnetwork.domain.BankTestSamples.*;
import static com.kycnetwork.domain.CustomerTestSamples.*;
import static com.kycnetwork.domain.TransactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.kycnetwork.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransactionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Transaction.class);
        Transaction transaction1 = getTransactionSample1();
        Transaction transaction2 = new Transaction();
        assertThat(transaction1).isNotEqualTo(transaction2);

        transaction2.setId(transaction1.getId());
        assertThat(transaction1).isEqualTo(transaction2);

        transaction2 = getTransactionSample2();
        assertThat(transaction1).isNotEqualTo(transaction2);
    }

    @Test
    void originatorTest() {
        Transaction transaction = getTransactionRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        transaction.setOriginator(customerBack);
        assertThat(transaction.getOriginator()).isEqualTo(customerBack);

        transaction.originator(null);
        assertThat(transaction.getOriginator()).isNull();
    }

    @Test
    void beneficiaryTest() {
        Transaction transaction = getTransactionRandomSampleGenerator();
        Customer customerBack = getCustomerRandomSampleGenerator();

        transaction.setBeneficiary(customerBack);
        assertThat(transaction.getBeneficiary()).isEqualTo(customerBack);

        transaction.beneficiary(null);
        assertThat(transaction.getBeneficiary()).isNull();
    }

    @Test
    void originatorBankTest() {
        Transaction transaction = getTransactionRandomSampleGenerator();
        Bank bankBack = getBankRandomSampleGenerator();

        transaction.setOriginatorBank(bankBack);
        assertThat(transaction.getOriginatorBank()).isEqualTo(bankBack);

        transaction.originatorBank(null);
        assertThat(transaction.getOriginatorBank()).isNull();
    }

    @Test
    void beneficiaryBankTest() {
        Transaction transaction = getTransactionRandomSampleGenerator();
        Bank bankBack = getBankRandomSampleGenerator();

        transaction.setBeneficiaryBank(bankBack);
        assertThat(transaction.getBeneficiaryBank()).isEqualTo(bankBack);

        transaction.beneficiaryBank(null);
        assertThat(transaction.getBeneficiaryBank()).isNull();
    }
}

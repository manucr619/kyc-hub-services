package com.kycnetwork.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TransactionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Transaction getTransactionSample1() {
        return new Transaction().id(1L).transactionRef("transactionRef1").currency("currency1").notes("notes1");
    }

    public static Transaction getTransactionSample2() {
        return new Transaction().id(2L).transactionRef("transactionRef2").currency("currency2").notes("notes2");
    }

    public static Transaction getTransactionRandomSampleGenerator() {
        return new Transaction()
            .id(longCount.incrementAndGet())
            .transactionRef(UUID.randomUUID().toString())
            .currency(UUID.randomUUID().toString())
            .notes(UUID.randomUUID().toString());
    }
}

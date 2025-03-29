package com.kycnetwork.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BankTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Bank getBankSample1() {
        return new Bank().id(1L).name("name1").bicCode("bicCode1").country("country1");
    }

    public static Bank getBankSample2() {
        return new Bank().id(2L).name("name2").bicCode("bicCode2").country("country2");
    }

    public static Bank getBankRandomSampleGenerator() {
        return new Bank()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .bicCode(UUID.randomUUID().toString())
            .country(UUID.randomUUID().toString());
    }
}

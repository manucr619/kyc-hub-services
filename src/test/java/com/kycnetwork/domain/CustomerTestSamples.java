package com.kycnetwork.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CustomerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Customer getCustomerSample1() {
        return new Customer()
            .id(1L)
            .fullName("fullName1")
            .nationalId("nationalId1")
            .passportNumber("passportNumber1")
            .address("address1")
            .email("email1")
            .phone("phone1")
            .country("country1");
    }

    public static Customer getCustomerSample2() {
        return new Customer()
            .id(2L)
            .fullName("fullName2")
            .nationalId("nationalId2")
            .passportNumber("passportNumber2")
            .address("address2")
            .email("email2")
            .phone("phone2")
            .country("country2");
    }

    public static Customer getCustomerRandomSampleGenerator() {
        return new Customer()
            .id(longCount.incrementAndGet())
            .fullName(UUID.randomUUID().toString())
            .nationalId(UUID.randomUUID().toString())
            .passportNumber(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .phone(UUID.randomUUID().toString())
            .country(UUID.randomUUID().toString());
    }
}

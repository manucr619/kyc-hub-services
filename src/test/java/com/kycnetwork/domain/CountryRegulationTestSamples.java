package com.kycnetwork.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CountryRegulationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CountryRegulation getCountryRegulationSample1() {
        return new CountryRegulation()
            .id(1L)
            .countryCode("countryCode1")
            .countryName("countryName1")
            .kycRequirements("kycRequirements1")
            .regulatorContact("regulatorContact1");
    }

    public static CountryRegulation getCountryRegulationSample2() {
        return new CountryRegulation()
            .id(2L)
            .countryCode("countryCode2")
            .countryName("countryName2")
            .kycRequirements("kycRequirements2")
            .regulatorContact("regulatorContact2");
    }

    public static CountryRegulation getCountryRegulationRandomSampleGenerator() {
        return new CountryRegulation()
            .id(longCount.incrementAndGet())
            .countryCode(UUID.randomUUID().toString())
            .countryName(UUID.randomUUID().toString())
            .kycRequirements(UUID.randomUUID().toString())
            .regulatorContact(UUID.randomUUID().toString());
    }
}

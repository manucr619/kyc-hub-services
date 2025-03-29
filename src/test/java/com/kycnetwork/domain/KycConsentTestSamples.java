package com.kycnetwork.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class KycConsentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static KycConsent getKycConsentSample1() {
        return new KycConsent().id(1L).consentToken("consentToken1");
    }

    public static KycConsent getKycConsentSample2() {
        return new KycConsent().id(2L).consentToken("consentToken2");
    }

    public static KycConsent getKycConsentRandomSampleGenerator() {
        return new KycConsent().id(longCount.incrementAndGet()).consentToken(UUID.randomUUID().toString());
    }
}

package com.kycnetwork.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class KycCredentialTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static KycCredential getKycCredentialSample1() {
        return new KycCredential()
            .id(1L)
            .levelOfVerification("levelOfVerification1")
            .schemaVersion("schemaVersion1")
            .credentialHash("credentialHash1")
            .signature("signature1");
    }

    public static KycCredential getKycCredentialSample2() {
        return new KycCredential()
            .id(2L)
            .levelOfVerification("levelOfVerification2")
            .schemaVersion("schemaVersion2")
            .credentialHash("credentialHash2")
            .signature("signature2");
    }

    public static KycCredential getKycCredentialRandomSampleGenerator() {
        return new KycCredential()
            .id(longCount.incrementAndGet())
            .levelOfVerification(UUID.randomUUID().toString())
            .schemaVersion(UUID.randomUUID().toString())
            .credentialHash(UUID.randomUUID().toString())
            .signature(UUID.randomUUID().toString());
    }
}

package com.kycnetwork.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class KycDataRequestTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static KycDataRequest getKycDataRequestSample1() {
        return new KycDataRequest()
            .id(1L)
            .requestRef("requestRef1")
            .requestPurpose("requestPurpose1")
            .encryptedResponseData("encryptedResponseData1");
    }

    public static KycDataRequest getKycDataRequestSample2() {
        return new KycDataRequest()
            .id(2L)
            .requestRef("requestRef2")
            .requestPurpose("requestPurpose2")
            .encryptedResponseData("encryptedResponseData2");
    }

    public static KycDataRequest getKycDataRequestRandomSampleGenerator() {
        return new KycDataRequest()
            .id(longCount.incrementAndGet())
            .requestRef(UUID.randomUUID().toString())
            .requestPurpose(UUID.randomUUID().toString())
            .encryptedResponseData(UUID.randomUUID().toString());
    }
}

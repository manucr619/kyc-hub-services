package com.kycnetwork.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class APIAuditLogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static APIAuditLog getAPIAuditLogSample1() {
        return new APIAuditLog()
            .id(1L)
            .action("action1")
            .statusCode(1)
            .message("message1")
            .sourceIP("sourceIP1")
            .initiatedBy("initiatedBy1")
            .endpointAccessed("endpointAccessed1");
    }

    public static APIAuditLog getAPIAuditLogSample2() {
        return new APIAuditLog()
            .id(2L)
            .action("action2")
            .statusCode(2)
            .message("message2")
            .sourceIP("sourceIP2")
            .initiatedBy("initiatedBy2")
            .endpointAccessed("endpointAccessed2");
    }

    public static APIAuditLog getAPIAuditLogRandomSampleGenerator() {
        return new APIAuditLog()
            .id(longCount.incrementAndGet())
            .action(UUID.randomUUID().toString())
            .statusCode(intCount.incrementAndGet())
            .message(UUID.randomUUID().toString())
            .sourceIP(UUID.randomUUID().toString())
            .initiatedBy(UUID.randomUUID().toString())
            .endpointAccessed(UUID.randomUUID().toString());
    }
}

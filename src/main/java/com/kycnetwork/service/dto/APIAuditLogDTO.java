package com.kycnetwork.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.kycnetwork.domain.APIAuditLog} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class APIAuditLogDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant eventTime;

    @NotNull
    private String action;

    private Integer statusCode;

    private String message;

    private String sourceIP;

    private String initiatedBy;

    private String endpointAccessed;

    private BankDTO bank;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getEventTime() {
        return eventTime;
    }

    public void setEventTime(Instant eventTime) {
        this.eventTime = eventTime;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSourceIP() {
        return sourceIP;
    }

    public void setSourceIP(String sourceIP) {
        this.sourceIP = sourceIP;
    }

    public String getInitiatedBy() {
        return initiatedBy;
    }

    public void setInitiatedBy(String initiatedBy) {
        this.initiatedBy = initiatedBy;
    }

    public String getEndpointAccessed() {
        return endpointAccessed;
    }

    public void setEndpointAccessed(String endpointAccessed) {
        this.endpointAccessed = endpointAccessed;
    }

    public BankDTO getBank() {
        return bank;
    }

    public void setBank(BankDTO bank) {
        this.bank = bank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof APIAuditLogDTO)) {
            return false;
        }

        APIAuditLogDTO aPIAuditLogDTO = (APIAuditLogDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, aPIAuditLogDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "APIAuditLogDTO{" +
            "id=" + getId() +
            ", eventTime='" + getEventTime() + "'" +
            ", action='" + getAction() + "'" +
            ", statusCode=" + getStatusCode() +
            ", message='" + getMessage() + "'" +
            ", sourceIP='" + getSourceIP() + "'" +
            ", initiatedBy='" + getInitiatedBy() + "'" +
            ", endpointAccessed='" + getEndpointAccessed() + "'" +
            ", bank=" + getBank() +
            "}";
    }
}

package com.kycnetwork.service.dto;

import com.kycnetwork.domain.enumeration.RequestStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.kycnetwork.domain.KycDataRequest} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class KycDataRequestDTO implements Serializable {

    private Long id;

    @NotNull
    private String requestRef;

    @NotNull
    private Instant requestDate;

    @NotNull
    private RequestStatus status;

    private String requestPurpose;

    private String encryptedResponseData;

    private Instant respondedAt;

    private CustomerDTO customer;

    private BankDTO fromBank;

    private BankDTO toBank;

    private KycConsentDTO consent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestRef() {
        return requestRef;
    }

    public void setRequestRef(String requestRef) {
        this.requestRef = requestRef;
    }

    public Instant getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Instant requestDate) {
        this.requestDate = requestDate;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public String getRequestPurpose() {
        return requestPurpose;
    }

    public void setRequestPurpose(String requestPurpose) {
        this.requestPurpose = requestPurpose;
    }

    public String getEncryptedResponseData() {
        return encryptedResponseData;
    }

    public void setEncryptedResponseData(String encryptedResponseData) {
        this.encryptedResponseData = encryptedResponseData;
    }

    public Instant getRespondedAt() {
        return respondedAt;
    }

    public void setRespondedAt(Instant respondedAt) {
        this.respondedAt = respondedAt;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public BankDTO getFromBank() {
        return fromBank;
    }

    public void setFromBank(BankDTO fromBank) {
        this.fromBank = fromBank;
    }

    public BankDTO getToBank() {
        return toBank;
    }

    public void setToBank(BankDTO toBank) {
        this.toBank = toBank;
    }

    public KycConsentDTO getConsent() {
        return consent;
    }

    public void setConsent(KycConsentDTO consent) {
        this.consent = consent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KycDataRequestDTO)) {
            return false;
        }

        KycDataRequestDTO kycDataRequestDTO = (KycDataRequestDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, kycDataRequestDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "KycDataRequestDTO{" +
            "id=" + getId() +
            ", requestRef='" + getRequestRef() + "'" +
            ", requestDate='" + getRequestDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", requestPurpose='" + getRequestPurpose() + "'" +
            ", encryptedResponseData='" + getEncryptedResponseData() + "'" +
            ", respondedAt='" + getRespondedAt() + "'" +
            ", customer=" + getCustomer() +
            ", fromBank=" + getFromBank() +
            ", toBank=" + getToBank() +
            ", consent=" + getConsent() +
            "}";
    }
}

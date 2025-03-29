package com.kycnetwork.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.kycnetwork.domain.KycConsent} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class KycConsentDTO implements Serializable {

    private Long id;

    @NotNull
    private String consentToken;

    @NotNull
    private Instant consentGivenAt;

    private Instant expiresAt;

    private Boolean isRevoked;

    private Instant revokedAt;

    private CustomerDTO customer;

    private BankDTO issuerBank;

    private BankDTO recipientBank;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConsentToken() {
        return consentToken;
    }

    public void setConsentToken(String consentToken) {
        this.consentToken = consentToken;
    }

    public Instant getConsentGivenAt() {
        return consentGivenAt;
    }

    public void setConsentGivenAt(Instant consentGivenAt) {
        this.consentGivenAt = consentGivenAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Boolean getIsRevoked() {
        return isRevoked;
    }

    public void setIsRevoked(Boolean isRevoked) {
        this.isRevoked = isRevoked;
    }

    public Instant getRevokedAt() {
        return revokedAt;
    }

    public void setRevokedAt(Instant revokedAt) {
        this.revokedAt = revokedAt;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public BankDTO getIssuerBank() {
        return issuerBank;
    }

    public void setIssuerBank(BankDTO issuerBank) {
        this.issuerBank = issuerBank;
    }

    public BankDTO getRecipientBank() {
        return recipientBank;
    }

    public void setRecipientBank(BankDTO recipientBank) {
        this.recipientBank = recipientBank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KycConsentDTO)) {
            return false;
        }

        KycConsentDTO kycConsentDTO = (KycConsentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, kycConsentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "KycConsentDTO{" +
            "id=" + getId() +
            ", consentToken='" + getConsentToken() + "'" +
            ", consentGivenAt='" + getConsentGivenAt() + "'" +
            ", expiresAt='" + getExpiresAt() + "'" +
            ", isRevoked='" + getIsRevoked() + "'" +
            ", revokedAt='" + getRevokedAt() + "'" +
            ", customer=" + getCustomer() +
            ", issuerBank=" + getIssuerBank() +
            ", recipientBank=" + getRecipientBank() +
            "}";
    }
}

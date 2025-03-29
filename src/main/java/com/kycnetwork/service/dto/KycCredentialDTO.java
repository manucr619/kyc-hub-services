package com.kycnetwork.service.dto;

import com.kycnetwork.domain.enumeration.CredentialStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.kycnetwork.domain.KycCredential} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class KycCredentialDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant issuedDate;

    @NotNull
    private Instant expiryDate;

    @NotNull
    private CredentialStatus status;

    private String levelOfVerification;

    private String schemaVersion;

    @NotNull
    private String credentialHash;

    @NotNull
    private String signature;

    private BankDTO issuer;

    private CustomerDTO customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(Instant issuedDate) {
        this.issuedDate = issuedDate;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public CredentialStatus getStatus() {
        return status;
    }

    public void setStatus(CredentialStatus status) {
        this.status = status;
    }

    public String getLevelOfVerification() {
        return levelOfVerification;
    }

    public void setLevelOfVerification(String levelOfVerification) {
        this.levelOfVerification = levelOfVerification;
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public String getCredentialHash() {
        return credentialHash;
    }

    public void setCredentialHash(String credentialHash) {
        this.credentialHash = credentialHash;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public BankDTO getIssuer() {
        return issuer;
    }

    public void setIssuer(BankDTO issuer) {
        this.issuer = issuer;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KycCredentialDTO)) {
            return false;
        }

        KycCredentialDTO kycCredentialDTO = (KycCredentialDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, kycCredentialDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "KycCredentialDTO{" +
            "id=" + getId() +
            ", issuedDate='" + getIssuedDate() + "'" +
            ", expiryDate='" + getExpiryDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", levelOfVerification='" + getLevelOfVerification() + "'" +
            ", schemaVersion='" + getSchemaVersion() + "'" +
            ", credentialHash='" + getCredentialHash() + "'" +
            ", signature='" + getSignature() + "'" +
            ", issuer=" + getIssuer() +
            ", customer=" + getCustomer() +
            "}";
    }
}

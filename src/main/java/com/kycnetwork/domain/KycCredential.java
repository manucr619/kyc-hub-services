package com.kycnetwork.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kycnetwork.domain.enumeration.CredentialStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A KycCredential.
 */
@Entity
@Table(name = "kyc_credential")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class KycCredential implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "issued_date", nullable = false)
    private Instant issuedDate;

    @NotNull
    @Column(name = "expiry_date", nullable = false)
    private Instant expiryDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CredentialStatus status;

    @Column(name = "level_of_verification")
    private String levelOfVerification;

    @Column(name = "schema_version")
    private String schemaVersion;

    @NotNull
    @Column(name = "credential_hash", nullable = false)
    private String credentialHash;

    @NotNull
    @Column(name = "signature", nullable = false)
    private String signature;

    @ManyToOne(fetch = FetchType.LAZY)
    private Bank issuer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "countryRegulation" }, allowSetters = true)
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public KycCredential id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getIssuedDate() {
        return this.issuedDate;
    }

    public KycCredential issuedDate(Instant issuedDate) {
        this.setIssuedDate(issuedDate);
        return this;
    }

    public void setIssuedDate(Instant issuedDate) {
        this.issuedDate = issuedDate;
    }

    public Instant getExpiryDate() {
        return this.expiryDate;
    }

    public KycCredential expiryDate(Instant expiryDate) {
        this.setExpiryDate(expiryDate);
        return this;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public CredentialStatus getStatus() {
        return this.status;
    }

    public KycCredential status(CredentialStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(CredentialStatus status) {
        this.status = status;
    }

    public String getLevelOfVerification() {
        return this.levelOfVerification;
    }

    public KycCredential levelOfVerification(String levelOfVerification) {
        this.setLevelOfVerification(levelOfVerification);
        return this;
    }

    public void setLevelOfVerification(String levelOfVerification) {
        this.levelOfVerification = levelOfVerification;
    }

    public String getSchemaVersion() {
        return this.schemaVersion;
    }

    public KycCredential schemaVersion(String schemaVersion) {
        this.setSchemaVersion(schemaVersion);
        return this;
    }

    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public String getCredentialHash() {
        return this.credentialHash;
    }

    public KycCredential credentialHash(String credentialHash) {
        this.setCredentialHash(credentialHash);
        return this;
    }

    public void setCredentialHash(String credentialHash) {
        this.credentialHash = credentialHash;
    }

    public String getSignature() {
        return this.signature;
    }

    public KycCredential signature(String signature) {
        this.setSignature(signature);
        return this;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Bank getIssuer() {
        return this.issuer;
    }

    public void setIssuer(Bank bank) {
        this.issuer = bank;
    }

    public KycCredential issuer(Bank bank) {
        this.setIssuer(bank);
        return this;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public KycCredential customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KycCredential)) {
            return false;
        }
        return getId() != null && getId().equals(((KycCredential) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "KycCredential{" +
            "id=" + getId() +
            ", issuedDate='" + getIssuedDate() + "'" +
            ", expiryDate='" + getExpiryDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", levelOfVerification='" + getLevelOfVerification() + "'" +
            ", schemaVersion='" + getSchemaVersion() + "'" +
            ", credentialHash='" + getCredentialHash() + "'" +
            ", signature='" + getSignature() + "'" +
            "}";
    }
}

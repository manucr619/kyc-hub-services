package com.kycnetwork.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A KycConsent.
 */
@Entity
@Table(name = "kyc_consent")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class KycConsent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "consent_token", nullable = false)
    private String consentToken;

    @NotNull
    @Column(name = "consent_given_at", nullable = false)
    private Instant consentGivenAt;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @Column(name = "is_revoked")
    private Boolean isRevoked;

    @Column(name = "revoked_at")
    private Instant revokedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "countryRegulation" }, allowSetters = true)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Bank issuerBank;

    @ManyToOne(fetch = FetchType.LAZY)
    private Bank recipientBank;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public KycConsent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConsentToken() {
        return this.consentToken;
    }

    public KycConsent consentToken(String consentToken) {
        this.setConsentToken(consentToken);
        return this;
    }

    public void setConsentToken(String consentToken) {
        this.consentToken = consentToken;
    }

    public Instant getConsentGivenAt() {
        return this.consentGivenAt;
    }

    public KycConsent consentGivenAt(Instant consentGivenAt) {
        this.setConsentGivenAt(consentGivenAt);
        return this;
    }

    public void setConsentGivenAt(Instant consentGivenAt) {
        this.consentGivenAt = consentGivenAt;
    }

    public Instant getExpiresAt() {
        return this.expiresAt;
    }

    public KycConsent expiresAt(Instant expiresAt) {
        this.setExpiresAt(expiresAt);
        return this;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Boolean getIsRevoked() {
        return this.isRevoked;
    }

    public KycConsent isRevoked(Boolean isRevoked) {
        this.setIsRevoked(isRevoked);
        return this;
    }

    public void setIsRevoked(Boolean isRevoked) {
        this.isRevoked = isRevoked;
    }

    public Instant getRevokedAt() {
        return this.revokedAt;
    }

    public KycConsent revokedAt(Instant revokedAt) {
        this.setRevokedAt(revokedAt);
        return this;
    }

    public void setRevokedAt(Instant revokedAt) {
        this.revokedAt = revokedAt;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public KycConsent customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    public Bank getIssuerBank() {
        return this.issuerBank;
    }

    public void setIssuerBank(Bank bank) {
        this.issuerBank = bank;
    }

    public KycConsent issuerBank(Bank bank) {
        this.setIssuerBank(bank);
        return this;
    }

    public Bank getRecipientBank() {
        return this.recipientBank;
    }

    public void setRecipientBank(Bank bank) {
        this.recipientBank = bank;
    }

    public KycConsent recipientBank(Bank bank) {
        this.setRecipientBank(bank);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KycConsent)) {
            return false;
        }
        return getId() != null && getId().equals(((KycConsent) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "KycConsent{" +
            "id=" + getId() +
            ", consentToken='" + getConsentToken() + "'" +
            ", consentGivenAt='" + getConsentGivenAt() + "'" +
            ", expiresAt='" + getExpiresAt() + "'" +
            ", isRevoked='" + getIsRevoked() + "'" +
            ", revokedAt='" + getRevokedAt() + "'" +
            "}";
    }
}

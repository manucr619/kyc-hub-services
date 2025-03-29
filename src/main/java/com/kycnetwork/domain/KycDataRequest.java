package com.kycnetwork.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kycnetwork.domain.enumeration.RequestStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A KycDataRequest.
 */
@Entity
@Table(name = "kyc_data_request")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class KycDataRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "request_ref", nullable = false)
    private String requestRef;

    @NotNull
    @Column(name = "request_date", nullable = false)
    private Instant requestDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatus status;

    @Column(name = "request_purpose")
    private String requestPurpose;

    @Column(name = "encrypted_response_data")
    private String encryptedResponseData;

    @Column(name = "responded_at")
    private Instant respondedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "countryRegulation" }, allowSetters = true)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Bank fromBank;

    @ManyToOne(fetch = FetchType.LAZY)
    private Bank toBank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "customer", "issuerBank", "recipientBank" }, allowSetters = true)
    private KycConsent consent;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public KycDataRequest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRequestRef() {
        return this.requestRef;
    }

    public KycDataRequest requestRef(String requestRef) {
        this.setRequestRef(requestRef);
        return this;
    }

    public void setRequestRef(String requestRef) {
        this.requestRef = requestRef;
    }

    public Instant getRequestDate() {
        return this.requestDate;
    }

    public KycDataRequest requestDate(Instant requestDate) {
        this.setRequestDate(requestDate);
        return this;
    }

    public void setRequestDate(Instant requestDate) {
        this.requestDate = requestDate;
    }

    public RequestStatus getStatus() {
        return this.status;
    }

    public KycDataRequest status(RequestStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public String getRequestPurpose() {
        return this.requestPurpose;
    }

    public KycDataRequest requestPurpose(String requestPurpose) {
        this.setRequestPurpose(requestPurpose);
        return this;
    }

    public void setRequestPurpose(String requestPurpose) {
        this.requestPurpose = requestPurpose;
    }

    public String getEncryptedResponseData() {
        return this.encryptedResponseData;
    }

    public KycDataRequest encryptedResponseData(String encryptedResponseData) {
        this.setEncryptedResponseData(encryptedResponseData);
        return this;
    }

    public void setEncryptedResponseData(String encryptedResponseData) {
        this.encryptedResponseData = encryptedResponseData;
    }

    public Instant getRespondedAt() {
        return this.respondedAt;
    }

    public KycDataRequest respondedAt(Instant respondedAt) {
        this.setRespondedAt(respondedAt);
        return this;
    }

    public void setRespondedAt(Instant respondedAt) {
        this.respondedAt = respondedAt;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public KycDataRequest customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    public Bank getFromBank() {
        return this.fromBank;
    }

    public void setFromBank(Bank bank) {
        this.fromBank = bank;
    }

    public KycDataRequest fromBank(Bank bank) {
        this.setFromBank(bank);
        return this;
    }

    public Bank getToBank() {
        return this.toBank;
    }

    public void setToBank(Bank bank) {
        this.toBank = bank;
    }

    public KycDataRequest toBank(Bank bank) {
        this.setToBank(bank);
        return this;
    }

    public KycConsent getConsent() {
        return this.consent;
    }

    public void setConsent(KycConsent kycConsent) {
        this.consent = kycConsent;
    }

    public KycDataRequest consent(KycConsent kycConsent) {
        this.setConsent(kycConsent);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof KycDataRequest)) {
            return false;
        }
        return getId() != null && getId().equals(((KycDataRequest) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "KycDataRequest{" +
            "id=" + getId() +
            ", requestRef='" + getRequestRef() + "'" +
            ", requestDate='" + getRequestDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", requestPurpose='" + getRequestPurpose() + "'" +
            ", encryptedResponseData='" + getEncryptedResponseData() + "'" +
            ", respondedAt='" + getRespondedAt() + "'" +
            "}";
    }
}

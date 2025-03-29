package com.kycnetwork.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A APIAuditLog.
 */
@Entity
@Table(name = "api_audit_log")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class APIAuditLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "event_time", nullable = false)
    private Instant eventTime;

    @NotNull
    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "status_code")
    private Integer statusCode;

    @Column(name = "message")
    private String message;

    @Column(name = "source_ip")
    private String sourceIP;

    @Column(name = "initiated_by")
    private String initiatedBy;

    @Column(name = "endpoint_accessed")
    private String endpointAccessed;

    @ManyToOne(fetch = FetchType.LAZY)
    private Bank bank;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public APIAuditLog id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getEventTime() {
        return this.eventTime;
    }

    public APIAuditLog eventTime(Instant eventTime) {
        this.setEventTime(eventTime);
        return this;
    }

    public void setEventTime(Instant eventTime) {
        this.eventTime = eventTime;
    }

    public String getAction() {
        return this.action;
    }

    public APIAuditLog action(String action) {
        this.setAction(action);
        return this;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getStatusCode() {
        return this.statusCode;
    }

    public APIAuditLog statusCode(Integer statusCode) {
        this.setStatusCode(statusCode);
        return this;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return this.message;
    }

    public APIAuditLog message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSourceIP() {
        return this.sourceIP;
    }

    public APIAuditLog sourceIP(String sourceIP) {
        this.setSourceIP(sourceIP);
        return this;
    }

    public void setSourceIP(String sourceIP) {
        this.sourceIP = sourceIP;
    }

    public String getInitiatedBy() {
        return this.initiatedBy;
    }

    public APIAuditLog initiatedBy(String initiatedBy) {
        this.setInitiatedBy(initiatedBy);
        return this;
    }

    public void setInitiatedBy(String initiatedBy) {
        this.initiatedBy = initiatedBy;
    }

    public String getEndpointAccessed() {
        return this.endpointAccessed;
    }

    public APIAuditLog endpointAccessed(String endpointAccessed) {
        this.setEndpointAccessed(endpointAccessed);
        return this;
    }

    public void setEndpointAccessed(String endpointAccessed) {
        this.endpointAccessed = endpointAccessed;
    }

    public Bank getBank() {
        return this.bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public APIAuditLog bank(Bank bank) {
        this.setBank(bank);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof APIAuditLog)) {
            return false;
        }
        return getId() != null && getId().equals(((APIAuditLog) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "APIAuditLog{" +
            "id=" + getId() +
            ", eventTime='" + getEventTime() + "'" +
            ", action='" + getAction() + "'" +
            ", statusCode=" + getStatusCode() +
            ", message='" + getMessage() + "'" +
            ", sourceIP='" + getSourceIP() + "'" +
            ", initiatedBy='" + getInitiatedBy() + "'" +
            ", endpointAccessed='" + getEndpointAccessed() + "'" +
            "}";
    }
}

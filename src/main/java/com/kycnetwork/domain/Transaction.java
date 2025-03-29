package com.kycnetwork.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kycnetwork.domain.enumeration.TransactionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Transaction.
 */
@Entity
@Table(name = "transaction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "transaction_ref", nullable = false)
    private String transactionRef;

    @NotNull
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Column(name = "currency", nullable = false)
    private String currency;

    @NotNull
    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransactionStatus status;

    @Column(name = "notes")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "countryRegulation" }, allowSetters = true)
    private Customer originator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "countryRegulation" }, allowSetters = true)
    private Customer beneficiary;

    @ManyToOne(fetch = FetchType.LAZY)
    private Bank originatorBank;

    @ManyToOne(fetch = FetchType.LAZY)
    private Bank beneficiaryBank;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Transaction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionRef() {
        return this.transactionRef;
    }

    public Transaction transactionRef(String transactionRef) {
        this.setTransactionRef(transactionRef);
        return this;
    }

    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public Transaction amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return this.currency;
    }

    public Transaction currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    public Transaction timestamp(Instant timestamp) {
        this.setTimestamp(timestamp);
        return this;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public TransactionStatus getStatus() {
        return this.status;
    }

    public Transaction status(TransactionStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return this.notes;
    }

    public Transaction notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Customer getOriginator() {
        return this.originator;
    }

    public void setOriginator(Customer customer) {
        this.originator = customer;
    }

    public Transaction originator(Customer customer) {
        this.setOriginator(customer);
        return this;
    }

    public Customer getBeneficiary() {
        return this.beneficiary;
    }

    public void setBeneficiary(Customer customer) {
        this.beneficiary = customer;
    }

    public Transaction beneficiary(Customer customer) {
        this.setBeneficiary(customer);
        return this;
    }

    public Bank getOriginatorBank() {
        return this.originatorBank;
    }

    public void setOriginatorBank(Bank bank) {
        this.originatorBank = bank;
    }

    public Transaction originatorBank(Bank bank) {
        this.setOriginatorBank(bank);
        return this;
    }

    public Bank getBeneficiaryBank() {
        return this.beneficiaryBank;
    }

    public void setBeneficiaryBank(Bank bank) {
        this.beneficiaryBank = bank;
    }

    public Transaction beneficiaryBank(Bank bank) {
        this.setBeneficiaryBank(bank);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transaction)) {
            return false;
        }
        return getId() != null && getId().equals(((Transaction) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Transaction{" +
            "id=" + getId() +
            ", transactionRef='" + getTransactionRef() + "'" +
            ", amount=" + getAmount() +
            ", currency='" + getCurrency() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            ", status='" + getStatus() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}

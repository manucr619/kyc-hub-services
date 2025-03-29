package com.kycnetwork.service.dto;

import com.kycnetwork.domain.enumeration.TransactionStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.kycnetwork.domain.Transaction} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TransactionDTO implements Serializable {

    private Long id;

    @NotNull
    private String transactionRef;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private String currency;

    @NotNull
    private Instant timestamp;

    @NotNull
    private TransactionStatus status;

    private String notes;

    private CustomerDTO originator;

    private CustomerDTO beneficiary;

    private BankDTO originatorBank;

    private BankDTO beneficiaryBank;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public CustomerDTO getOriginator() {
        return originator;
    }

    public void setOriginator(CustomerDTO originator) {
        this.originator = originator;
    }

    public CustomerDTO getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(CustomerDTO beneficiary) {
        this.beneficiary = beneficiary;
    }

    public BankDTO getOriginatorBank() {
        return originatorBank;
    }

    public void setOriginatorBank(BankDTO originatorBank) {
        this.originatorBank = originatorBank;
    }

    public BankDTO getBeneficiaryBank() {
        return beneficiaryBank;
    }

    public void setBeneficiaryBank(BankDTO beneficiaryBank) {
        this.beneficiaryBank = beneficiaryBank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransactionDTO)) {
            return false;
        }

        TransactionDTO transactionDTO = (TransactionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, transactionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransactionDTO{" +
            "id=" + getId() +
            ", transactionRef='" + getTransactionRef() + "'" +
            ", amount=" + getAmount() +
            ", currency='" + getCurrency() + "'" +
            ", timestamp='" + getTimestamp() + "'" +
            ", status='" + getStatus() + "'" +
            ", notes='" + getNotes() + "'" +
            ", originator=" + getOriginator() +
            ", beneficiary=" + getBeneficiary() +
            ", originatorBank=" + getOriginatorBank() +
            ", beneficiaryBank=" + getBeneficiaryBank() +
            "}";
    }
}

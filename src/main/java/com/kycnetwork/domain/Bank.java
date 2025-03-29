package com.kycnetwork.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Bank.
 */
@Entity
@Table(name = "bank")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Bank implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "bic_code", nullable = false)
    private String bicCode;

    @NotNull
    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "is_active")
    private Boolean isActive;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Bank id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Bank name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBicCode() {
        return this.bicCode;
    }

    public Bank bicCode(String bicCode) {
        this.setBicCode(bicCode);
        return this;
    }

    public void setBicCode(String bicCode) {
        this.bicCode = bicCode;
    }

    public String getCountry() {
        return this.country;
    }

    public Bank country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Bank isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bank)) {
            return false;
        }
        return getId() != null && getId().equals(((Bank) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Bank{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", bicCode='" + getBicCode() + "'" +
            ", country='" + getCountry() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}

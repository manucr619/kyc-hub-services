package com.kycnetwork.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CountryRegulation.
 */
@Entity
@Table(name = "country_regulation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CountryRegulation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "country_code", nullable = false)
    private String countryCode;

    @NotNull
    @Column(name = "country_name", nullable = false)
    private String countryName;

    @Column(name = "kyc_requirements")
    private String kycRequirements;

    @Column(name = "data_localization")
    private Boolean dataLocalization;

    @Column(name = "regulator_contact")
    private String regulatorContact;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CountryRegulation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public CountryRegulation countryCode(String countryCode) {
        this.setCountryCode(countryCode);
        return this;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return this.countryName;
    }

    public CountryRegulation countryName(String countryName) {
        this.setCountryName(countryName);
        return this;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getKycRequirements() {
        return this.kycRequirements;
    }

    public CountryRegulation kycRequirements(String kycRequirements) {
        this.setKycRequirements(kycRequirements);
        return this;
    }

    public void setKycRequirements(String kycRequirements) {
        this.kycRequirements = kycRequirements;
    }

    public Boolean getDataLocalization() {
        return this.dataLocalization;
    }

    public CountryRegulation dataLocalization(Boolean dataLocalization) {
        this.setDataLocalization(dataLocalization);
        return this;
    }

    public void setDataLocalization(Boolean dataLocalization) {
        this.dataLocalization = dataLocalization;
    }

    public String getRegulatorContact() {
        return this.regulatorContact;
    }

    public CountryRegulation regulatorContact(String regulatorContact) {
        this.setRegulatorContact(regulatorContact);
        return this;
    }

    public void setRegulatorContact(String regulatorContact) {
        this.regulatorContact = regulatorContact;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CountryRegulation)) {
            return false;
        }
        return getId() != null && getId().equals(((CountryRegulation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountryRegulation{" +
            "id=" + getId() +
            ", countryCode='" + getCountryCode() + "'" +
            ", countryName='" + getCountryName() + "'" +
            ", kycRequirements='" + getKycRequirements() + "'" +
            ", dataLocalization='" + getDataLocalization() + "'" +
            ", regulatorContact='" + getRegulatorContact() + "'" +
            "}";
    }
}

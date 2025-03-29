package com.kycnetwork.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.kycnetwork.domain.CountryRegulation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CountryRegulationDTO implements Serializable {

    private Long id;

    @NotNull
    private String countryCode;

    @NotNull
    private String countryName;

    private String kycRequirements;

    private Boolean dataLocalization;

    private String regulatorContact;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getKycRequirements() {
        return kycRequirements;
    }

    public void setKycRequirements(String kycRequirements) {
        this.kycRequirements = kycRequirements;
    }

    public Boolean getDataLocalization() {
        return dataLocalization;
    }

    public void setDataLocalization(Boolean dataLocalization) {
        this.dataLocalization = dataLocalization;
    }

    public String getRegulatorContact() {
        return regulatorContact;
    }

    public void setRegulatorContact(String regulatorContact) {
        this.regulatorContact = regulatorContact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CountryRegulationDTO)) {
            return false;
        }

        CountryRegulationDTO countryRegulationDTO = (CountryRegulationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, countryRegulationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountryRegulationDTO{" +
            "id=" + getId() +
            ", countryCode='" + getCountryCode() + "'" +
            ", countryName='" + getCountryName() + "'" +
            ", kycRequirements='" + getKycRequirements() + "'" +
            ", dataLocalization='" + getDataLocalization() + "'" +
            ", regulatorContact='" + getRegulatorContact() + "'" +
            "}";
    }
}

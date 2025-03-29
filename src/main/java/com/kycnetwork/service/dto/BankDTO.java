package com.kycnetwork.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.kycnetwork.domain.Bank} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BankDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String bicCode;

    @NotNull
    private String country;

    private Boolean isActive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBicCode() {
        return bicCode;
    }

    public void setBicCode(String bicCode) {
        this.bicCode = bicCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BankDTO)) {
            return false;
        }

        BankDTO bankDTO = (BankDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bankDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BankDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", bicCode='" + getBicCode() + "'" +
            ", country='" + getCountry() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}

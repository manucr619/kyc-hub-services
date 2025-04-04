package com.kycnetwork.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class CountryRegulationAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCountryRegulationAllPropertiesEquals(CountryRegulation expected, CountryRegulation actual) {
        assertCountryRegulationAutoGeneratedPropertiesEquals(expected, actual);
        assertCountryRegulationAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCountryRegulationAllUpdatablePropertiesEquals(CountryRegulation expected, CountryRegulation actual) {
        assertCountryRegulationUpdatableFieldsEquals(expected, actual);
        assertCountryRegulationUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCountryRegulationAutoGeneratedPropertiesEquals(CountryRegulation expected, CountryRegulation actual) {
        assertThat(actual)
            .as("Verify CountryRegulation auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCountryRegulationUpdatableFieldsEquals(CountryRegulation expected, CountryRegulation actual) {
        assertThat(actual)
            .as("Verify CountryRegulation relevant properties")
            .satisfies(a -> assertThat(a.getCountryCode()).as("check countryCode").isEqualTo(expected.getCountryCode()))
            .satisfies(a -> assertThat(a.getCountryName()).as("check countryName").isEqualTo(expected.getCountryName()))
            .satisfies(a -> assertThat(a.getKycRequirements()).as("check kycRequirements").isEqualTo(expected.getKycRequirements()))
            .satisfies(a -> assertThat(a.getDataLocalization()).as("check dataLocalization").isEqualTo(expected.getDataLocalization()))
            .satisfies(a -> assertThat(a.getRegulatorContact()).as("check regulatorContact").isEqualTo(expected.getRegulatorContact()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertCountryRegulationUpdatableRelationshipsEquals(CountryRegulation expected, CountryRegulation actual) {
        // empty method
    }
}

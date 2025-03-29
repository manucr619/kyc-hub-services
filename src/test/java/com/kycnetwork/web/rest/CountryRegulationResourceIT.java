package com.kycnetwork.web.rest;

import static com.kycnetwork.domain.CountryRegulationAsserts.*;
import static com.kycnetwork.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kycnetwork.IntegrationTest;
import com.kycnetwork.domain.CountryRegulation;
import com.kycnetwork.repository.CountryRegulationRepository;
import com.kycnetwork.service.dto.CountryRegulationDTO;
import com.kycnetwork.service.mapper.CountryRegulationMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CountryRegulationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CountryRegulationResourceIT {

    private static final String DEFAULT_COUNTRY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_KYC_REQUIREMENTS = "AAAAAAAAAA";
    private static final String UPDATED_KYC_REQUIREMENTS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DATA_LOCALIZATION = false;
    private static final Boolean UPDATED_DATA_LOCALIZATION = true;

    private static final String DEFAULT_REGULATOR_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_REGULATOR_CONTACT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/country-regulations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CountryRegulationRepository countryRegulationRepository;

    @Autowired
    private CountryRegulationMapper countryRegulationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCountryRegulationMockMvc;

    private CountryRegulation countryRegulation;

    private CountryRegulation insertedCountryRegulation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CountryRegulation createEntity() {
        return new CountryRegulation()
            .countryCode(DEFAULT_COUNTRY_CODE)
            .countryName(DEFAULT_COUNTRY_NAME)
            .kycRequirements(DEFAULT_KYC_REQUIREMENTS)
            .dataLocalization(DEFAULT_DATA_LOCALIZATION)
            .regulatorContact(DEFAULT_REGULATOR_CONTACT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CountryRegulation createUpdatedEntity() {
        return new CountryRegulation()
            .countryCode(UPDATED_COUNTRY_CODE)
            .countryName(UPDATED_COUNTRY_NAME)
            .kycRequirements(UPDATED_KYC_REQUIREMENTS)
            .dataLocalization(UPDATED_DATA_LOCALIZATION)
            .regulatorContact(UPDATED_REGULATOR_CONTACT);
    }

    @BeforeEach
    public void initTest() {
        countryRegulation = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCountryRegulation != null) {
            countryRegulationRepository.delete(insertedCountryRegulation);
            insertedCountryRegulation = null;
        }
    }

    @Test
    @Transactional
    void createCountryRegulation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CountryRegulation
        CountryRegulationDTO countryRegulationDTO = countryRegulationMapper.toDto(countryRegulation);
        var returnedCountryRegulationDTO = om.readValue(
            restCountryRegulationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(countryRegulationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CountryRegulationDTO.class
        );

        // Validate the CountryRegulation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCountryRegulation = countryRegulationMapper.toEntity(returnedCountryRegulationDTO);
        assertCountryRegulationUpdatableFieldsEquals(returnedCountryRegulation, getPersistedCountryRegulation(returnedCountryRegulation));

        insertedCountryRegulation = returnedCountryRegulation;
    }

    @Test
    @Transactional
    void createCountryRegulationWithExistingId() throws Exception {
        // Create the CountryRegulation with an existing ID
        countryRegulation.setId(1L);
        CountryRegulationDTO countryRegulationDTO = countryRegulationMapper.toDto(countryRegulation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCountryRegulationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(countryRegulationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CountryRegulation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCountryCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        countryRegulation.setCountryCode(null);

        // Create the CountryRegulation, which fails.
        CountryRegulationDTO countryRegulationDTO = countryRegulationMapper.toDto(countryRegulation);

        restCountryRegulationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(countryRegulationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountryNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        countryRegulation.setCountryName(null);

        // Create the CountryRegulation, which fails.
        CountryRegulationDTO countryRegulationDTO = countryRegulationMapper.toDto(countryRegulation);

        restCountryRegulationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(countryRegulationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCountryRegulations() throws Exception {
        // Initialize the database
        insertedCountryRegulation = countryRegulationRepository.saveAndFlush(countryRegulation);

        // Get all the countryRegulationList
        restCountryRegulationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(countryRegulation.getId().intValue())))
            .andExpect(jsonPath("$.[*].countryCode").value(hasItem(DEFAULT_COUNTRY_CODE)))
            .andExpect(jsonPath("$.[*].countryName").value(hasItem(DEFAULT_COUNTRY_NAME)))
            .andExpect(jsonPath("$.[*].kycRequirements").value(hasItem(DEFAULT_KYC_REQUIREMENTS)))
            .andExpect(jsonPath("$.[*].dataLocalization").value(hasItem(DEFAULT_DATA_LOCALIZATION)))
            .andExpect(jsonPath("$.[*].regulatorContact").value(hasItem(DEFAULT_REGULATOR_CONTACT)));
    }

    @Test
    @Transactional
    void getCountryRegulation() throws Exception {
        // Initialize the database
        insertedCountryRegulation = countryRegulationRepository.saveAndFlush(countryRegulation);

        // Get the countryRegulation
        restCountryRegulationMockMvc
            .perform(get(ENTITY_API_URL_ID, countryRegulation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(countryRegulation.getId().intValue()))
            .andExpect(jsonPath("$.countryCode").value(DEFAULT_COUNTRY_CODE))
            .andExpect(jsonPath("$.countryName").value(DEFAULT_COUNTRY_NAME))
            .andExpect(jsonPath("$.kycRequirements").value(DEFAULT_KYC_REQUIREMENTS))
            .andExpect(jsonPath("$.dataLocalization").value(DEFAULT_DATA_LOCALIZATION))
            .andExpect(jsonPath("$.regulatorContact").value(DEFAULT_REGULATOR_CONTACT));
    }

    @Test
    @Transactional
    void getNonExistingCountryRegulation() throws Exception {
        // Get the countryRegulation
        restCountryRegulationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCountryRegulation() throws Exception {
        // Initialize the database
        insertedCountryRegulation = countryRegulationRepository.saveAndFlush(countryRegulation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the countryRegulation
        CountryRegulation updatedCountryRegulation = countryRegulationRepository.findById(countryRegulation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCountryRegulation are not directly saved in db
        em.detach(updatedCountryRegulation);
        updatedCountryRegulation
            .countryCode(UPDATED_COUNTRY_CODE)
            .countryName(UPDATED_COUNTRY_NAME)
            .kycRequirements(UPDATED_KYC_REQUIREMENTS)
            .dataLocalization(UPDATED_DATA_LOCALIZATION)
            .regulatorContact(UPDATED_REGULATOR_CONTACT);
        CountryRegulationDTO countryRegulationDTO = countryRegulationMapper.toDto(updatedCountryRegulation);

        restCountryRegulationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, countryRegulationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(countryRegulationDTO))
            )
            .andExpect(status().isOk());

        // Validate the CountryRegulation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCountryRegulationToMatchAllProperties(updatedCountryRegulation);
    }

    @Test
    @Transactional
    void putNonExistingCountryRegulation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        countryRegulation.setId(longCount.incrementAndGet());

        // Create the CountryRegulation
        CountryRegulationDTO countryRegulationDTO = countryRegulationMapper.toDto(countryRegulation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountryRegulationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, countryRegulationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(countryRegulationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountryRegulation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCountryRegulation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        countryRegulation.setId(longCount.incrementAndGet());

        // Create the CountryRegulation
        CountryRegulationDTO countryRegulationDTO = countryRegulationMapper.toDto(countryRegulation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryRegulationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(countryRegulationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountryRegulation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCountryRegulation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        countryRegulation.setId(longCount.incrementAndGet());

        // Create the CountryRegulation
        CountryRegulationDTO countryRegulationDTO = countryRegulationMapper.toDto(countryRegulation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryRegulationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(countryRegulationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CountryRegulation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCountryRegulationWithPatch() throws Exception {
        // Initialize the database
        insertedCountryRegulation = countryRegulationRepository.saveAndFlush(countryRegulation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the countryRegulation using partial update
        CountryRegulation partialUpdatedCountryRegulation = new CountryRegulation();
        partialUpdatedCountryRegulation.setId(countryRegulation.getId());

        partialUpdatedCountryRegulation
            .countryCode(UPDATED_COUNTRY_CODE)
            .countryName(UPDATED_COUNTRY_NAME)
            .dataLocalization(UPDATED_DATA_LOCALIZATION)
            .regulatorContact(UPDATED_REGULATOR_CONTACT);

        restCountryRegulationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountryRegulation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCountryRegulation))
            )
            .andExpect(status().isOk());

        // Validate the CountryRegulation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCountryRegulationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCountryRegulation, countryRegulation),
            getPersistedCountryRegulation(countryRegulation)
        );
    }

    @Test
    @Transactional
    void fullUpdateCountryRegulationWithPatch() throws Exception {
        // Initialize the database
        insertedCountryRegulation = countryRegulationRepository.saveAndFlush(countryRegulation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the countryRegulation using partial update
        CountryRegulation partialUpdatedCountryRegulation = new CountryRegulation();
        partialUpdatedCountryRegulation.setId(countryRegulation.getId());

        partialUpdatedCountryRegulation
            .countryCode(UPDATED_COUNTRY_CODE)
            .countryName(UPDATED_COUNTRY_NAME)
            .kycRequirements(UPDATED_KYC_REQUIREMENTS)
            .dataLocalization(UPDATED_DATA_LOCALIZATION)
            .regulatorContact(UPDATED_REGULATOR_CONTACT);

        restCountryRegulationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountryRegulation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCountryRegulation))
            )
            .andExpect(status().isOk());

        // Validate the CountryRegulation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCountryRegulationUpdatableFieldsEquals(
            partialUpdatedCountryRegulation,
            getPersistedCountryRegulation(partialUpdatedCountryRegulation)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCountryRegulation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        countryRegulation.setId(longCount.incrementAndGet());

        // Create the CountryRegulation
        CountryRegulationDTO countryRegulationDTO = countryRegulationMapper.toDto(countryRegulation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountryRegulationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, countryRegulationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(countryRegulationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountryRegulation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCountryRegulation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        countryRegulation.setId(longCount.incrementAndGet());

        // Create the CountryRegulation
        CountryRegulationDTO countryRegulationDTO = countryRegulationMapper.toDto(countryRegulation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryRegulationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(countryRegulationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CountryRegulation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCountryRegulation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        countryRegulation.setId(longCount.incrementAndGet());

        // Create the CountryRegulation
        CountryRegulationDTO countryRegulationDTO = countryRegulationMapper.toDto(countryRegulation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryRegulationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(countryRegulationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CountryRegulation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCountryRegulation() throws Exception {
        // Initialize the database
        insertedCountryRegulation = countryRegulationRepository.saveAndFlush(countryRegulation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the countryRegulation
        restCountryRegulationMockMvc
            .perform(delete(ENTITY_API_URL_ID, countryRegulation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return countryRegulationRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected CountryRegulation getPersistedCountryRegulation(CountryRegulation countryRegulation) {
        return countryRegulationRepository.findById(countryRegulation.getId()).orElseThrow();
    }

    protected void assertPersistedCountryRegulationToMatchAllProperties(CountryRegulation expectedCountryRegulation) {
        assertCountryRegulationAllPropertiesEquals(expectedCountryRegulation, getPersistedCountryRegulation(expectedCountryRegulation));
    }

    protected void assertPersistedCountryRegulationToMatchUpdatableProperties(CountryRegulation expectedCountryRegulation) {
        assertCountryRegulationAllUpdatablePropertiesEquals(
            expectedCountryRegulation,
            getPersistedCountryRegulation(expectedCountryRegulation)
        );
    }
}

package com.kycnetwork.web.rest;

import static com.kycnetwork.domain.KycConsentAsserts.*;
import static com.kycnetwork.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kycnetwork.IntegrationTest;
import com.kycnetwork.domain.KycConsent;
import com.kycnetwork.repository.KycConsentRepository;
import com.kycnetwork.service.dto.KycConsentDTO;
import com.kycnetwork.service.mapper.KycConsentMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link KycConsentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class KycConsentResourceIT {

    private static final String DEFAULT_CONSENT_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_CONSENT_TOKEN = "BBBBBBBBBB";

    private static final Instant DEFAULT_CONSENT_GIVEN_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CONSENT_GIVEN_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPIRES_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRES_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IS_REVOKED = false;
    private static final Boolean UPDATED_IS_REVOKED = true;

    private static final Instant DEFAULT_REVOKED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REVOKED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/kyc-consents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private KycConsentRepository kycConsentRepository;

    @Autowired
    private KycConsentMapper kycConsentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restKycConsentMockMvc;

    private KycConsent kycConsent;

    private KycConsent insertedKycConsent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KycConsent createEntity() {
        return new KycConsent()
            .consentToken(DEFAULT_CONSENT_TOKEN)
            .consentGivenAt(DEFAULT_CONSENT_GIVEN_AT)
            .expiresAt(DEFAULT_EXPIRES_AT)
            .isRevoked(DEFAULT_IS_REVOKED)
            .revokedAt(DEFAULT_REVOKED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KycConsent createUpdatedEntity() {
        return new KycConsent()
            .consentToken(UPDATED_CONSENT_TOKEN)
            .consentGivenAt(UPDATED_CONSENT_GIVEN_AT)
            .expiresAt(UPDATED_EXPIRES_AT)
            .isRevoked(UPDATED_IS_REVOKED)
            .revokedAt(UPDATED_REVOKED_AT);
    }

    @BeforeEach
    public void initTest() {
        kycConsent = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedKycConsent != null) {
            kycConsentRepository.delete(insertedKycConsent);
            insertedKycConsent = null;
        }
    }

    @Test
    @Transactional
    void createKycConsent() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the KycConsent
        KycConsentDTO kycConsentDTO = kycConsentMapper.toDto(kycConsent);
        var returnedKycConsentDTO = om.readValue(
            restKycConsentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycConsentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            KycConsentDTO.class
        );

        // Validate the KycConsent in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedKycConsent = kycConsentMapper.toEntity(returnedKycConsentDTO);
        assertKycConsentUpdatableFieldsEquals(returnedKycConsent, getPersistedKycConsent(returnedKycConsent));

        insertedKycConsent = returnedKycConsent;
    }

    @Test
    @Transactional
    void createKycConsentWithExistingId() throws Exception {
        // Create the KycConsent with an existing ID
        kycConsent.setId(1L);
        KycConsentDTO kycConsentDTO = kycConsentMapper.toDto(kycConsent);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restKycConsentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycConsentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the KycConsent in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkConsentTokenIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        kycConsent.setConsentToken(null);

        // Create the KycConsent, which fails.
        KycConsentDTO kycConsentDTO = kycConsentMapper.toDto(kycConsent);

        restKycConsentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycConsentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkConsentGivenAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        kycConsent.setConsentGivenAt(null);

        // Create the KycConsent, which fails.
        KycConsentDTO kycConsentDTO = kycConsentMapper.toDto(kycConsent);

        restKycConsentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycConsentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllKycConsents() throws Exception {
        // Initialize the database
        insertedKycConsent = kycConsentRepository.saveAndFlush(kycConsent);

        // Get all the kycConsentList
        restKycConsentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(kycConsent.getId().intValue())))
            .andExpect(jsonPath("$.[*].consentToken").value(hasItem(DEFAULT_CONSENT_TOKEN)))
            .andExpect(jsonPath("$.[*].consentGivenAt").value(hasItem(DEFAULT_CONSENT_GIVEN_AT.toString())))
            .andExpect(jsonPath("$.[*].expiresAt").value(hasItem(DEFAULT_EXPIRES_AT.toString())))
            .andExpect(jsonPath("$.[*].isRevoked").value(hasItem(DEFAULT_IS_REVOKED)))
            .andExpect(jsonPath("$.[*].revokedAt").value(hasItem(DEFAULT_REVOKED_AT.toString())));
    }

    @Test
    @Transactional
    void getKycConsent() throws Exception {
        // Initialize the database
        insertedKycConsent = kycConsentRepository.saveAndFlush(kycConsent);

        // Get the kycConsent
        restKycConsentMockMvc
            .perform(get(ENTITY_API_URL_ID, kycConsent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(kycConsent.getId().intValue()))
            .andExpect(jsonPath("$.consentToken").value(DEFAULT_CONSENT_TOKEN))
            .andExpect(jsonPath("$.consentGivenAt").value(DEFAULT_CONSENT_GIVEN_AT.toString()))
            .andExpect(jsonPath("$.expiresAt").value(DEFAULT_EXPIRES_AT.toString()))
            .andExpect(jsonPath("$.isRevoked").value(DEFAULT_IS_REVOKED))
            .andExpect(jsonPath("$.revokedAt").value(DEFAULT_REVOKED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingKycConsent() throws Exception {
        // Get the kycConsent
        restKycConsentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingKycConsent() throws Exception {
        // Initialize the database
        insertedKycConsent = kycConsentRepository.saveAndFlush(kycConsent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the kycConsent
        KycConsent updatedKycConsent = kycConsentRepository.findById(kycConsent.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedKycConsent are not directly saved in db
        em.detach(updatedKycConsent);
        updatedKycConsent
            .consentToken(UPDATED_CONSENT_TOKEN)
            .consentGivenAt(UPDATED_CONSENT_GIVEN_AT)
            .expiresAt(UPDATED_EXPIRES_AT)
            .isRevoked(UPDATED_IS_REVOKED)
            .revokedAt(UPDATED_REVOKED_AT);
        KycConsentDTO kycConsentDTO = kycConsentMapper.toDto(updatedKycConsent);

        restKycConsentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, kycConsentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(kycConsentDTO))
            )
            .andExpect(status().isOk());

        // Validate the KycConsent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedKycConsentToMatchAllProperties(updatedKycConsent);
    }

    @Test
    @Transactional
    void putNonExistingKycConsent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycConsent.setId(longCount.incrementAndGet());

        // Create the KycConsent
        KycConsentDTO kycConsentDTO = kycConsentMapper.toDto(kycConsent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKycConsentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, kycConsentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(kycConsentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the KycConsent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchKycConsent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycConsent.setId(longCount.incrementAndGet());

        // Create the KycConsent
        KycConsentDTO kycConsentDTO = kycConsentMapper.toDto(kycConsent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKycConsentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(kycConsentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the KycConsent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamKycConsent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycConsent.setId(longCount.incrementAndGet());

        // Create the KycConsent
        KycConsentDTO kycConsentDTO = kycConsentMapper.toDto(kycConsent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKycConsentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycConsentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the KycConsent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateKycConsentWithPatch() throws Exception {
        // Initialize the database
        insertedKycConsent = kycConsentRepository.saveAndFlush(kycConsent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the kycConsent using partial update
        KycConsent partialUpdatedKycConsent = new KycConsent();
        partialUpdatedKycConsent.setId(kycConsent.getId());

        partialUpdatedKycConsent.consentGivenAt(UPDATED_CONSENT_GIVEN_AT);

        restKycConsentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKycConsent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedKycConsent))
            )
            .andExpect(status().isOk());

        // Validate the KycConsent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertKycConsentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedKycConsent, kycConsent),
            getPersistedKycConsent(kycConsent)
        );
    }

    @Test
    @Transactional
    void fullUpdateKycConsentWithPatch() throws Exception {
        // Initialize the database
        insertedKycConsent = kycConsentRepository.saveAndFlush(kycConsent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the kycConsent using partial update
        KycConsent partialUpdatedKycConsent = new KycConsent();
        partialUpdatedKycConsent.setId(kycConsent.getId());

        partialUpdatedKycConsent
            .consentToken(UPDATED_CONSENT_TOKEN)
            .consentGivenAt(UPDATED_CONSENT_GIVEN_AT)
            .expiresAt(UPDATED_EXPIRES_AT)
            .isRevoked(UPDATED_IS_REVOKED)
            .revokedAt(UPDATED_REVOKED_AT);

        restKycConsentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKycConsent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedKycConsent))
            )
            .andExpect(status().isOk());

        // Validate the KycConsent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertKycConsentUpdatableFieldsEquals(partialUpdatedKycConsent, getPersistedKycConsent(partialUpdatedKycConsent));
    }

    @Test
    @Transactional
    void patchNonExistingKycConsent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycConsent.setId(longCount.incrementAndGet());

        // Create the KycConsent
        KycConsentDTO kycConsentDTO = kycConsentMapper.toDto(kycConsent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKycConsentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, kycConsentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(kycConsentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the KycConsent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchKycConsent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycConsent.setId(longCount.incrementAndGet());

        // Create the KycConsent
        KycConsentDTO kycConsentDTO = kycConsentMapper.toDto(kycConsent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKycConsentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(kycConsentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the KycConsent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamKycConsent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycConsent.setId(longCount.incrementAndGet());

        // Create the KycConsent
        KycConsentDTO kycConsentDTO = kycConsentMapper.toDto(kycConsent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKycConsentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(kycConsentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the KycConsent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteKycConsent() throws Exception {
        // Initialize the database
        insertedKycConsent = kycConsentRepository.saveAndFlush(kycConsent);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the kycConsent
        restKycConsentMockMvc
            .perform(delete(ENTITY_API_URL_ID, kycConsent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return kycConsentRepository.count();
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

    protected KycConsent getPersistedKycConsent(KycConsent kycConsent) {
        return kycConsentRepository.findById(kycConsent.getId()).orElseThrow();
    }

    protected void assertPersistedKycConsentToMatchAllProperties(KycConsent expectedKycConsent) {
        assertKycConsentAllPropertiesEquals(expectedKycConsent, getPersistedKycConsent(expectedKycConsent));
    }

    protected void assertPersistedKycConsentToMatchUpdatableProperties(KycConsent expectedKycConsent) {
        assertKycConsentAllUpdatablePropertiesEquals(expectedKycConsent, getPersistedKycConsent(expectedKycConsent));
    }
}

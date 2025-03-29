package com.kycnetwork.web.rest;

import static com.kycnetwork.domain.KycCredentialAsserts.*;
import static com.kycnetwork.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kycnetwork.IntegrationTest;
import com.kycnetwork.domain.KycCredential;
import com.kycnetwork.domain.enumeration.CredentialStatus;
import com.kycnetwork.repository.KycCredentialRepository;
import com.kycnetwork.service.dto.KycCredentialDTO;
import com.kycnetwork.service.mapper.KycCredentialMapper;
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
 * Integration tests for the {@link KycCredentialResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class KycCredentialResourceIT {

    private static final Instant DEFAULT_ISSUED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ISSUED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPIRY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final CredentialStatus DEFAULT_STATUS = CredentialStatus.VALID;
    private static final CredentialStatus UPDATED_STATUS = CredentialStatus.EXPIRED;

    private static final String DEFAULT_LEVEL_OF_VERIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_LEVEL_OF_VERIFICATION = "BBBBBBBBBB";

    private static final String DEFAULT_SCHEMA_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_SCHEMA_VERSION = "BBBBBBBBBB";

    private static final String DEFAULT_CREDENTIAL_HASH = "AAAAAAAAAA";
    private static final String UPDATED_CREDENTIAL_HASH = "BBBBBBBBBB";

    private static final String DEFAULT_SIGNATURE = "AAAAAAAAAA";
    private static final String UPDATED_SIGNATURE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/kyc-credentials";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private KycCredentialRepository kycCredentialRepository;

    @Autowired
    private KycCredentialMapper kycCredentialMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restKycCredentialMockMvc;

    private KycCredential kycCredential;

    private KycCredential insertedKycCredential;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KycCredential createEntity() {
        return new KycCredential()
            .issuedDate(DEFAULT_ISSUED_DATE)
            .expiryDate(DEFAULT_EXPIRY_DATE)
            .status(DEFAULT_STATUS)
            .levelOfVerification(DEFAULT_LEVEL_OF_VERIFICATION)
            .schemaVersion(DEFAULT_SCHEMA_VERSION)
            .credentialHash(DEFAULT_CREDENTIAL_HASH)
            .signature(DEFAULT_SIGNATURE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KycCredential createUpdatedEntity() {
        return new KycCredential()
            .issuedDate(UPDATED_ISSUED_DATE)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .status(UPDATED_STATUS)
            .levelOfVerification(UPDATED_LEVEL_OF_VERIFICATION)
            .schemaVersion(UPDATED_SCHEMA_VERSION)
            .credentialHash(UPDATED_CREDENTIAL_HASH)
            .signature(UPDATED_SIGNATURE);
    }

    @BeforeEach
    public void initTest() {
        kycCredential = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedKycCredential != null) {
            kycCredentialRepository.delete(insertedKycCredential);
            insertedKycCredential = null;
        }
    }

    @Test
    @Transactional
    void createKycCredential() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the KycCredential
        KycCredentialDTO kycCredentialDTO = kycCredentialMapper.toDto(kycCredential);
        var returnedKycCredentialDTO = om.readValue(
            restKycCredentialMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycCredentialDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            KycCredentialDTO.class
        );

        // Validate the KycCredential in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedKycCredential = kycCredentialMapper.toEntity(returnedKycCredentialDTO);
        assertKycCredentialUpdatableFieldsEquals(returnedKycCredential, getPersistedKycCredential(returnedKycCredential));

        insertedKycCredential = returnedKycCredential;
    }

    @Test
    @Transactional
    void createKycCredentialWithExistingId() throws Exception {
        // Create the KycCredential with an existing ID
        kycCredential.setId(1L);
        KycCredentialDTO kycCredentialDTO = kycCredentialMapper.toDto(kycCredential);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restKycCredentialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycCredentialDTO)))
            .andExpect(status().isBadRequest());

        // Validate the KycCredential in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIssuedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        kycCredential.setIssuedDate(null);

        // Create the KycCredential, which fails.
        KycCredentialDTO kycCredentialDTO = kycCredentialMapper.toDto(kycCredential);

        restKycCredentialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycCredentialDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExpiryDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        kycCredential.setExpiryDate(null);

        // Create the KycCredential, which fails.
        KycCredentialDTO kycCredentialDTO = kycCredentialMapper.toDto(kycCredential);

        restKycCredentialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycCredentialDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        kycCredential.setStatus(null);

        // Create the KycCredential, which fails.
        KycCredentialDTO kycCredentialDTO = kycCredentialMapper.toDto(kycCredential);

        restKycCredentialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycCredentialDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCredentialHashIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        kycCredential.setCredentialHash(null);

        // Create the KycCredential, which fails.
        KycCredentialDTO kycCredentialDTO = kycCredentialMapper.toDto(kycCredential);

        restKycCredentialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycCredentialDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSignatureIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        kycCredential.setSignature(null);

        // Create the KycCredential, which fails.
        KycCredentialDTO kycCredentialDTO = kycCredentialMapper.toDto(kycCredential);

        restKycCredentialMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycCredentialDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllKycCredentials() throws Exception {
        // Initialize the database
        insertedKycCredential = kycCredentialRepository.saveAndFlush(kycCredential);

        // Get all the kycCredentialList
        restKycCredentialMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(kycCredential.getId().intValue())))
            .andExpect(jsonPath("$.[*].issuedDate").value(hasItem(DEFAULT_ISSUED_DATE.toString())))
            .andExpect(jsonPath("$.[*].expiryDate").value(hasItem(DEFAULT_EXPIRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].levelOfVerification").value(hasItem(DEFAULT_LEVEL_OF_VERIFICATION)))
            .andExpect(jsonPath("$.[*].schemaVersion").value(hasItem(DEFAULT_SCHEMA_VERSION)))
            .andExpect(jsonPath("$.[*].credentialHash").value(hasItem(DEFAULT_CREDENTIAL_HASH)))
            .andExpect(jsonPath("$.[*].signature").value(hasItem(DEFAULT_SIGNATURE)));
    }

    @Test
    @Transactional
    void getKycCredential() throws Exception {
        // Initialize the database
        insertedKycCredential = kycCredentialRepository.saveAndFlush(kycCredential);

        // Get the kycCredential
        restKycCredentialMockMvc
            .perform(get(ENTITY_API_URL_ID, kycCredential.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(kycCredential.getId().intValue()))
            .andExpect(jsonPath("$.issuedDate").value(DEFAULT_ISSUED_DATE.toString()))
            .andExpect(jsonPath("$.expiryDate").value(DEFAULT_EXPIRY_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.levelOfVerification").value(DEFAULT_LEVEL_OF_VERIFICATION))
            .andExpect(jsonPath("$.schemaVersion").value(DEFAULT_SCHEMA_VERSION))
            .andExpect(jsonPath("$.credentialHash").value(DEFAULT_CREDENTIAL_HASH))
            .andExpect(jsonPath("$.signature").value(DEFAULT_SIGNATURE));
    }

    @Test
    @Transactional
    void getNonExistingKycCredential() throws Exception {
        // Get the kycCredential
        restKycCredentialMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingKycCredential() throws Exception {
        // Initialize the database
        insertedKycCredential = kycCredentialRepository.saveAndFlush(kycCredential);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the kycCredential
        KycCredential updatedKycCredential = kycCredentialRepository.findById(kycCredential.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedKycCredential are not directly saved in db
        em.detach(updatedKycCredential);
        updatedKycCredential
            .issuedDate(UPDATED_ISSUED_DATE)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .status(UPDATED_STATUS)
            .levelOfVerification(UPDATED_LEVEL_OF_VERIFICATION)
            .schemaVersion(UPDATED_SCHEMA_VERSION)
            .credentialHash(UPDATED_CREDENTIAL_HASH)
            .signature(UPDATED_SIGNATURE);
        KycCredentialDTO kycCredentialDTO = kycCredentialMapper.toDto(updatedKycCredential);

        restKycCredentialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, kycCredentialDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(kycCredentialDTO))
            )
            .andExpect(status().isOk());

        // Validate the KycCredential in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedKycCredentialToMatchAllProperties(updatedKycCredential);
    }

    @Test
    @Transactional
    void putNonExistingKycCredential() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycCredential.setId(longCount.incrementAndGet());

        // Create the KycCredential
        KycCredentialDTO kycCredentialDTO = kycCredentialMapper.toDto(kycCredential);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKycCredentialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, kycCredentialDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(kycCredentialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the KycCredential in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchKycCredential() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycCredential.setId(longCount.incrementAndGet());

        // Create the KycCredential
        KycCredentialDTO kycCredentialDTO = kycCredentialMapper.toDto(kycCredential);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKycCredentialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(kycCredentialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the KycCredential in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamKycCredential() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycCredential.setId(longCount.incrementAndGet());

        // Create the KycCredential
        KycCredentialDTO kycCredentialDTO = kycCredentialMapper.toDto(kycCredential);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKycCredentialMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycCredentialDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the KycCredential in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateKycCredentialWithPatch() throws Exception {
        // Initialize the database
        insertedKycCredential = kycCredentialRepository.saveAndFlush(kycCredential);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the kycCredential using partial update
        KycCredential partialUpdatedKycCredential = new KycCredential();
        partialUpdatedKycCredential.setId(kycCredential.getId());

        partialUpdatedKycCredential.expiryDate(UPDATED_EXPIRY_DATE);

        restKycCredentialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKycCredential.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedKycCredential))
            )
            .andExpect(status().isOk());

        // Validate the KycCredential in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertKycCredentialUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedKycCredential, kycCredential),
            getPersistedKycCredential(kycCredential)
        );
    }

    @Test
    @Transactional
    void fullUpdateKycCredentialWithPatch() throws Exception {
        // Initialize the database
        insertedKycCredential = kycCredentialRepository.saveAndFlush(kycCredential);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the kycCredential using partial update
        KycCredential partialUpdatedKycCredential = new KycCredential();
        partialUpdatedKycCredential.setId(kycCredential.getId());

        partialUpdatedKycCredential
            .issuedDate(UPDATED_ISSUED_DATE)
            .expiryDate(UPDATED_EXPIRY_DATE)
            .status(UPDATED_STATUS)
            .levelOfVerification(UPDATED_LEVEL_OF_VERIFICATION)
            .schemaVersion(UPDATED_SCHEMA_VERSION)
            .credentialHash(UPDATED_CREDENTIAL_HASH)
            .signature(UPDATED_SIGNATURE);

        restKycCredentialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKycCredential.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedKycCredential))
            )
            .andExpect(status().isOk());

        // Validate the KycCredential in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertKycCredentialUpdatableFieldsEquals(partialUpdatedKycCredential, getPersistedKycCredential(partialUpdatedKycCredential));
    }

    @Test
    @Transactional
    void patchNonExistingKycCredential() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycCredential.setId(longCount.incrementAndGet());

        // Create the KycCredential
        KycCredentialDTO kycCredentialDTO = kycCredentialMapper.toDto(kycCredential);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKycCredentialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, kycCredentialDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(kycCredentialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the KycCredential in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchKycCredential() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycCredential.setId(longCount.incrementAndGet());

        // Create the KycCredential
        KycCredentialDTO kycCredentialDTO = kycCredentialMapper.toDto(kycCredential);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKycCredentialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(kycCredentialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the KycCredential in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamKycCredential() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycCredential.setId(longCount.incrementAndGet());

        // Create the KycCredential
        KycCredentialDTO kycCredentialDTO = kycCredentialMapper.toDto(kycCredential);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKycCredentialMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(kycCredentialDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the KycCredential in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteKycCredential() throws Exception {
        // Initialize the database
        insertedKycCredential = kycCredentialRepository.saveAndFlush(kycCredential);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the kycCredential
        restKycCredentialMockMvc
            .perform(delete(ENTITY_API_URL_ID, kycCredential.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return kycCredentialRepository.count();
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

    protected KycCredential getPersistedKycCredential(KycCredential kycCredential) {
        return kycCredentialRepository.findById(kycCredential.getId()).orElseThrow();
    }

    protected void assertPersistedKycCredentialToMatchAllProperties(KycCredential expectedKycCredential) {
        assertKycCredentialAllPropertiesEquals(expectedKycCredential, getPersistedKycCredential(expectedKycCredential));
    }

    protected void assertPersistedKycCredentialToMatchUpdatableProperties(KycCredential expectedKycCredential) {
        assertKycCredentialAllUpdatablePropertiesEquals(expectedKycCredential, getPersistedKycCredential(expectedKycCredential));
    }
}

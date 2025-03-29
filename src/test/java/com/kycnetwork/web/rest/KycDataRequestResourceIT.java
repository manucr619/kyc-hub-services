package com.kycnetwork.web.rest;

import static com.kycnetwork.domain.KycDataRequestAsserts.*;
import static com.kycnetwork.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kycnetwork.IntegrationTest;
import com.kycnetwork.domain.KycDataRequest;
import com.kycnetwork.domain.enumeration.RequestStatus;
import com.kycnetwork.repository.KycDataRequestRepository;
import com.kycnetwork.service.dto.KycDataRequestDTO;
import com.kycnetwork.service.mapper.KycDataRequestMapper;
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
 * Integration tests for the {@link KycDataRequestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class KycDataRequestResourceIT {

    private static final String DEFAULT_REQUEST_REF = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_REF = "BBBBBBBBBB";

    private static final Instant DEFAULT_REQUEST_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REQUEST_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final RequestStatus DEFAULT_STATUS = RequestStatus.REQUESTED;
    private static final RequestStatus UPDATED_STATUS = RequestStatus.APPROVED;

    private static final String DEFAULT_REQUEST_PURPOSE = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_PURPOSE = "BBBBBBBBBB";

    private static final String DEFAULT_ENCRYPTED_RESPONSE_DATA = "AAAAAAAAAA";
    private static final String UPDATED_ENCRYPTED_RESPONSE_DATA = "BBBBBBBBBB";

    private static final Instant DEFAULT_RESPONDED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RESPONDED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/kyc-data-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private KycDataRequestRepository kycDataRequestRepository;

    @Autowired
    private KycDataRequestMapper kycDataRequestMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restKycDataRequestMockMvc;

    private KycDataRequest kycDataRequest;

    private KycDataRequest insertedKycDataRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KycDataRequest createEntity() {
        return new KycDataRequest()
            .requestRef(DEFAULT_REQUEST_REF)
            .requestDate(DEFAULT_REQUEST_DATE)
            .status(DEFAULT_STATUS)
            .requestPurpose(DEFAULT_REQUEST_PURPOSE)
            .encryptedResponseData(DEFAULT_ENCRYPTED_RESPONSE_DATA)
            .respondedAt(DEFAULT_RESPONDED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static KycDataRequest createUpdatedEntity() {
        return new KycDataRequest()
            .requestRef(UPDATED_REQUEST_REF)
            .requestDate(UPDATED_REQUEST_DATE)
            .status(UPDATED_STATUS)
            .requestPurpose(UPDATED_REQUEST_PURPOSE)
            .encryptedResponseData(UPDATED_ENCRYPTED_RESPONSE_DATA)
            .respondedAt(UPDATED_RESPONDED_AT);
    }

    @BeforeEach
    public void initTest() {
        kycDataRequest = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedKycDataRequest != null) {
            kycDataRequestRepository.delete(insertedKycDataRequest);
            insertedKycDataRequest = null;
        }
    }

    @Test
    @Transactional
    void createKycDataRequest() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the KycDataRequest
        KycDataRequestDTO kycDataRequestDTO = kycDataRequestMapper.toDto(kycDataRequest);
        var returnedKycDataRequestDTO = om.readValue(
            restKycDataRequestMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycDataRequestDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            KycDataRequestDTO.class
        );

        // Validate the KycDataRequest in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedKycDataRequest = kycDataRequestMapper.toEntity(returnedKycDataRequestDTO);
        assertKycDataRequestUpdatableFieldsEquals(returnedKycDataRequest, getPersistedKycDataRequest(returnedKycDataRequest));

        insertedKycDataRequest = returnedKycDataRequest;
    }

    @Test
    @Transactional
    void createKycDataRequestWithExistingId() throws Exception {
        // Create the KycDataRequest with an existing ID
        kycDataRequest.setId(1L);
        KycDataRequestDTO kycDataRequestDTO = kycDataRequestMapper.toDto(kycDataRequest);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restKycDataRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycDataRequestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the KycDataRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRequestRefIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        kycDataRequest.setRequestRef(null);

        // Create the KycDataRequest, which fails.
        KycDataRequestDTO kycDataRequestDTO = kycDataRequestMapper.toDto(kycDataRequest);

        restKycDataRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycDataRequestDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRequestDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        kycDataRequest.setRequestDate(null);

        // Create the KycDataRequest, which fails.
        KycDataRequestDTO kycDataRequestDTO = kycDataRequestMapper.toDto(kycDataRequest);

        restKycDataRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycDataRequestDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        kycDataRequest.setStatus(null);

        // Create the KycDataRequest, which fails.
        KycDataRequestDTO kycDataRequestDTO = kycDataRequestMapper.toDto(kycDataRequest);

        restKycDataRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycDataRequestDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllKycDataRequests() throws Exception {
        // Initialize the database
        insertedKycDataRequest = kycDataRequestRepository.saveAndFlush(kycDataRequest);

        // Get all the kycDataRequestList
        restKycDataRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(kycDataRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].requestRef").value(hasItem(DEFAULT_REQUEST_REF)))
            .andExpect(jsonPath("$.[*].requestDate").value(hasItem(DEFAULT_REQUEST_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].requestPurpose").value(hasItem(DEFAULT_REQUEST_PURPOSE)))
            .andExpect(jsonPath("$.[*].encryptedResponseData").value(hasItem(DEFAULT_ENCRYPTED_RESPONSE_DATA)))
            .andExpect(jsonPath("$.[*].respondedAt").value(hasItem(DEFAULT_RESPONDED_AT.toString())));
    }

    @Test
    @Transactional
    void getKycDataRequest() throws Exception {
        // Initialize the database
        insertedKycDataRequest = kycDataRequestRepository.saveAndFlush(kycDataRequest);

        // Get the kycDataRequest
        restKycDataRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, kycDataRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(kycDataRequest.getId().intValue()))
            .andExpect(jsonPath("$.requestRef").value(DEFAULT_REQUEST_REF))
            .andExpect(jsonPath("$.requestDate").value(DEFAULT_REQUEST_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.requestPurpose").value(DEFAULT_REQUEST_PURPOSE))
            .andExpect(jsonPath("$.encryptedResponseData").value(DEFAULT_ENCRYPTED_RESPONSE_DATA))
            .andExpect(jsonPath("$.respondedAt").value(DEFAULT_RESPONDED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingKycDataRequest() throws Exception {
        // Get the kycDataRequest
        restKycDataRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingKycDataRequest() throws Exception {
        // Initialize the database
        insertedKycDataRequest = kycDataRequestRepository.saveAndFlush(kycDataRequest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the kycDataRequest
        KycDataRequest updatedKycDataRequest = kycDataRequestRepository.findById(kycDataRequest.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedKycDataRequest are not directly saved in db
        em.detach(updatedKycDataRequest);
        updatedKycDataRequest
            .requestRef(UPDATED_REQUEST_REF)
            .requestDate(UPDATED_REQUEST_DATE)
            .status(UPDATED_STATUS)
            .requestPurpose(UPDATED_REQUEST_PURPOSE)
            .encryptedResponseData(UPDATED_ENCRYPTED_RESPONSE_DATA)
            .respondedAt(UPDATED_RESPONDED_AT);
        KycDataRequestDTO kycDataRequestDTO = kycDataRequestMapper.toDto(updatedKycDataRequest);

        restKycDataRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, kycDataRequestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(kycDataRequestDTO))
            )
            .andExpect(status().isOk());

        // Validate the KycDataRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedKycDataRequestToMatchAllProperties(updatedKycDataRequest);
    }

    @Test
    @Transactional
    void putNonExistingKycDataRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycDataRequest.setId(longCount.incrementAndGet());

        // Create the KycDataRequest
        KycDataRequestDTO kycDataRequestDTO = kycDataRequestMapper.toDto(kycDataRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKycDataRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, kycDataRequestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(kycDataRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the KycDataRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchKycDataRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycDataRequest.setId(longCount.incrementAndGet());

        // Create the KycDataRequest
        KycDataRequestDTO kycDataRequestDTO = kycDataRequestMapper.toDto(kycDataRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKycDataRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(kycDataRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the KycDataRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamKycDataRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycDataRequest.setId(longCount.incrementAndGet());

        // Create the KycDataRequest
        KycDataRequestDTO kycDataRequestDTO = kycDataRequestMapper.toDto(kycDataRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKycDataRequestMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(kycDataRequestDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the KycDataRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateKycDataRequestWithPatch() throws Exception {
        // Initialize the database
        insertedKycDataRequest = kycDataRequestRepository.saveAndFlush(kycDataRequest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the kycDataRequest using partial update
        KycDataRequest partialUpdatedKycDataRequest = new KycDataRequest();
        partialUpdatedKycDataRequest.setId(kycDataRequest.getId());

        partialUpdatedKycDataRequest
            .requestRef(UPDATED_REQUEST_REF)
            .requestPurpose(UPDATED_REQUEST_PURPOSE)
            .encryptedResponseData(UPDATED_ENCRYPTED_RESPONSE_DATA)
            .respondedAt(UPDATED_RESPONDED_AT);

        restKycDataRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKycDataRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedKycDataRequest))
            )
            .andExpect(status().isOk());

        // Validate the KycDataRequest in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertKycDataRequestUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedKycDataRequest, kycDataRequest),
            getPersistedKycDataRequest(kycDataRequest)
        );
    }

    @Test
    @Transactional
    void fullUpdateKycDataRequestWithPatch() throws Exception {
        // Initialize the database
        insertedKycDataRequest = kycDataRequestRepository.saveAndFlush(kycDataRequest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the kycDataRequest using partial update
        KycDataRequest partialUpdatedKycDataRequest = new KycDataRequest();
        partialUpdatedKycDataRequest.setId(kycDataRequest.getId());

        partialUpdatedKycDataRequest
            .requestRef(UPDATED_REQUEST_REF)
            .requestDate(UPDATED_REQUEST_DATE)
            .status(UPDATED_STATUS)
            .requestPurpose(UPDATED_REQUEST_PURPOSE)
            .encryptedResponseData(UPDATED_ENCRYPTED_RESPONSE_DATA)
            .respondedAt(UPDATED_RESPONDED_AT);

        restKycDataRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKycDataRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedKycDataRequest))
            )
            .andExpect(status().isOk());

        // Validate the KycDataRequest in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertKycDataRequestUpdatableFieldsEquals(partialUpdatedKycDataRequest, getPersistedKycDataRequest(partialUpdatedKycDataRequest));
    }

    @Test
    @Transactional
    void patchNonExistingKycDataRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycDataRequest.setId(longCount.incrementAndGet());

        // Create the KycDataRequest
        KycDataRequestDTO kycDataRequestDTO = kycDataRequestMapper.toDto(kycDataRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKycDataRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, kycDataRequestDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(kycDataRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the KycDataRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchKycDataRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycDataRequest.setId(longCount.incrementAndGet());

        // Create the KycDataRequest
        KycDataRequestDTO kycDataRequestDTO = kycDataRequestMapper.toDto(kycDataRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKycDataRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(kycDataRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the KycDataRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamKycDataRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        kycDataRequest.setId(longCount.incrementAndGet());

        // Create the KycDataRequest
        KycDataRequestDTO kycDataRequestDTO = kycDataRequestMapper.toDto(kycDataRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKycDataRequestMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(kycDataRequestDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the KycDataRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteKycDataRequest() throws Exception {
        // Initialize the database
        insertedKycDataRequest = kycDataRequestRepository.saveAndFlush(kycDataRequest);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the kycDataRequest
        restKycDataRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, kycDataRequest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return kycDataRequestRepository.count();
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

    protected KycDataRequest getPersistedKycDataRequest(KycDataRequest kycDataRequest) {
        return kycDataRequestRepository.findById(kycDataRequest.getId()).orElseThrow();
    }

    protected void assertPersistedKycDataRequestToMatchAllProperties(KycDataRequest expectedKycDataRequest) {
        assertKycDataRequestAllPropertiesEquals(expectedKycDataRequest, getPersistedKycDataRequest(expectedKycDataRequest));
    }

    protected void assertPersistedKycDataRequestToMatchUpdatableProperties(KycDataRequest expectedKycDataRequest) {
        assertKycDataRequestAllUpdatablePropertiesEquals(expectedKycDataRequest, getPersistedKycDataRequest(expectedKycDataRequest));
    }
}

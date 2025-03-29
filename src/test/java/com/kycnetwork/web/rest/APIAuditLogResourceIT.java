package com.kycnetwork.web.rest;

import static com.kycnetwork.domain.APIAuditLogAsserts.*;
import static com.kycnetwork.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kycnetwork.IntegrationTest;
import com.kycnetwork.domain.APIAuditLog;
import com.kycnetwork.repository.APIAuditLogRepository;
import com.kycnetwork.service.dto.APIAuditLogDTO;
import com.kycnetwork.service.mapper.APIAuditLogMapper;
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
 * Integration tests for the {@link APIAuditLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class APIAuditLogResourceIT {

    private static final Instant DEFAULT_EVENT_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EVENT_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS_CODE = 1;
    private static final Integer UPDATED_STATUS_CODE = 2;

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_IP = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_IP = "BBBBBBBBBB";

    private static final String DEFAULT_INITIATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_INITIATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_ENDPOINT_ACCESSED = "AAAAAAAAAA";
    private static final String UPDATED_ENDPOINT_ACCESSED = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/api-audit-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private APIAuditLogRepository aPIAuditLogRepository;

    @Autowired
    private APIAuditLogMapper aPIAuditLogMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAPIAuditLogMockMvc;

    private APIAuditLog aPIAuditLog;

    private APIAuditLog insertedAPIAuditLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static APIAuditLog createEntity() {
        return new APIAuditLog()
            .eventTime(DEFAULT_EVENT_TIME)
            .action(DEFAULT_ACTION)
            .statusCode(DEFAULT_STATUS_CODE)
            .message(DEFAULT_MESSAGE)
            .sourceIP(DEFAULT_SOURCE_IP)
            .initiatedBy(DEFAULT_INITIATED_BY)
            .endpointAccessed(DEFAULT_ENDPOINT_ACCESSED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static APIAuditLog createUpdatedEntity() {
        return new APIAuditLog()
            .eventTime(UPDATED_EVENT_TIME)
            .action(UPDATED_ACTION)
            .statusCode(UPDATED_STATUS_CODE)
            .message(UPDATED_MESSAGE)
            .sourceIP(UPDATED_SOURCE_IP)
            .initiatedBy(UPDATED_INITIATED_BY)
            .endpointAccessed(UPDATED_ENDPOINT_ACCESSED);
    }

    @BeforeEach
    public void initTest() {
        aPIAuditLog = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAPIAuditLog != null) {
            aPIAuditLogRepository.delete(insertedAPIAuditLog);
            insertedAPIAuditLog = null;
        }
    }

    @Test
    @Transactional
    void createAPIAuditLog() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the APIAuditLog
        APIAuditLogDTO aPIAuditLogDTO = aPIAuditLogMapper.toDto(aPIAuditLog);
        var returnedAPIAuditLogDTO = om.readValue(
            restAPIAuditLogMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aPIAuditLogDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            APIAuditLogDTO.class
        );

        // Validate the APIAuditLog in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAPIAuditLog = aPIAuditLogMapper.toEntity(returnedAPIAuditLogDTO);
        assertAPIAuditLogUpdatableFieldsEquals(returnedAPIAuditLog, getPersistedAPIAuditLog(returnedAPIAuditLog));

        insertedAPIAuditLog = returnedAPIAuditLog;
    }

    @Test
    @Transactional
    void createAPIAuditLogWithExistingId() throws Exception {
        // Create the APIAuditLog with an existing ID
        aPIAuditLog.setId(1L);
        APIAuditLogDTO aPIAuditLogDTO = aPIAuditLogMapper.toDto(aPIAuditLog);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAPIAuditLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aPIAuditLogDTO)))
            .andExpect(status().isBadRequest());

        // Validate the APIAuditLog in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEventTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        aPIAuditLog.setEventTime(null);

        // Create the APIAuditLog, which fails.
        APIAuditLogDTO aPIAuditLogDTO = aPIAuditLogMapper.toDto(aPIAuditLog);

        restAPIAuditLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aPIAuditLogDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        aPIAuditLog.setAction(null);

        // Create the APIAuditLog, which fails.
        APIAuditLogDTO aPIAuditLogDTO = aPIAuditLogMapper.toDto(aPIAuditLog);

        restAPIAuditLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aPIAuditLogDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAPIAuditLogs() throws Exception {
        // Initialize the database
        insertedAPIAuditLog = aPIAuditLogRepository.saveAndFlush(aPIAuditLog);

        // Get all the aPIAuditLogList
        restAPIAuditLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aPIAuditLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventTime").value(hasItem(DEFAULT_EVENT_TIME.toString())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].statusCode").value(hasItem(DEFAULT_STATUS_CODE)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].sourceIP").value(hasItem(DEFAULT_SOURCE_IP)))
            .andExpect(jsonPath("$.[*].initiatedBy").value(hasItem(DEFAULT_INITIATED_BY)))
            .andExpect(jsonPath("$.[*].endpointAccessed").value(hasItem(DEFAULT_ENDPOINT_ACCESSED)));
    }

    @Test
    @Transactional
    void getAPIAuditLog() throws Exception {
        // Initialize the database
        insertedAPIAuditLog = aPIAuditLogRepository.saveAndFlush(aPIAuditLog);

        // Get the aPIAuditLog
        restAPIAuditLogMockMvc
            .perform(get(ENTITY_API_URL_ID, aPIAuditLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aPIAuditLog.getId().intValue()))
            .andExpect(jsonPath("$.eventTime").value(DEFAULT_EVENT_TIME.toString()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION))
            .andExpect(jsonPath("$.statusCode").value(DEFAULT_STATUS_CODE))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.sourceIP").value(DEFAULT_SOURCE_IP))
            .andExpect(jsonPath("$.initiatedBy").value(DEFAULT_INITIATED_BY))
            .andExpect(jsonPath("$.endpointAccessed").value(DEFAULT_ENDPOINT_ACCESSED));
    }

    @Test
    @Transactional
    void getNonExistingAPIAuditLog() throws Exception {
        // Get the aPIAuditLog
        restAPIAuditLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAPIAuditLog() throws Exception {
        // Initialize the database
        insertedAPIAuditLog = aPIAuditLogRepository.saveAndFlush(aPIAuditLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aPIAuditLog
        APIAuditLog updatedAPIAuditLog = aPIAuditLogRepository.findById(aPIAuditLog.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAPIAuditLog are not directly saved in db
        em.detach(updatedAPIAuditLog);
        updatedAPIAuditLog
            .eventTime(UPDATED_EVENT_TIME)
            .action(UPDATED_ACTION)
            .statusCode(UPDATED_STATUS_CODE)
            .message(UPDATED_MESSAGE)
            .sourceIP(UPDATED_SOURCE_IP)
            .initiatedBy(UPDATED_INITIATED_BY)
            .endpointAccessed(UPDATED_ENDPOINT_ACCESSED);
        APIAuditLogDTO aPIAuditLogDTO = aPIAuditLogMapper.toDto(updatedAPIAuditLog);

        restAPIAuditLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aPIAuditLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aPIAuditLogDTO))
            )
            .andExpect(status().isOk());

        // Validate the APIAuditLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAPIAuditLogToMatchAllProperties(updatedAPIAuditLog);
    }

    @Test
    @Transactional
    void putNonExistingAPIAuditLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aPIAuditLog.setId(longCount.incrementAndGet());

        // Create the APIAuditLog
        APIAuditLogDTO aPIAuditLogDTO = aPIAuditLogMapper.toDto(aPIAuditLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAPIAuditLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aPIAuditLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aPIAuditLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the APIAuditLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAPIAuditLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aPIAuditLog.setId(longCount.incrementAndGet());

        // Create the APIAuditLog
        APIAuditLogDTO aPIAuditLogDTO = aPIAuditLogMapper.toDto(aPIAuditLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAPIAuditLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aPIAuditLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the APIAuditLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAPIAuditLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aPIAuditLog.setId(longCount.incrementAndGet());

        // Create the APIAuditLog
        APIAuditLogDTO aPIAuditLogDTO = aPIAuditLogMapper.toDto(aPIAuditLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAPIAuditLogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aPIAuditLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the APIAuditLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAPIAuditLogWithPatch() throws Exception {
        // Initialize the database
        insertedAPIAuditLog = aPIAuditLogRepository.saveAndFlush(aPIAuditLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aPIAuditLog using partial update
        APIAuditLog partialUpdatedAPIAuditLog = new APIAuditLog();
        partialUpdatedAPIAuditLog.setId(aPIAuditLog.getId());

        partialUpdatedAPIAuditLog
            .eventTime(UPDATED_EVENT_TIME)
            .action(UPDATED_ACTION)
            .message(UPDATED_MESSAGE)
            .endpointAccessed(UPDATED_ENDPOINT_ACCESSED);

        restAPIAuditLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAPIAuditLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAPIAuditLog))
            )
            .andExpect(status().isOk());

        // Validate the APIAuditLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAPIAuditLogUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAPIAuditLog, aPIAuditLog),
            getPersistedAPIAuditLog(aPIAuditLog)
        );
    }

    @Test
    @Transactional
    void fullUpdateAPIAuditLogWithPatch() throws Exception {
        // Initialize the database
        insertedAPIAuditLog = aPIAuditLogRepository.saveAndFlush(aPIAuditLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aPIAuditLog using partial update
        APIAuditLog partialUpdatedAPIAuditLog = new APIAuditLog();
        partialUpdatedAPIAuditLog.setId(aPIAuditLog.getId());

        partialUpdatedAPIAuditLog
            .eventTime(UPDATED_EVENT_TIME)
            .action(UPDATED_ACTION)
            .statusCode(UPDATED_STATUS_CODE)
            .message(UPDATED_MESSAGE)
            .sourceIP(UPDATED_SOURCE_IP)
            .initiatedBy(UPDATED_INITIATED_BY)
            .endpointAccessed(UPDATED_ENDPOINT_ACCESSED);

        restAPIAuditLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAPIAuditLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAPIAuditLog))
            )
            .andExpect(status().isOk());

        // Validate the APIAuditLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAPIAuditLogUpdatableFieldsEquals(partialUpdatedAPIAuditLog, getPersistedAPIAuditLog(partialUpdatedAPIAuditLog));
    }

    @Test
    @Transactional
    void patchNonExistingAPIAuditLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aPIAuditLog.setId(longCount.incrementAndGet());

        // Create the APIAuditLog
        APIAuditLogDTO aPIAuditLogDTO = aPIAuditLogMapper.toDto(aPIAuditLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAPIAuditLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aPIAuditLogDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aPIAuditLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the APIAuditLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAPIAuditLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aPIAuditLog.setId(longCount.incrementAndGet());

        // Create the APIAuditLog
        APIAuditLogDTO aPIAuditLogDTO = aPIAuditLogMapper.toDto(aPIAuditLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAPIAuditLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aPIAuditLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the APIAuditLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAPIAuditLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aPIAuditLog.setId(longCount.incrementAndGet());

        // Create the APIAuditLog
        APIAuditLogDTO aPIAuditLogDTO = aPIAuditLogMapper.toDto(aPIAuditLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAPIAuditLogMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(aPIAuditLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the APIAuditLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAPIAuditLog() throws Exception {
        // Initialize the database
        insertedAPIAuditLog = aPIAuditLogRepository.saveAndFlush(aPIAuditLog);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the aPIAuditLog
        restAPIAuditLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, aPIAuditLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return aPIAuditLogRepository.count();
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

    protected APIAuditLog getPersistedAPIAuditLog(APIAuditLog aPIAuditLog) {
        return aPIAuditLogRepository.findById(aPIAuditLog.getId()).orElseThrow();
    }

    protected void assertPersistedAPIAuditLogToMatchAllProperties(APIAuditLog expectedAPIAuditLog) {
        assertAPIAuditLogAllPropertiesEquals(expectedAPIAuditLog, getPersistedAPIAuditLog(expectedAPIAuditLog));
    }

    protected void assertPersistedAPIAuditLogToMatchUpdatableProperties(APIAuditLog expectedAPIAuditLog) {
        assertAPIAuditLogAllUpdatablePropertiesEquals(expectedAPIAuditLog, getPersistedAPIAuditLog(expectedAPIAuditLog));
    }
}

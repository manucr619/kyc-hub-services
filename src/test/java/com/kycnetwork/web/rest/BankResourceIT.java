package com.kycnetwork.web.rest;

import static com.kycnetwork.domain.BankAsserts.*;
import static com.kycnetwork.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kycnetwork.IntegrationTest;
import com.kycnetwork.domain.Bank;
import com.kycnetwork.repository.BankRepository;
import com.kycnetwork.service.dto.BankDTO;
import com.kycnetwork.service.mapper.BankMapper;
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
 * Integration tests for the {@link BankResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BankResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BIC_CODE = "AAAAAAAAAA";
    private static final String UPDATED_BIC_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/banks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private BankMapper bankMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBankMockMvc;

    private Bank bank;

    private Bank insertedBank;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bank createEntity() {
        return new Bank().name(DEFAULT_NAME).bicCode(DEFAULT_BIC_CODE).country(DEFAULT_COUNTRY).isActive(DEFAULT_IS_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bank createUpdatedEntity() {
        return new Bank().name(UPDATED_NAME).bicCode(UPDATED_BIC_CODE).country(UPDATED_COUNTRY).isActive(UPDATED_IS_ACTIVE);
    }

    @BeforeEach
    public void initTest() {
        bank = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedBank != null) {
            bankRepository.delete(insertedBank);
            insertedBank = null;
        }
    }

    @Test
    @Transactional
    void createBank() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Bank
        BankDTO bankDTO = bankMapper.toDto(bank);
        var returnedBankDTO = om.readValue(
            restBankMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BankDTO.class
        );

        // Validate the Bank in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBank = bankMapper.toEntity(returnedBankDTO);
        assertBankUpdatableFieldsEquals(returnedBank, getPersistedBank(returnedBank));

        insertedBank = returnedBank;
    }

    @Test
    @Transactional
    void createBankWithExistingId() throws Exception {
        // Create the Bank with an existing ID
        bank.setId(1L);
        BankDTO bankDTO = bankMapper.toDto(bank);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBankMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Bank in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bank.setName(null);

        // Create the Bank, which fails.
        BankDTO bankDTO = bankMapper.toDto(bank);

        restBankMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBicCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bank.setBicCode(null);

        // Create the Bank, which fails.
        BankDTO bankDTO = bankMapper.toDto(bank);

        restBankMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bank.setCountry(null);

        // Create the Bank, which fails.
        BankDTO bankDTO = bankMapper.toDto(bank);

        restBankMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBanks() throws Exception {
        // Initialize the database
        insertedBank = bankRepository.saveAndFlush(bank);

        // Get all the bankList
        restBankMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bank.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].bicCode").value(hasItem(DEFAULT_BIC_CODE)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)));
    }

    @Test
    @Transactional
    void getBank() throws Exception {
        // Initialize the database
        insertedBank = bankRepository.saveAndFlush(bank);

        // Get the bank
        restBankMockMvc
            .perform(get(ENTITY_API_URL_ID, bank.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bank.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.bicCode").value(DEFAULT_BIC_CODE))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE));
    }

    @Test
    @Transactional
    void getNonExistingBank() throws Exception {
        // Get the bank
        restBankMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBank() throws Exception {
        // Initialize the database
        insertedBank = bankRepository.saveAndFlush(bank);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bank
        Bank updatedBank = bankRepository.findById(bank.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBank are not directly saved in db
        em.detach(updatedBank);
        updatedBank.name(UPDATED_NAME).bicCode(UPDATED_BIC_CODE).country(UPDATED_COUNTRY).isActive(UPDATED_IS_ACTIVE);
        BankDTO bankDTO = bankMapper.toDto(updatedBank);

        restBankMockMvc
            .perform(put(ENTITY_API_URL_ID, bankDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankDTO)))
            .andExpect(status().isOk());

        // Validate the Bank in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBankToMatchAllProperties(updatedBank);
    }

    @Test
    @Transactional
    void putNonExistingBank() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bank.setId(longCount.incrementAndGet());

        // Create the Bank
        BankDTO bankDTO = bankMapper.toDto(bank);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBankMockMvc
            .perform(put(ENTITY_API_URL_ID, bankDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Bank in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBank() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bank.setId(longCount.incrementAndGet());

        // Create the Bank
        BankDTO bankDTO = bankMapper.toDto(bank);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bankDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bank in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBank() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bank.setId(longCount.incrementAndGet());

        // Create the Bank
        BankDTO bankDTO = bankMapper.toDto(bank);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bank in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBankWithPatch() throws Exception {
        // Initialize the database
        insertedBank = bankRepository.saveAndFlush(bank);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bank using partial update
        Bank partialUpdatedBank = new Bank();
        partialUpdatedBank.setId(bank.getId());

        partialUpdatedBank.country(UPDATED_COUNTRY).isActive(UPDATED_IS_ACTIVE);

        restBankMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBank.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBank))
            )
            .andExpect(status().isOk());

        // Validate the Bank in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBankUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedBank, bank), getPersistedBank(bank));
    }

    @Test
    @Transactional
    void fullUpdateBankWithPatch() throws Exception {
        // Initialize the database
        insertedBank = bankRepository.saveAndFlush(bank);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bank using partial update
        Bank partialUpdatedBank = new Bank();
        partialUpdatedBank.setId(bank.getId());

        partialUpdatedBank.name(UPDATED_NAME).bicCode(UPDATED_BIC_CODE).country(UPDATED_COUNTRY).isActive(UPDATED_IS_ACTIVE);

        restBankMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBank.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBank))
            )
            .andExpect(status().isOk());

        // Validate the Bank in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBankUpdatableFieldsEquals(partialUpdatedBank, getPersistedBank(partialUpdatedBank));
    }

    @Test
    @Transactional
    void patchNonExistingBank() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bank.setId(longCount.incrementAndGet());

        // Create the Bank
        BankDTO bankDTO = bankMapper.toDto(bank);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBankMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bankDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bankDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bank in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBank() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bank.setId(longCount.incrementAndGet());

        // Create the Bank
        BankDTO bankDTO = bankMapper.toDto(bank);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bankDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bank in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBank() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bank.setId(longCount.incrementAndGet());

        // Create the Bank
        BankDTO bankDTO = bankMapper.toDto(bank);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bankDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bank in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBank() throws Exception {
        // Initialize the database
        insertedBank = bankRepository.saveAndFlush(bank);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the bank
        restBankMockMvc
            .perform(delete(ENTITY_API_URL_ID, bank.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bankRepository.count();
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

    protected Bank getPersistedBank(Bank bank) {
        return bankRepository.findById(bank.getId()).orElseThrow();
    }

    protected void assertPersistedBankToMatchAllProperties(Bank expectedBank) {
        assertBankAllPropertiesEquals(expectedBank, getPersistedBank(expectedBank));
    }

    protected void assertPersistedBankToMatchUpdatableProperties(Bank expectedBank) {
        assertBankAllUpdatablePropertiesEquals(expectedBank, getPersistedBank(expectedBank));
    }
}

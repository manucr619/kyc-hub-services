package com.kycnetwork.service;

import com.kycnetwork.domain.KycConsent;
import com.kycnetwork.repository.KycConsentRepository;
import com.kycnetwork.service.dto.KycConsentDTO;
import com.kycnetwork.service.mapper.KycConsentMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kycnetwork.domain.KycConsent}.
 */
@Service
@Transactional
public class KycConsentService {

    private static final Logger LOG = LoggerFactory.getLogger(KycConsentService.class);

    private final KycConsentRepository kycConsentRepository;

    private final KycConsentMapper kycConsentMapper;

    public KycConsentService(KycConsentRepository kycConsentRepository, KycConsentMapper kycConsentMapper) {
        this.kycConsentRepository = kycConsentRepository;
        this.kycConsentMapper = kycConsentMapper;
    }

    /**
     * Save a kycConsent.
     *
     * @param kycConsentDTO the entity to save.
     * @return the persisted entity.
     */
    public KycConsentDTO save(KycConsentDTO kycConsentDTO) {
        LOG.debug("Request to save KycConsent : {}", kycConsentDTO);
        KycConsent kycConsent = kycConsentMapper.toEntity(kycConsentDTO);
        kycConsent = kycConsentRepository.save(kycConsent);
        return kycConsentMapper.toDto(kycConsent);
    }

    /**
     * Update a kycConsent.
     *
     * @param kycConsentDTO the entity to save.
     * @return the persisted entity.
     */
    public KycConsentDTO update(KycConsentDTO kycConsentDTO) {
        LOG.debug("Request to update KycConsent : {}", kycConsentDTO);
        KycConsent kycConsent = kycConsentMapper.toEntity(kycConsentDTO);
        kycConsent = kycConsentRepository.save(kycConsent);
        return kycConsentMapper.toDto(kycConsent);
    }

    /**
     * Partially update a kycConsent.
     *
     * @param kycConsentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<KycConsentDTO> partialUpdate(KycConsentDTO kycConsentDTO) {
        LOG.debug("Request to partially update KycConsent : {}", kycConsentDTO);

        return kycConsentRepository
            .findById(kycConsentDTO.getId())
            .map(existingKycConsent -> {
                kycConsentMapper.partialUpdate(existingKycConsent, kycConsentDTO);

                return existingKycConsent;
            })
            .map(kycConsentRepository::save)
            .map(kycConsentMapper::toDto);
    }

    /**
     * Get all the kycConsents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<KycConsentDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all KycConsents");
        return kycConsentRepository.findAll(pageable).map(kycConsentMapper::toDto);
    }

    /**
     * Get one kycConsent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<KycConsentDTO> findOne(Long id) {
        LOG.debug("Request to get KycConsent : {}", id);
        return kycConsentRepository.findById(id).map(kycConsentMapper::toDto);
    }

    /**
     * Delete the kycConsent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete KycConsent : {}", id);
        kycConsentRepository.deleteById(id);
    }
}

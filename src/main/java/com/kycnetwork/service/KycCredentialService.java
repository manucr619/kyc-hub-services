package com.kycnetwork.service;

import com.kycnetwork.domain.KycCredential;
import com.kycnetwork.repository.KycCredentialRepository;
import com.kycnetwork.service.dto.KycCredentialDTO;
import com.kycnetwork.service.mapper.KycCredentialMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kycnetwork.domain.KycCredential}.
 */
@Service
@Transactional
public class KycCredentialService {

    private static final Logger LOG = LoggerFactory.getLogger(KycCredentialService.class);

    private final KycCredentialRepository kycCredentialRepository;

    private final KycCredentialMapper kycCredentialMapper;

    public KycCredentialService(KycCredentialRepository kycCredentialRepository, KycCredentialMapper kycCredentialMapper) {
        this.kycCredentialRepository = kycCredentialRepository;
        this.kycCredentialMapper = kycCredentialMapper;
    }

    /**
     * Save a kycCredential.
     *
     * @param kycCredentialDTO the entity to save.
     * @return the persisted entity.
     */
    public KycCredentialDTO save(KycCredentialDTO kycCredentialDTO) {
        LOG.debug("Request to save KycCredential : {}", kycCredentialDTO);
        KycCredential kycCredential = kycCredentialMapper.toEntity(kycCredentialDTO);
        kycCredential = kycCredentialRepository.save(kycCredential);
        return kycCredentialMapper.toDto(kycCredential);
    }

    /**
     * Update a kycCredential.
     *
     * @param kycCredentialDTO the entity to save.
     * @return the persisted entity.
     */
    public KycCredentialDTO update(KycCredentialDTO kycCredentialDTO) {
        LOG.debug("Request to update KycCredential : {}", kycCredentialDTO);
        KycCredential kycCredential = kycCredentialMapper.toEntity(kycCredentialDTO);
        kycCredential = kycCredentialRepository.save(kycCredential);
        return kycCredentialMapper.toDto(kycCredential);
    }

    /**
     * Partially update a kycCredential.
     *
     * @param kycCredentialDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<KycCredentialDTO> partialUpdate(KycCredentialDTO kycCredentialDTO) {
        LOG.debug("Request to partially update KycCredential : {}", kycCredentialDTO);

        return kycCredentialRepository
            .findById(kycCredentialDTO.getId())
            .map(existingKycCredential -> {
                kycCredentialMapper.partialUpdate(existingKycCredential, kycCredentialDTO);

                return existingKycCredential;
            })
            .map(kycCredentialRepository::save)
            .map(kycCredentialMapper::toDto);
    }

    /**
     * Get all the kycCredentials.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<KycCredentialDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all KycCredentials");
        return kycCredentialRepository.findAll(pageable).map(kycCredentialMapper::toDto);
    }

    /**
     * Get one kycCredential by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<KycCredentialDTO> findOne(Long id) {
        LOG.debug("Request to get KycCredential : {}", id);
        return kycCredentialRepository.findById(id).map(kycCredentialMapper::toDto);
    }

    /**
     * Delete the kycCredential by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete KycCredential : {}", id);
        kycCredentialRepository.deleteById(id);
    }
}

package com.kycnetwork.service;

import com.kycnetwork.domain.KycDataRequest;
import com.kycnetwork.repository.KycDataRequestRepository;
import com.kycnetwork.service.dto.KycDataRequestDTO;
import com.kycnetwork.service.mapper.KycDataRequestMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kycnetwork.domain.KycDataRequest}.
 */
@Service
@Transactional
public class KycDataRequestService {

    private static final Logger LOG = LoggerFactory.getLogger(KycDataRequestService.class);

    private final KycDataRequestRepository kycDataRequestRepository;

    private final KycDataRequestMapper kycDataRequestMapper;

    public KycDataRequestService(KycDataRequestRepository kycDataRequestRepository, KycDataRequestMapper kycDataRequestMapper) {
        this.kycDataRequestRepository = kycDataRequestRepository;
        this.kycDataRequestMapper = kycDataRequestMapper;
    }

    /**
     * Save a kycDataRequest.
     *
     * @param kycDataRequestDTO the entity to save.
     * @return the persisted entity.
     */
    public KycDataRequestDTO save(KycDataRequestDTO kycDataRequestDTO) {
        LOG.debug("Request to save KycDataRequest : {}", kycDataRequestDTO);
        KycDataRequest kycDataRequest = kycDataRequestMapper.toEntity(kycDataRequestDTO);
        kycDataRequest = kycDataRequestRepository.save(kycDataRequest);
        return kycDataRequestMapper.toDto(kycDataRequest);
    }

    /**
     * Update a kycDataRequest.
     *
     * @param kycDataRequestDTO the entity to save.
     * @return the persisted entity.
     */
    public KycDataRequestDTO update(KycDataRequestDTO kycDataRequestDTO) {
        LOG.debug("Request to update KycDataRequest : {}", kycDataRequestDTO);
        KycDataRequest kycDataRequest = kycDataRequestMapper.toEntity(kycDataRequestDTO);
        kycDataRequest = kycDataRequestRepository.save(kycDataRequest);
        return kycDataRequestMapper.toDto(kycDataRequest);
    }

    /**
     * Partially update a kycDataRequest.
     *
     * @param kycDataRequestDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<KycDataRequestDTO> partialUpdate(KycDataRequestDTO kycDataRequestDTO) {
        LOG.debug("Request to partially update KycDataRequest : {}", kycDataRequestDTO);

        return kycDataRequestRepository
            .findById(kycDataRequestDTO.getId())
            .map(existingKycDataRequest -> {
                kycDataRequestMapper.partialUpdate(existingKycDataRequest, kycDataRequestDTO);

                return existingKycDataRequest;
            })
            .map(kycDataRequestRepository::save)
            .map(kycDataRequestMapper::toDto);
    }

    /**
     * Get all the kycDataRequests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<KycDataRequestDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all KycDataRequests");
        return kycDataRequestRepository.findAll(pageable).map(kycDataRequestMapper::toDto);
    }

    /**
     * Get one kycDataRequest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<KycDataRequestDTO> findOne(Long id) {
        LOG.debug("Request to get KycDataRequest : {}", id);
        return kycDataRequestRepository.findById(id).map(kycDataRequestMapper::toDto);
    }

    /**
     * Delete the kycDataRequest by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete KycDataRequest : {}", id);
        kycDataRequestRepository.deleteById(id);
    }
}

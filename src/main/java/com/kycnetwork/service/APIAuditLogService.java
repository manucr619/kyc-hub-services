package com.kycnetwork.service;

import com.kycnetwork.domain.APIAuditLog;
import com.kycnetwork.repository.APIAuditLogRepository;
import com.kycnetwork.service.dto.APIAuditLogDTO;
import com.kycnetwork.service.mapper.APIAuditLogMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kycnetwork.domain.APIAuditLog}.
 */
@Service
@Transactional
public class APIAuditLogService {

    private static final Logger LOG = LoggerFactory.getLogger(APIAuditLogService.class);

    private final APIAuditLogRepository aPIAuditLogRepository;

    private final APIAuditLogMapper aPIAuditLogMapper;

    public APIAuditLogService(APIAuditLogRepository aPIAuditLogRepository, APIAuditLogMapper aPIAuditLogMapper) {
        this.aPIAuditLogRepository = aPIAuditLogRepository;
        this.aPIAuditLogMapper = aPIAuditLogMapper;
    }

    /**
     * Save a aPIAuditLog.
     *
     * @param aPIAuditLogDTO the entity to save.
     * @return the persisted entity.
     */
    public APIAuditLogDTO save(APIAuditLogDTO aPIAuditLogDTO) {
        LOG.debug("Request to save APIAuditLog : {}", aPIAuditLogDTO);
        APIAuditLog aPIAuditLog = aPIAuditLogMapper.toEntity(aPIAuditLogDTO);
        aPIAuditLog = aPIAuditLogRepository.save(aPIAuditLog);
        return aPIAuditLogMapper.toDto(aPIAuditLog);
    }

    /**
     * Update a aPIAuditLog.
     *
     * @param aPIAuditLogDTO the entity to save.
     * @return the persisted entity.
     */
    public APIAuditLogDTO update(APIAuditLogDTO aPIAuditLogDTO) {
        LOG.debug("Request to update APIAuditLog : {}", aPIAuditLogDTO);
        APIAuditLog aPIAuditLog = aPIAuditLogMapper.toEntity(aPIAuditLogDTO);
        aPIAuditLog = aPIAuditLogRepository.save(aPIAuditLog);
        return aPIAuditLogMapper.toDto(aPIAuditLog);
    }

    /**
     * Partially update a aPIAuditLog.
     *
     * @param aPIAuditLogDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<APIAuditLogDTO> partialUpdate(APIAuditLogDTO aPIAuditLogDTO) {
        LOG.debug("Request to partially update APIAuditLog : {}", aPIAuditLogDTO);

        return aPIAuditLogRepository
            .findById(aPIAuditLogDTO.getId())
            .map(existingAPIAuditLog -> {
                aPIAuditLogMapper.partialUpdate(existingAPIAuditLog, aPIAuditLogDTO);

                return existingAPIAuditLog;
            })
            .map(aPIAuditLogRepository::save)
            .map(aPIAuditLogMapper::toDto);
    }

    /**
     * Get all the aPIAuditLogs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<APIAuditLogDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all APIAuditLogs");
        return aPIAuditLogRepository.findAll(pageable).map(aPIAuditLogMapper::toDto);
    }

    /**
     * Get one aPIAuditLog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<APIAuditLogDTO> findOne(Long id) {
        LOG.debug("Request to get APIAuditLog : {}", id);
        return aPIAuditLogRepository.findById(id).map(aPIAuditLogMapper::toDto);
    }

    /**
     * Delete the aPIAuditLog by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete APIAuditLog : {}", id);
        aPIAuditLogRepository.deleteById(id);
    }
}

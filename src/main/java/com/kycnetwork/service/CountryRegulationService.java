package com.kycnetwork.service;

import com.kycnetwork.domain.CountryRegulation;
import com.kycnetwork.repository.CountryRegulationRepository;
import com.kycnetwork.service.dto.CountryRegulationDTO;
import com.kycnetwork.service.mapper.CountryRegulationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.kycnetwork.domain.CountryRegulation}.
 */
@Service
@Transactional
public class CountryRegulationService {

    private static final Logger LOG = LoggerFactory.getLogger(CountryRegulationService.class);

    private final CountryRegulationRepository countryRegulationRepository;

    private final CountryRegulationMapper countryRegulationMapper;

    public CountryRegulationService(
        CountryRegulationRepository countryRegulationRepository,
        CountryRegulationMapper countryRegulationMapper
    ) {
        this.countryRegulationRepository = countryRegulationRepository;
        this.countryRegulationMapper = countryRegulationMapper;
    }

    /**
     * Save a countryRegulation.
     *
     * @param countryRegulationDTO the entity to save.
     * @return the persisted entity.
     */
    public CountryRegulationDTO save(CountryRegulationDTO countryRegulationDTO) {
        LOG.debug("Request to save CountryRegulation : {}", countryRegulationDTO);
        CountryRegulation countryRegulation = countryRegulationMapper.toEntity(countryRegulationDTO);
        countryRegulation = countryRegulationRepository.save(countryRegulation);
        return countryRegulationMapper.toDto(countryRegulation);
    }

    /**
     * Update a countryRegulation.
     *
     * @param countryRegulationDTO the entity to save.
     * @return the persisted entity.
     */
    public CountryRegulationDTO update(CountryRegulationDTO countryRegulationDTO) {
        LOG.debug("Request to update CountryRegulation : {}", countryRegulationDTO);
        CountryRegulation countryRegulation = countryRegulationMapper.toEntity(countryRegulationDTO);
        countryRegulation = countryRegulationRepository.save(countryRegulation);
        return countryRegulationMapper.toDto(countryRegulation);
    }

    /**
     * Partially update a countryRegulation.
     *
     * @param countryRegulationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CountryRegulationDTO> partialUpdate(CountryRegulationDTO countryRegulationDTO) {
        LOG.debug("Request to partially update CountryRegulation : {}", countryRegulationDTO);

        return countryRegulationRepository
            .findById(countryRegulationDTO.getId())
            .map(existingCountryRegulation -> {
                countryRegulationMapper.partialUpdate(existingCountryRegulation, countryRegulationDTO);

                return existingCountryRegulation;
            })
            .map(countryRegulationRepository::save)
            .map(countryRegulationMapper::toDto);
    }

    /**
     * Get all the countryRegulations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CountryRegulationDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all CountryRegulations");
        return countryRegulationRepository.findAll(pageable).map(countryRegulationMapper::toDto);
    }

    /**
     * Get one countryRegulation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CountryRegulationDTO> findOne(Long id) {
        LOG.debug("Request to get CountryRegulation : {}", id);
        return countryRegulationRepository.findById(id).map(countryRegulationMapper::toDto);
    }

    /**
     * Delete the countryRegulation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete CountryRegulation : {}", id);
        countryRegulationRepository.deleteById(id);
    }
}

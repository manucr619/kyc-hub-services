package com.kycnetwork.web.rest;

import com.kycnetwork.repository.CountryRegulationRepository;
import com.kycnetwork.service.CountryRegulationService;
import com.kycnetwork.service.dto.CountryRegulationDTO;
import com.kycnetwork.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.kycnetwork.domain.CountryRegulation}.
 */
@RestController
@RequestMapping("/api/country-regulations")
public class CountryRegulationResource {

    private static final Logger LOG = LoggerFactory.getLogger(CountryRegulationResource.class);

    private static final String ENTITY_NAME = "countryRegulation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CountryRegulationService countryRegulationService;

    private final CountryRegulationRepository countryRegulationRepository;

    public CountryRegulationResource(
        CountryRegulationService countryRegulationService,
        CountryRegulationRepository countryRegulationRepository
    ) {
        this.countryRegulationService = countryRegulationService;
        this.countryRegulationRepository = countryRegulationRepository;
    }

    /**
     * {@code POST  /country-regulations} : Create a new countryRegulation.
     *
     * @param countryRegulationDTO the countryRegulationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new countryRegulationDTO, or with status {@code 400 (Bad Request)} if the countryRegulation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CountryRegulationDTO> createCountryRegulation(@Valid @RequestBody CountryRegulationDTO countryRegulationDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save CountryRegulation : {}", countryRegulationDTO);
        if (countryRegulationDTO.getId() != null) {
            throw new BadRequestAlertException("A new countryRegulation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        countryRegulationDTO = countryRegulationService.save(countryRegulationDTO);
        return ResponseEntity.created(new URI("/api/country-regulations/" + countryRegulationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, countryRegulationDTO.getId().toString()))
            .body(countryRegulationDTO);
    }

    /**
     * {@code PUT  /country-regulations/:id} : Updates an existing countryRegulation.
     *
     * @param id the id of the countryRegulationDTO to save.
     * @param countryRegulationDTO the countryRegulationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated countryRegulationDTO,
     * or with status {@code 400 (Bad Request)} if the countryRegulationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the countryRegulationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CountryRegulationDTO> updateCountryRegulation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CountryRegulationDTO countryRegulationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update CountryRegulation : {}, {}", id, countryRegulationDTO);
        if (countryRegulationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, countryRegulationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!countryRegulationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        countryRegulationDTO = countryRegulationService.update(countryRegulationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, countryRegulationDTO.getId().toString()))
            .body(countryRegulationDTO);
    }

    /**
     * {@code PATCH  /country-regulations/:id} : Partial updates given fields of an existing countryRegulation, field will ignore if it is null
     *
     * @param id the id of the countryRegulationDTO to save.
     * @param countryRegulationDTO the countryRegulationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated countryRegulationDTO,
     * or with status {@code 400 (Bad Request)} if the countryRegulationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the countryRegulationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the countryRegulationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CountryRegulationDTO> partialUpdateCountryRegulation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CountryRegulationDTO countryRegulationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update CountryRegulation partially : {}, {}", id, countryRegulationDTO);
        if (countryRegulationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, countryRegulationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!countryRegulationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CountryRegulationDTO> result = countryRegulationService.partialUpdate(countryRegulationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, countryRegulationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /country-regulations} : get all the countryRegulations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of countryRegulations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CountryRegulationDTO>> getAllCountryRegulations(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of CountryRegulations");
        Page<CountryRegulationDTO> page = countryRegulationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /country-regulations/:id} : get the "id" countryRegulation.
     *
     * @param id the id of the countryRegulationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the countryRegulationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CountryRegulationDTO> getCountryRegulation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get CountryRegulation : {}", id);
        Optional<CountryRegulationDTO> countryRegulationDTO = countryRegulationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(countryRegulationDTO);
    }

    /**
     * {@code DELETE  /country-regulations/:id} : delete the "id" countryRegulation.
     *
     * @param id the id of the countryRegulationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCountryRegulation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete CountryRegulation : {}", id);
        countryRegulationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

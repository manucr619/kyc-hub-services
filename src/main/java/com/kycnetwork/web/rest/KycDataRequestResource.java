package com.kycnetwork.web.rest;

import com.kycnetwork.repository.KycDataRequestRepository;
import com.kycnetwork.service.KycDataRequestService;
import com.kycnetwork.service.dto.KycDataRequestDTO;
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
 * REST controller for managing {@link com.kycnetwork.domain.KycDataRequest}.
 */
@RestController
@RequestMapping("/api/kyc-data-requests")
public class KycDataRequestResource {

    private static final Logger LOG = LoggerFactory.getLogger(KycDataRequestResource.class);

    private static final String ENTITY_NAME = "kycDataRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final KycDataRequestService kycDataRequestService;

    private final KycDataRequestRepository kycDataRequestRepository;

    public KycDataRequestResource(KycDataRequestService kycDataRequestService, KycDataRequestRepository kycDataRequestRepository) {
        this.kycDataRequestService = kycDataRequestService;
        this.kycDataRequestRepository = kycDataRequestRepository;
    }

    /**
     * {@code POST  /kyc-data-requests} : Create a new kycDataRequest.
     *
     * @param kycDataRequestDTO the kycDataRequestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new kycDataRequestDTO, or with status {@code 400 (Bad Request)} if the kycDataRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<KycDataRequestDTO> createKycDataRequest(@Valid @RequestBody KycDataRequestDTO kycDataRequestDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save KycDataRequest : {}", kycDataRequestDTO);
        if (kycDataRequestDTO.getId() != null) {
            throw new BadRequestAlertException("A new kycDataRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        kycDataRequestDTO = kycDataRequestService.save(kycDataRequestDTO);
        return ResponseEntity.created(new URI("/api/kyc-data-requests/" + kycDataRequestDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, kycDataRequestDTO.getId().toString()))
            .body(kycDataRequestDTO);
    }

    /**
     * {@code PUT  /kyc-data-requests/:id} : Updates an existing kycDataRequest.
     *
     * @param id the id of the kycDataRequestDTO to save.
     * @param kycDataRequestDTO the kycDataRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kycDataRequestDTO,
     * or with status {@code 400 (Bad Request)} if the kycDataRequestDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the kycDataRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<KycDataRequestDTO> updateKycDataRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody KycDataRequestDTO kycDataRequestDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update KycDataRequest : {}, {}", id, kycDataRequestDTO);
        if (kycDataRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, kycDataRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!kycDataRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        kycDataRequestDTO = kycDataRequestService.update(kycDataRequestDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, kycDataRequestDTO.getId().toString()))
            .body(kycDataRequestDTO);
    }

    /**
     * {@code PATCH  /kyc-data-requests/:id} : Partial updates given fields of an existing kycDataRequest, field will ignore if it is null
     *
     * @param id the id of the kycDataRequestDTO to save.
     * @param kycDataRequestDTO the kycDataRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kycDataRequestDTO,
     * or with status {@code 400 (Bad Request)} if the kycDataRequestDTO is not valid,
     * or with status {@code 404 (Not Found)} if the kycDataRequestDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the kycDataRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<KycDataRequestDTO> partialUpdateKycDataRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody KycDataRequestDTO kycDataRequestDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update KycDataRequest partially : {}, {}", id, kycDataRequestDTO);
        if (kycDataRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, kycDataRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!kycDataRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<KycDataRequestDTO> result = kycDataRequestService.partialUpdate(kycDataRequestDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, kycDataRequestDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /kyc-data-requests} : get all the kycDataRequests.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of kycDataRequests in body.
     */
    @GetMapping("")
    public ResponseEntity<List<KycDataRequestDTO>> getAllKycDataRequests(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of KycDataRequests");
        Page<KycDataRequestDTO> page = kycDataRequestService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /kyc-data-requests/:id} : get the "id" kycDataRequest.
     *
     * @param id the id of the kycDataRequestDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the kycDataRequestDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<KycDataRequestDTO> getKycDataRequest(@PathVariable("id") Long id) {
        LOG.debug("REST request to get KycDataRequest : {}", id);
        Optional<KycDataRequestDTO> kycDataRequestDTO = kycDataRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(kycDataRequestDTO);
    }

    /**
     * {@code DELETE  /kyc-data-requests/:id} : delete the "id" kycDataRequest.
     *
     * @param id the id of the kycDataRequestDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKycDataRequest(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete KycDataRequest : {}", id);
        kycDataRequestService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

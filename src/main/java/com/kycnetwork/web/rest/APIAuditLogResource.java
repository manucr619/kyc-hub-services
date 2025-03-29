package com.kycnetwork.web.rest;

import com.kycnetwork.repository.APIAuditLogRepository;
import com.kycnetwork.service.APIAuditLogService;
import com.kycnetwork.service.dto.APIAuditLogDTO;
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
 * REST controller for managing {@link com.kycnetwork.domain.APIAuditLog}.
 */
@RestController
@RequestMapping("/api/api-audit-logs")
public class APIAuditLogResource {

    private static final Logger LOG = LoggerFactory.getLogger(APIAuditLogResource.class);

    private static final String ENTITY_NAME = "aPIAuditLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final APIAuditLogService aPIAuditLogService;

    private final APIAuditLogRepository aPIAuditLogRepository;

    public APIAuditLogResource(APIAuditLogService aPIAuditLogService, APIAuditLogRepository aPIAuditLogRepository) {
        this.aPIAuditLogService = aPIAuditLogService;
        this.aPIAuditLogRepository = aPIAuditLogRepository;
    }

    /**
     * {@code POST  /api-audit-logs} : Create a new aPIAuditLog.
     *
     * @param aPIAuditLogDTO the aPIAuditLogDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aPIAuditLogDTO, or with status {@code 400 (Bad Request)} if the aPIAuditLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<APIAuditLogDTO> createAPIAuditLog(@Valid @RequestBody APIAuditLogDTO aPIAuditLogDTO) throws URISyntaxException {
        LOG.debug("REST request to save APIAuditLog : {}", aPIAuditLogDTO);
        if (aPIAuditLogDTO.getId() != null) {
            throw new BadRequestAlertException("A new aPIAuditLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        aPIAuditLogDTO = aPIAuditLogService.save(aPIAuditLogDTO);
        return ResponseEntity.created(new URI("/api/api-audit-logs/" + aPIAuditLogDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, aPIAuditLogDTO.getId().toString()))
            .body(aPIAuditLogDTO);
    }

    /**
     * {@code PUT  /api-audit-logs/:id} : Updates an existing aPIAuditLog.
     *
     * @param id the id of the aPIAuditLogDTO to save.
     * @param aPIAuditLogDTO the aPIAuditLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aPIAuditLogDTO,
     * or with status {@code 400 (Bad Request)} if the aPIAuditLogDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aPIAuditLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<APIAuditLogDTO> updateAPIAuditLog(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody APIAuditLogDTO aPIAuditLogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update APIAuditLog : {}, {}", id, aPIAuditLogDTO);
        if (aPIAuditLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aPIAuditLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aPIAuditLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        aPIAuditLogDTO = aPIAuditLogService.update(aPIAuditLogDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aPIAuditLogDTO.getId().toString()))
            .body(aPIAuditLogDTO);
    }

    /**
     * {@code PATCH  /api-audit-logs/:id} : Partial updates given fields of an existing aPIAuditLog, field will ignore if it is null
     *
     * @param id the id of the aPIAuditLogDTO to save.
     * @param aPIAuditLogDTO the aPIAuditLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aPIAuditLogDTO,
     * or with status {@code 400 (Bad Request)} if the aPIAuditLogDTO is not valid,
     * or with status {@code 404 (Not Found)} if the aPIAuditLogDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the aPIAuditLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<APIAuditLogDTO> partialUpdateAPIAuditLog(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody APIAuditLogDTO aPIAuditLogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update APIAuditLog partially : {}, {}", id, aPIAuditLogDTO);
        if (aPIAuditLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aPIAuditLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aPIAuditLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<APIAuditLogDTO> result = aPIAuditLogService.partialUpdate(aPIAuditLogDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aPIAuditLogDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /api-audit-logs} : get all the aPIAuditLogs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aPIAuditLogs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<APIAuditLogDTO>> getAllAPIAuditLogs(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of APIAuditLogs");
        Page<APIAuditLogDTO> page = aPIAuditLogService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /api-audit-logs/:id} : get the "id" aPIAuditLog.
     *
     * @param id the id of the aPIAuditLogDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aPIAuditLogDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<APIAuditLogDTO> getAPIAuditLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to get APIAuditLog : {}", id);
        Optional<APIAuditLogDTO> aPIAuditLogDTO = aPIAuditLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aPIAuditLogDTO);
    }

    /**
     * {@code DELETE  /api-audit-logs/:id} : delete the "id" aPIAuditLog.
     *
     * @param id the id of the aPIAuditLogDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAPIAuditLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete APIAuditLog : {}", id);
        aPIAuditLogService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

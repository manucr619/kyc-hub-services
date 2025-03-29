package com.kycnetwork.web.rest;

import com.kycnetwork.repository.KycConsentRepository;
import com.kycnetwork.service.KycConsentService;
import com.kycnetwork.service.dto.KycConsentDTO;
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
 * REST controller for managing {@link com.kycnetwork.domain.KycConsent}.
 */
@RestController
@RequestMapping("/api/kyc-consents")
public class KycConsentResource {

    private static final Logger LOG = LoggerFactory.getLogger(KycConsentResource.class);

    private static final String ENTITY_NAME = "kycConsent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final KycConsentService kycConsentService;

    private final KycConsentRepository kycConsentRepository;

    public KycConsentResource(KycConsentService kycConsentService, KycConsentRepository kycConsentRepository) {
        this.kycConsentService = kycConsentService;
        this.kycConsentRepository = kycConsentRepository;
    }

    /**
     * {@code POST  /kyc-consents} : Create a new kycConsent.
     *
     * @param kycConsentDTO the kycConsentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new kycConsentDTO, or with status {@code 400 (Bad Request)} if the kycConsent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<KycConsentDTO> createKycConsent(@Valid @RequestBody KycConsentDTO kycConsentDTO) throws URISyntaxException {
        LOG.debug("REST request to save KycConsent : {}", kycConsentDTO);
        if (kycConsentDTO.getId() != null) {
            throw new BadRequestAlertException("A new kycConsent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        kycConsentDTO = kycConsentService.save(kycConsentDTO);
        return ResponseEntity.created(new URI("/api/kyc-consents/" + kycConsentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, kycConsentDTO.getId().toString()))
            .body(kycConsentDTO);
    }

    /**
     * {@code PUT  /kyc-consents/:id} : Updates an existing kycConsent.
     *
     * @param id the id of the kycConsentDTO to save.
     * @param kycConsentDTO the kycConsentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kycConsentDTO,
     * or with status {@code 400 (Bad Request)} if the kycConsentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the kycConsentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<KycConsentDTO> updateKycConsent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody KycConsentDTO kycConsentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update KycConsent : {}, {}", id, kycConsentDTO);
        if (kycConsentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, kycConsentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!kycConsentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        kycConsentDTO = kycConsentService.update(kycConsentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, kycConsentDTO.getId().toString()))
            .body(kycConsentDTO);
    }

    /**
     * {@code PATCH  /kyc-consents/:id} : Partial updates given fields of an existing kycConsent, field will ignore if it is null
     *
     * @param id the id of the kycConsentDTO to save.
     * @param kycConsentDTO the kycConsentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kycConsentDTO,
     * or with status {@code 400 (Bad Request)} if the kycConsentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the kycConsentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the kycConsentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<KycConsentDTO> partialUpdateKycConsent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody KycConsentDTO kycConsentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update KycConsent partially : {}, {}", id, kycConsentDTO);
        if (kycConsentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, kycConsentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!kycConsentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<KycConsentDTO> result = kycConsentService.partialUpdate(kycConsentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, kycConsentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /kyc-consents} : get all the kycConsents.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of kycConsents in body.
     */
    @GetMapping("")
    public ResponseEntity<List<KycConsentDTO>> getAllKycConsents(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of KycConsents");
        Page<KycConsentDTO> page = kycConsentService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /kyc-consents/:id} : get the "id" kycConsent.
     *
     * @param id the id of the kycConsentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the kycConsentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<KycConsentDTO> getKycConsent(@PathVariable("id") Long id) {
        LOG.debug("REST request to get KycConsent : {}", id);
        Optional<KycConsentDTO> kycConsentDTO = kycConsentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(kycConsentDTO);
    }

    /**
     * {@code DELETE  /kyc-consents/:id} : delete the "id" kycConsent.
     *
     * @param id the id of the kycConsentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKycConsent(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete KycConsent : {}", id);
        kycConsentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

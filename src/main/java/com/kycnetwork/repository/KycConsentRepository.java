package com.kycnetwork.repository;

import com.kycnetwork.domain.KycConsent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the KycConsent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KycConsentRepository extends JpaRepository<KycConsent, Long> {}

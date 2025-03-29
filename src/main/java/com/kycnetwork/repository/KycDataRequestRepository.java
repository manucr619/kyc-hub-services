package com.kycnetwork.repository;

import com.kycnetwork.domain.KycDataRequest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the KycDataRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KycDataRequestRepository extends JpaRepository<KycDataRequest, Long> {}

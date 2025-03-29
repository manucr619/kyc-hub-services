package com.kycnetwork.repository;

import com.kycnetwork.domain.KycCredential;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the KycCredential entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KycCredentialRepository extends JpaRepository<KycCredential, Long> {}

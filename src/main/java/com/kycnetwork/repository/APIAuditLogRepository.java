package com.kycnetwork.repository;

import com.kycnetwork.domain.APIAuditLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the APIAuditLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface APIAuditLogRepository extends JpaRepository<APIAuditLog, Long> {}

package com.kycnetwork.repository;

import com.kycnetwork.domain.CountryRegulation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CountryRegulation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CountryRegulationRepository extends JpaRepository<CountryRegulation, Long> {}

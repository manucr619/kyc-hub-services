package com.kycnetwork.service.mapper;

import com.kycnetwork.domain.CountryRegulation;
import com.kycnetwork.service.dto.CountryRegulationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CountryRegulation} and its DTO {@link CountryRegulationDTO}.
 */
@Mapper(componentModel = "spring")
public interface CountryRegulationMapper extends EntityMapper<CountryRegulationDTO, CountryRegulation> {}

package com.kycnetwork.service.mapper;

import com.kycnetwork.domain.CountryRegulation;
import com.kycnetwork.domain.Customer;
import com.kycnetwork.service.dto.CountryRegulationDTO;
import com.kycnetwork.service.dto.CustomerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Customer} and its DTO {@link CustomerDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {
    @Mapping(target = "countryRegulation", source = "countryRegulation", qualifiedByName = "countryRegulationId")
    CustomerDTO toDto(Customer s);

    @Named("countryRegulationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CountryRegulationDTO toDtoCountryRegulationId(CountryRegulation countryRegulation);
}

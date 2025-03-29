package com.kycnetwork.service.mapper;

import com.kycnetwork.domain.Bank;
import com.kycnetwork.domain.Customer;
import com.kycnetwork.domain.KycConsent;
import com.kycnetwork.service.dto.BankDTO;
import com.kycnetwork.service.dto.CustomerDTO;
import com.kycnetwork.service.dto.KycConsentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link KycConsent} and its DTO {@link KycConsentDTO}.
 */
@Mapper(componentModel = "spring")
public interface KycConsentMapper extends EntityMapper<KycConsentDTO, KycConsent> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerId")
    @Mapping(target = "issuerBank", source = "issuerBank", qualifiedByName = "bankId")
    @Mapping(target = "recipientBank", source = "recipientBank", qualifiedByName = "bankId")
    KycConsentDTO toDto(KycConsent s);

    @Named("customerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerDTO toDtoCustomerId(Customer customer);

    @Named("bankId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BankDTO toDtoBankId(Bank bank);
}

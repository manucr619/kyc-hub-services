package com.kycnetwork.service.mapper;

import com.kycnetwork.domain.Bank;
import com.kycnetwork.domain.Customer;
import com.kycnetwork.domain.KycConsent;
import com.kycnetwork.domain.KycDataRequest;
import com.kycnetwork.service.dto.BankDTO;
import com.kycnetwork.service.dto.CustomerDTO;
import com.kycnetwork.service.dto.KycConsentDTO;
import com.kycnetwork.service.dto.KycDataRequestDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link KycDataRequest} and its DTO {@link KycDataRequestDTO}.
 */
@Mapper(componentModel = "spring")
public interface KycDataRequestMapper extends EntityMapper<KycDataRequestDTO, KycDataRequest> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerId")
    @Mapping(target = "fromBank", source = "fromBank", qualifiedByName = "bankId")
    @Mapping(target = "toBank", source = "toBank", qualifiedByName = "bankId")
    @Mapping(target = "consent", source = "consent", qualifiedByName = "kycConsentId")
    KycDataRequestDTO toDto(KycDataRequest s);

    @Named("customerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerDTO toDtoCustomerId(Customer customer);

    @Named("bankId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BankDTO toDtoBankId(Bank bank);

    @Named("kycConsentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    KycConsentDTO toDtoKycConsentId(KycConsent kycConsent);
}

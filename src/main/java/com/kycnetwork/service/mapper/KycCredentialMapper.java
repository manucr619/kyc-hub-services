package com.kycnetwork.service.mapper;

import com.kycnetwork.domain.Bank;
import com.kycnetwork.domain.Customer;
import com.kycnetwork.domain.KycCredential;
import com.kycnetwork.service.dto.BankDTO;
import com.kycnetwork.service.dto.CustomerDTO;
import com.kycnetwork.service.dto.KycCredentialDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link KycCredential} and its DTO {@link KycCredentialDTO}.
 */
@Mapper(componentModel = "spring")
public interface KycCredentialMapper extends EntityMapper<KycCredentialDTO, KycCredential> {
    @Mapping(target = "issuer", source = "issuer", qualifiedByName = "bankId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerId")
    KycCredentialDTO toDto(KycCredential s);

    @Named("bankId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BankDTO toDtoBankId(Bank bank);

    @Named("customerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerDTO toDtoCustomerId(Customer customer);
}

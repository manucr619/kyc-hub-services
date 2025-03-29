package com.kycnetwork.service.mapper;

import com.kycnetwork.domain.Bank;
import com.kycnetwork.domain.Customer;
import com.kycnetwork.domain.Transaction;
import com.kycnetwork.service.dto.BankDTO;
import com.kycnetwork.service.dto.CustomerDTO;
import com.kycnetwork.service.dto.TransactionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Transaction} and its DTO {@link TransactionDTO}.
 */
@Mapper(componentModel = "spring")
public interface TransactionMapper extends EntityMapper<TransactionDTO, Transaction> {
    @Mapping(target = "originator", source = "originator", qualifiedByName = "customerId")
    @Mapping(target = "beneficiary", source = "beneficiary", qualifiedByName = "customerId")
    @Mapping(target = "originatorBank", source = "originatorBank", qualifiedByName = "bankId")
    @Mapping(target = "beneficiaryBank", source = "beneficiaryBank", qualifiedByName = "bankId")
    TransactionDTO toDto(Transaction s);

    @Named("customerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerDTO toDtoCustomerId(Customer customer);

    @Named("bankId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BankDTO toDtoBankId(Bank bank);
}

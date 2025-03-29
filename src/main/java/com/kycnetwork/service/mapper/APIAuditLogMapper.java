package com.kycnetwork.service.mapper;

import com.kycnetwork.domain.APIAuditLog;
import com.kycnetwork.domain.Bank;
import com.kycnetwork.service.dto.APIAuditLogDTO;
import com.kycnetwork.service.dto.BankDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link APIAuditLog} and its DTO {@link APIAuditLogDTO}.
 */
@Mapper(componentModel = "spring")
public interface APIAuditLogMapper extends EntityMapper<APIAuditLogDTO, APIAuditLog> {
    @Mapping(target = "bank", source = "bank", qualifiedByName = "bankId")
    APIAuditLogDTO toDto(APIAuditLog s);

    @Named("bankId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BankDTO toDtoBankId(Bank bank);
}

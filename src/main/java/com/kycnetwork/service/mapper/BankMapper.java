package com.kycnetwork.service.mapper;

import com.kycnetwork.domain.Bank;
import com.kycnetwork.service.dto.BankDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Bank} and its DTO {@link BankDTO}.
 */
@Mapper(componentModel = "spring")
public interface BankMapper extends EntityMapper<BankDTO, Bank> {}

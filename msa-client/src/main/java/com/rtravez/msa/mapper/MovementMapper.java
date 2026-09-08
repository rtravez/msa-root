package com.rtravez.msa.mapper;

import com.rtravez.msa.dto.request.MovementRequest;
import com.rtravez.msa.dto.response.MovementReportResponse;
import com.rtravez.msa.entity.MovementEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovementMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "movementType", source = "movementType")
    @Mapping(target = "movementValue", source = "movementValue")
    @Mapping(target = "account.accountNumber", source = "accountNumber")
    MovementEntity toEntity(MovementRequest request);

    @Mapping(target = "accountNumber", source = "account.accountNumber")
    @Mapping(target = "accountType", source = "account.accountType")
    @Mapping(target = "initialBalance", source = "account.initialBalance")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "movementValue", source = "movementValue")
    @Mapping(target = "availableBalance", source = "availableBalance")
    @Mapping(target = "movementDate", source = "movementDate")
    @Mapping(target = "identification", source = "account.person.identification")
    @Mapping(target = "lastname", source = "account.person.lastname")
    @Mapping(target = "name", source = "account.person.name")
    MovementReportResponse toResponse(MovementEntity entity);
}

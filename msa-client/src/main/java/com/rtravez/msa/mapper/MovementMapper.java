package com.rtravez.msa.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.rtravez.msa.dto.request.MovementRequest;
import com.rtravez.msa.entity.MovementEntity;

@Mapper(componentModel = "spring")
public interface MovementMapper {

    @BeanMapping (ignoreByDefault = true)
    @Mapping(target = "movementType", source = "movementType")
    @Mapping(target = "movementValue", source = "movementValue")
    @Mapping(target = "account.accountNumber", source = "accountNumber")
    MovementEntity toEntity(MovementRequest request);
}

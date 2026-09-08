package com.rtravez.msa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.rtravez.msa.dto.response.AccountResponse;
import com.rtravez.msa.entity.AccountEntity;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "name", source = "accountEntity.person.name")
    @Mapping(target = "lastname", source = "accountEntity.person.lastname")
    @Mapping(target = "personId", source = "accountEntity.person.personId")
    AccountResponse toResponse(AccountEntity accountEntity);
}

package com.rtravez.msa.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.rtravez.msa.dto.response.AccountResponse;
import com.rtravez.msa.entity.AccountEntity;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "name", source = "person.name")
    @Mapping(target = "lastname", source = "person.lastname")
    @Mapping(target = "personId", source = "person.personId")
    AccountResponse toResponse(AccountEntity entity);

}

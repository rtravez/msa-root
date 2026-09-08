package com.rtravez.msa.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.rtravez.msa.dto.request.UserRequest;
import com.rtravez.msa.dto.response.UserResponse;
import com.rtravez.msa.entity.view.PersonView;
import com.rtravez.msa.entity.view.UserView;

/**
 * MapStruct mapper for UserView to UserResponse conversions.
 * Centralizes DTO mapping logic and eliminates manual conversion code.
 *
 * @author renetravez
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Maps UserView to UserResponse.
     * Includes nested Person data extraction.
     *
     * @param entity the user view entity
     * @return the user response DTO
     */
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "name", source = "person.name")
    @Mapping(target = "lastname", source = "person.lastname")
    @Mapping(target = "identification", source = "person.identification")
    @Mapping(target = "address", source = "person.address")
    @Mapping(target = "telephone", source = "person.telephone")
    @Mapping(target = "gender", source = "person.gender")
    @Mapping(target = "age", source = "person.age")
    @Mapping(target = "personId", source = "person.personId")
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "username", source = "username")
    UserResponse toResponse(UserView entity);
}

package com.rtravez.msa.mapper;

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
    @Mapping(target = "name", source = "person.name")
    @Mapping(target = "lastname", source = "person.lastname")
    @Mapping(target = "identification", source = "person.identification")
    @Mapping(target = "address", source = "person.address")
    @Mapping(target = "telephone", source = "person.telephone")
    @Mapping(target = "gender", source = "person.gender")
    @Mapping(target = "age", source = "person.age")
    UserResponse toResponse(UserView entity);

    /**
     * Maps UserRequest to PersonView.
     * Extracts only the person-related fields from the user request.
     * The service layer is responsible for setting audit/metadata fields.
     *
     * @param request the user request DTO
     * @return the person view with populated person fields
     */
    @Mapping(target = "personId", ignore = true)
    @Mapping(target = "users", ignore = true)
    @Mapping(target = "accounts", ignore = true)
    PersonView toEntity(UserRequest request);

}

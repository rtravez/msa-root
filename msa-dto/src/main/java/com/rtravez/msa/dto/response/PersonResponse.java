package com.rtravez.msa.dto.response;

import com.rtravez.msa.dto.BaseDto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PersonResponse extends BaseDto {

    private Long personId;
    @NotEmpty
    private String identification;
    @NotEmpty
    private String name;
    @NotEmpty
    private String lastname;
    private String address;
    private String telephone;
    private String gender;
    private Integer age;
}

package com.rtravez.msa.dto.request;

import com.rtravez.msa.dto.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serial;

@Data
@EqualsAndHashCode(callSuper = true)
public class PersonRequest extends BaseDto {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long personId;
    @NotBlank
    @Size(max = 10)
    private String identification;
    @NotBlank
    private String name;
    @NotBlank
    private String lastname;
    private String address;
    private String telephone;
    private String gender;
    private Integer age;
}

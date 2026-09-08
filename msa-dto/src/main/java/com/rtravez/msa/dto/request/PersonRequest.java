package com.rtravez.msa.dto.request;

import com.rtravez.msa.dto.BaseDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PersonRequest extends BaseDto {
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

package com.rtravez.msa.dto.request;

import com.rtravez.msa.dto.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class PersonRequest extends BaseDto implements Serializable {
    private static final long serialVersionUID = 1L;

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

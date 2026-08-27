package com.rtravez.msa.dto.response;

import com.rtravez.msa.dto.BaseDto;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class PersonResponse extends BaseDto implements Serializable {
    @Serial
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

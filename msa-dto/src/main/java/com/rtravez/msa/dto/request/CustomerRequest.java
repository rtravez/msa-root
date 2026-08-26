package com.rtravez.msa.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class CustomerRequest extends PersonRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long customerId;
    @NotEmpty
    private String password;
    @NotEmpty
    private String username;
}
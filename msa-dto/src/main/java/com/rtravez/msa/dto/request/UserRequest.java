package com.rtravez.msa.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class UserRequest extends PersonRequest {

    private Long userId;
    @NotEmpty
    private String password;
    @NotEmpty
    private String username;
}
package com.rtravez.msa.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class UserRequest extends PersonRequest {

    private Long userId;
    @NotBlank
    @Size(min = 8, max = 60)
    private String password;
    @NotBlank
    @Size(max = 20)
    private String username;
}
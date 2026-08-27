package com.rtravez.msa.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class UserResponse extends PersonResponse {

    private Long userId;
    private String username;

}
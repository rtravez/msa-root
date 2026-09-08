package com.rtravez.msa.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data 
@SuperBuilder 
@NoArgsConstructor 
@AllArgsConstructor 
@EqualsAndHashCode (callSuper = true)
public class UserResponse extends PersonResponse {

    private Long userId;
    private String username;

}
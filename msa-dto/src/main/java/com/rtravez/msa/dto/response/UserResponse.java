package com.rtravez.msa.dto.response;

import lombok.Data;

@Data
public class UserResponse {

    private Long userId;
    private String username;
    private String name;
    private String lastname;
    private String address;
    private String telephone;
    private String password;
    private boolean status;

}
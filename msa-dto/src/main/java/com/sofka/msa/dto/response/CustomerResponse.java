package com.sofka.msa.dto.response;

import lombok.Data;

@Data
public class CustomerResponse {

    private Long customerId;
    private String username;
    private String name;
    private String lastname;
    private String address;
    private String telephone;
    private String password;
    private boolean status;


}
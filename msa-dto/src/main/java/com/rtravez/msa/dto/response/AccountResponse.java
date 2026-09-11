package com.rtravez.msa.dto.response;

import java.math.BigDecimal;

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
public class AccountResponse extends PersonResponse {
    private Long accountId;
    private Long accountNumber;
    private String accountType;
    private BigDecimal initialBalance;
}
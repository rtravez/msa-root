package com.rtravez.msa.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class AccountResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long accountId;
    private Long personId;
    private Long accountNumber;
    private String accountType;
    private BigDecimal initialBalance;
    private String name;
    private String lastname;
    private boolean status;
}
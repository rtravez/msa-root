package com.rtravez.msa.dto.request;

import com.rtravez.msa.dto.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class AccountRequest extends BaseDto implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long accountId;
    @NotNull
    private Long accountNumber;
    @NotEmpty
    private String accountType;
    @NotNull
    private BigDecimal initialBalance;
    @NotEmpty
    private String identification;
}
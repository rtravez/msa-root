package com.rtravez.msa.dto.request;

import com.rtravez.msa.dto.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.io.Serial;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class AccountRequest extends BaseDto {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long accountId;
    @NotNull
    @Positive
    private Long accountNumber;
    @NotBlank
    @Size(max = 11)
    private String accountType;
    @NotNull
    @DecimalMin(value = "0.00")
    @Digits(integer = 19, fraction = 2)
    private BigDecimal initialBalance;
    @NotBlank
    @Size(max = 10)
    private String identification;
}
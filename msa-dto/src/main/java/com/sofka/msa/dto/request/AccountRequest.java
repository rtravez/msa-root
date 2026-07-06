package com.sofka.msa.dto.request;

import com.sofka.msa.dto.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class AccountRequest extends BaseDto implements Serializable {
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
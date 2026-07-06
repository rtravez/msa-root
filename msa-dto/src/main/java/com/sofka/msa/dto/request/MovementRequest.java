package com.sofka.msa.dto.request;

import com.sofka.msa.dto.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class MovementRequest extends BaseDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long movementId;
    private Date movementDate;
    @NotEmpty
    private String movementType;
    @NotNull
    private BigDecimal value;
    private BigDecimal availableBalance;
    @NotNull
    private Long accountNumber;
}
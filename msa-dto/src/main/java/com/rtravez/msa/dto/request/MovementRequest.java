package com.rtravez.msa.dto.request;

import com.rtravez.msa.dto.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class MovementRequest extends BaseDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long movementId;
    private LocalDateTime movementDate;
    @NotEmpty
    private String movementType;
    @NotNull
    private BigDecimal value;
    private BigDecimal availableBalance;
    @NotNull
    private Long accountNumber;
}
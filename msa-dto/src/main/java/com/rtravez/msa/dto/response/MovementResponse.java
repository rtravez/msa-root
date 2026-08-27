package com.rtravez.msa.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class MovementResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long movementId;
    @NotNull
    private LocalDateTime movementDate;
    @NotEmpty
    private Character movementType;
    @NotNull
    private BigDecimal value;
    @NotNull
    private BigDecimal availableBalance;
    @NotNull
    private Long accountId;
}
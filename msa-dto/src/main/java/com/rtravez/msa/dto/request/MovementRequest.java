package com.rtravez.msa.dto.request;

import com.rtravez.msa.dto.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class MovementRequest extends BaseDto {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long movementId;
    private LocalDateTime movementDate;
    @NotBlank
    @Pattern(regexp = "[DR]", flags = Pattern.Flag.CASE_INSENSITIVE, message = "El tipo de movimiento debe ser D o R")
    private String movementType;
    @NotNull
    private BigDecimal value;
    private BigDecimal availableBalance;
    @NotNull
    @Positive
    private Long accountNumber;

    @AssertTrue(message = "El valor del movimiento no puede ser cero")
    public boolean isValueNonZero() {
        return value != null && value.signum() != 0;
    }

    @AssertTrue(message = "El tipo de movimiento no coincide con el signo del valor")
    public boolean isMovementTypeConsistent() {
        if (movementType == null || value == null) {
            return true;
        }
        return ("D".equalsIgnoreCase(movementType) && value.signum() > 0)
                || ("R".equalsIgnoreCase(movementType) && value.signum() < 0);
    }
}
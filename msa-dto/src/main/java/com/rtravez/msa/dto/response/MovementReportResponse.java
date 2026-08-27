package com.rtravez.msa.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MovementReportResponse {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime movementDate;
    private String identification;
    private String name;
    private String lastname;
    private Long accountNumber;
    private String accountType;
    private BigDecimal initialBalance;
    private Boolean status;
    private BigDecimal value;
    private BigDecimal availableBalance;

}
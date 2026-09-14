package com.rtravez.msa.dto.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class MovementReportRequest {

    private LocalDateTime initialDate;
    private LocalDateTime finalDate;
    private String identification;
    private String accountType;
}

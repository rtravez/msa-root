package com.rtravez.msa.controller;

import com.rtravez.msa.dto.BaseResponseDto;
import com.rtravez.msa.dto.response.MovementReportResponse;
import com.rtravez.msa.service.MovementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController()
@RequestMapping("/api/reports")
@Validated
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Reportes", description = "Consulta de movimientos por cliente, período y tipo de cuenta")
@SecurityRequirement(name = "bearerAuth")
public class ReportController {

    private final MovementService movementService;

    /**
     * Find report movement
     *
     * @param initialDate
     * @param finalDate
     * @param identification
     * @param accountType
     * @return
     */
    @GetMapping
    @Secured({"ROLE_ADMIN"})
    @Operation(summary = "Consultar movimientos", description = "Obtiene los movimientos de un cliente dentro de un período y para un tipo de cuenta. Requiere el rol `ADMIN`.")
    @ApiResponse(responseCode = "200", description = "Consulta ejecutada correctamente, incluso si no hay movimientos")
    @ApiResponse(responseCode = "401", description = "Token JWT ausente o inválido", content = @Content)
    @ApiResponse(responseCode = "403", description = "El usuario no posee el rol ADMIN", content = @Content)
    public ResponseEntity<BaseResponseDto<List<MovementReportResponse>>> findMovementByDateAndIdentification(
            @Parameter(in = ParameterIn.QUERY, description = "Fecha inicial del período, en formato ISO-8601", example = "2026-01-01", required = true)
            @RequestParam("initialDate") LocalDateTime initialDate,
            @Parameter(in = ParameterIn.QUERY, description = "Fecha final del período, en formato ISO-8601", example = "2026-01-31", required = true)
            @RequestParam("finalDate") LocalDateTime finalDate,
            @Parameter(in = ParameterIn.QUERY, description = "Número de identificación del cliente", example = "1710034065", required = true)
            @RequestParam("identification") String identification,
            @Parameter(in = ParameterIn.QUERY, description = "Tipo de cuenta", example = "AHORROS", required = true)
            @RequestParam("accountType") String accountType) {
        List<MovementReportResponse> responses = movementService.findMovementByDateAndIdentification(initialDate, finalDate, identification, accountType);
        if (responses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.<List<MovementReportResponse>>builder().code(HttpStatus.OK.value()).message("No existen movimientos").build());
        }

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.<List<MovementReportResponse>>builder().code(HttpStatus.OK.value()).data(responses).message("Movimientos encontrados con \u00E9xito").build());
    }

}

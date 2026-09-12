package com.rtravez.msa.controller;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rtravez.msa.dto.BaseResponseDto;
import com.rtravez.msa.dto.request.MovementRequest;
import com.rtravez.msa.dto.response.MovementReportResponse;
import com.rtravez.msa.dto.response.MovementResponse;
import com.rtravez.msa.service.MovementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController()
@RequestMapping("/api/movements")
@Validated
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Movimientos", description = "Registro y anulación de movimientos de cuentas")
@SecurityRequirement(name = "bearerAuth")
public class MovementController {

    private final MovementService movementService;

    @GetMapping
    @Secured({ "ROLE_ADMIN" })
    @Operation(summary = "Listar movimientos", description = "Obtiene los movimientos registrados de forma paginada. Requiere el rol `ADMIN`.")
    @ApiResponse(responseCode = "200", description = "Consulta ejecutada correctamente, incluso si no existen movimientos")
    @ApiResponse(responseCode = "401", description = "Token JWT ausente o inválido", content = @Content)
    @ApiResponse(responseCode = "403", description = "El usuario no posee el rol ADMIN", content = @Content)
    public ResponseEntity<BaseResponseDto<Page<MovementResponse>>> findMovementAll(
            @Parameter(description = "Paginación y ordenamiento. Por defecto devuelve 10 registros por página.") @PageableDefault(size = 10) Pageable pageable) {
        Page<MovementResponse> movementResponses = movementService.findMovementAll(pageable);
        if (movementResponses.isEmpty()) {
            return ResponseEntity.ok(BaseResponseDto.<Page<MovementResponse>>builder()
                    .status(HttpStatus.OK.value()).detail("No existen movimientos").build());
        }

        return ResponseEntity.ok(BaseResponseDto.<Page<MovementResponse>>builder().status(HttpStatus.OK.value())
                .data(movementResponses).detail("Movimientos encontrados con \u00E9xito").build());
    }

    @GetMapping(path = "/{id}")
    @Secured({ "ROLE_ADMIN" })
    @Operation(summary = "Buscar movimiento por id")
    @ApiResponse(responseCode = "200", description = "Movimiento encontrado")
    @ApiResponse(responseCode = "404", description = "Movimiento no encontrado")
    @ApiResponse(responseCode = "401", description = "Token JWT ausente o inválido", content = @Content)
    @ApiResponse(responseCode = "403", description = "El usuario no posee el rol ADMIN", content = @Content)
    public ResponseEntity<BaseResponseDto<MovementResponse>> findMovementById(
            @Parameter(description = "Identificador del movimiento", required = true, example = "1") @PathVariable Long id) {
        MovementResponse response = movementService.findMovementById(id);
        if (response == null || response.getMovementId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseDto.<MovementResponse>builder()
                    .status(HttpStatus.NOT_FOUND.value()).detail("Movimiento no encontrado").build());
        }
        return ResponseEntity.ok(BaseResponseDto.<MovementResponse>builder().status(HttpStatus.OK.value())
                .data(response).detail("Movimiento encontrado con \u00E9xito").build());
    }

    /**
     * Save movement
     *
     * @param request
     * @return
     */
    @Secured({ "ROLE_ADMIN" })
    @PostMapping
    @Operation(summary = "Crear movimiento", description = "Registra un débito (`D`) o retiro (`R`) para una cuenta existente. El tipo debe coincidir con el signo del valor.")
    @ApiResponse(responseCode = "201", description = "Movimiento creado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos del movimiento inválidos", content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class)))
    @ApiResponse(responseCode = "402", description = "Saldo disponible insuficiente", content = @Content)
    @ApiResponse(responseCode = "404", description = "La cuenta no existe", content = @Content)
    @ApiResponse(responseCode = "401", description = "Token JWT ausente o inválido", content = @Content)
    @ApiResponse(responseCode = "403", description = "El usuario no posee el rol ADMIN", content = @Content)
    public ResponseEntity<BaseResponseDto<MovementResponse>> save(
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del movimiento a registrar", required = true, content = @Content(examples = @ExampleObject(value = "{\"movementType\": \"D\", \"movementValue\": 100.00, \"accountNumber\": 478758}"))) MovementRequest request) {
        MovementResponse response = movementService.processSaveMovement(request);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseDto.<MovementResponse>builder()
                    .status(HttpStatus.NOT_FOUND.value()).detail("La cuenta no existe").build());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseDto.<MovementResponse>builder()
                .status(HttpStatus.CREATED.value()).data(response).detail("Movimiento creado con \u00E9xito").build());
    }

    /**
     * Update movement
     *
     * @param request
     * @return
     */
    @Secured({ "ROLE_ADMIN" })
    @PutMapping(path = "/{id}")
    @Operation(summary = "Actualizar movimiento", description = "Actualiza los datos de un movimiento existente. El cuerpo debe incluir el identificador del movimiento.")
    @ApiResponse(responseCode = "200", description = "Movimiento actualizado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos de movimiento inválidos", content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "El movimiento no existe", content = @Content)
    @ApiResponse(responseCode = "409", description = "Conflicto de integridad", content = @Content)
    @ApiResponse(responseCode = "401", description = "Token JWT ausente o inválido", content = @Content)
    @ApiResponse(responseCode = "403", description = "El usuario no posee el rol ADMIN", content = @Content)
    public ResponseEntity<BaseResponseDto<MovementResponse>> update(
            @Parameter(description = "Identificador del movimiento", required = true, example = "1") @PathVariable Long id,
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos actualizados del movimiento", required = true, content = @Content(examples = @ExampleObject(value = "{\"accountNumber\": 478758, \"movementType\": \"D\", \"movementValue\": 100.00}"))) MovementRequest request) {
        MovementResponse response = movementService.processUpdateMovement(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.<MovementResponse>builder()
                .status(HttpStatus.OK.value()).data(response).detail("Movimiento actualizado con \u00E9xito").build());
    }

    /**
     * Delete movement by id
     *
     * @param id
     * @return
     */
    @Secured({ "ROLE_ADMIN" })
    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Eliminar movimiento", description = "Anula un movimiento por su identificador. No se permite eliminar un movimiento que tenga movimientos posteriores.")
    @ApiResponse(responseCode = "200", description = "Movimiento eliminado correctamente")
    @ApiResponse(responseCode = "404", description = "El movimiento no existe", content = @Content)
    @ApiResponse(responseCode = "409", description = "Existen movimientos posteriores", content = @Content)
    @ApiResponse(responseCode = "401", description = "Token JWT ausente o inválido", content = @Content)
    @ApiResponse(responseCode = "403", description = "El usuario no posee el rol ADMIN", content = @Content)
    public ResponseEntity<BaseResponseDto<Long>> deleteById(
            @Parameter(in = ParameterIn.PATH, description = "Identificador del movimiento", example = "1", required = true) @PathVariable Long id) {
        if (this.movementService.deleteMovementById(id) >= 1) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(BaseResponseDto.<Long>builder().status(HttpStatus.OK.value())
                            .detail("Movimiento eliminado con \u00E9xito").build());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseDto.<Long>builder()
                    .status(HttpStatus.NOT_FOUND.value()).detail("El movimiento no existe").build());
        }
    }

    /**
     * Retrieves a list of movements for a user within a specified date range and
     * account type.
     * This operation requires the user to have the `ADMIN` role.
     *
     * @param initialDate    The start date of the period to retrieve movements
     *                       from. Must be provided in the format
     *                       "yyyy-MM-ddTHH:mm:ss".
     * @param finalDate      The end date of the period to retrieve movements until.
     *                       Must be provided in the format "yyyy-MM-ddTHH:mm:ss".
     * @param identification The identification number of the user whose movements
     *                       are being queried.
     * @param accountType    The type of account for which movements are being
     *                       retrieved (e.g., "AHORROS").
     * @return A {@link ResponseEntity} containing a {@link BaseResponseDto} with a
     *         list of {@link MovementReportResponse} objects.
     *         If no movements exist, the response includes a message indicating no
     *         movements were found.
     */
    @GetMapping("/reports")
    @Secured({ "ROLE_ADMIN" })
    @Operation(summary = "Consultar movimientos", description = "Obtiene los movimientos de un usuario dentro de un período y para un tipo de cuenta. Requiere el rol `ADMIN`.")
    @ApiResponse(responseCode = "200", description = "Consulta ejecutada correctamente, incluso si no hay movimientos")
    @ApiResponse(responseCode = "401", description = "Token JWT ausente o inválido", content = @Content)
    @ApiResponse(responseCode = "403", description = "El usuario no posee el rol ADMIN", content = @Content)
    public ResponseEntity<BaseResponseDto<Page<MovementReportResponse>>> findMovementByDateAndIdentification(
            @Parameter(in = ParameterIn.QUERY, description = "Fecha inicial del período", example = "2026-09-01T00:00:00", required = true) @RequestParam("initialDate") LocalDateTime initialDate,
            @Parameter(in = ParameterIn.QUERY, description = "Fecha final del período", example = "2026-09-30T23:59:59", required = true) @RequestParam("finalDate") LocalDateTime finalDate,
            @Parameter(in = ParameterIn.QUERY, description = "Número de identificación del usuario", example = "1710034065", required = true) @RequestParam("identification") String identification,
            @Parameter(in = ParameterIn.QUERY, description = "Tipo de cuenta", example = "AHORROS", required = true) @RequestParam("accountType") String accountType,
            @Parameter(description = "Paginación y ordenamiento. Por defecto devuelve 10 registros por página.") @PageableDefault(size = 10) Pageable pageable) {
        Page<MovementReportResponse> responses = movementService.findMovementByDateAndIdentification(initialDate,
                finalDate, identification, accountType, pageable);
        if (responses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.<Page<MovementReportResponse>>builder()
                    .status(HttpStatus.OK.value()).detail("No existen movimientos").build());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponseDto.<Page<MovementReportResponse>>builder().status(HttpStatus.OK.value())
                        .data(responses).detail("Movimientos encontrados con \u00E9xito").build());
    }
}

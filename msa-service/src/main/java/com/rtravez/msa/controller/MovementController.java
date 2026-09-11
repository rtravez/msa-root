package com.rtravez.msa.controller;

import com.rtravez.msa.dto.BaseResponseDto;
import com.rtravez.msa.dto.request.MovementRequest;
import com.rtravez.msa.dto.response.MovementResponse;
import com.rtravez.msa.service.MovementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/movements")
@Validated
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Movimientos", description = "Registro y anulación de movimientos de cuentas")
@SecurityRequirement(name = "bearerAuth")
public class MovementController {

    private final MovementService movementService;


    /**
     * Save movement
     *
     * @param request
     * @return
     */
    @Secured({"ROLE_ADMIN"})
    @PostMapping
    @Operation(summary = "Crear movimiento", description = "Registra un débito (`D`) o retiro (`R`) para una cuenta existente. El tipo debe coincidir con el signo del valor.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Movimiento creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos del movimiento inválidos", content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class))),
            @ApiResponse(responseCode = "402", description = "Saldo disponible insuficiente", content = @Content),
            @ApiResponse(responseCode = "404", description = "La cuenta no existe", content = @Content),
            @ApiResponse(responseCode = "401", description = "Token JWT ausente o inválido", content = @Content),
            @ApiResponse(responseCode = "403", description = "El usuario no posee el rol ADMIN", content = @Content)
    })
    public ResponseEntity<BaseResponseDto<MovementResponse>> save(
            @Valid @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del movimiento a registrar", required = true,
                    content = @Content(examples = @ExampleObject(value = "{\"movementType\": \"D\", \"movementValue\": 100.00, \"accountNumber\": 478758}")))
            MovementRequest request) {
        MovementResponse response = movementService.processSaveMovement(request);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseDto.<MovementResponse>builder().status(HttpStatus.NOT_FOUND.value()).detail("La cuenta no existe").build());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseDto.<MovementResponse>builder().status(HttpStatus.CREATED.value()).data(response).detail("Movimiento creado con \u00E9xito").build());
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
    public ResponseEntity<BaseResponseDto<Object>> deleteById(
            @Parameter(in = ParameterIn.PATH, description = "Identificador del movimiento", example = "1", required = true) @PathVariable Long id) {
        if (this.movementService.deleteMovementById(id) >= 1) {
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.builder().status(HttpStatus.OK.value())
                    .detail("Movimiento eliminado con \u00E9xito").build());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseDto.builder()
                    .status(HttpStatus.NOT_FOUND.value()).detail("El movimiento no existe").build());
        }
    }
}

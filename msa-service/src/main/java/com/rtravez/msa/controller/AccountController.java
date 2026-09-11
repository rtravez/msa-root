package com.rtravez.msa.controller;

import com.rtravez.msa.dto.BaseResponseDto;
import com.rtravez.msa.dto.request.AccountRequest;
import com.rtravez.msa.dto.response.AccountResponse;
import com.rtravez.msa.dto.response.UserResponse;
import com.rtravez.msa.service.AccountService;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/accounts")
@Validated
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Cuentas", description = "Administración de cuentas bancarias")
@SecurityRequirement(name = "bearerAuth")
public class AccountController {

    private final AccountService accountService;

    /**
     * Find account all
     *
     * @return
     */
    @GetMapping
    @Secured({ "ROLE_ADMIN" })
    @Operation(summary = "Listar cuentas", description = "Obtiene las cuentas registradas de forma paginada. Requiere el rol `ADMIN`.")
    @ApiResponse(responseCode = "200", description = "Consulta ejecutada correctamente, incluso si no existen cuentas")
    @ApiResponse(responseCode = "401", description = "Token JWT ausente o inválido", content = @Content)
    @ApiResponse(responseCode = "403", description = "El usuario no posee el rol ADMIN", content = @Content)
    public ResponseEntity<BaseResponseDto<Page<AccountResponse>>> findAccountAll(
            @Parameter(description = "Paginación y ordenamiento. Por defecto devuelve 20 registros por página.") @PageableDefault(size = 20) Pageable pageable) {
        Page<AccountResponse> accountResponses = accountService.findAccountAll(pageable);
        if (accountResponses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.<Page<AccountResponse>>builder()
                    .code(HttpStatus.OK.value()).message("No existen cuentas").build());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(BaseResponseDto.<Page<AccountResponse>>builder().code(HttpStatus.OK.value())
                        .data(accountResponses).message("Cuentas encontradas con \u00E9xito").build());
    }

    @Secured({ "ROLE_ADMIN" })
    @GetMapping(path = "/{id}")
    @Operation(summary = "Buscar cuenta por id")
    @ApiResponse(responseCode = "200", description = "Cuenta encontrada")
    @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    @ApiResponse(responseCode = "401", description = "Token ausente o inválido")
    @ApiResponse(responseCode = "403", description = "El token no tiene ROLE_ADMIN")
    public ResponseEntity<BaseResponseDto<AccountResponse>> findAccountById(
            @Parameter(description = "Identificador de la cuenta", required = true, example = "1") @PathVariable Long id) {
        return getBaseResponseDtoResponseEntity(this.accountService.findAccountById(id));
    }

    /**
     * Save account
     *
     * @param request
     * @return
     */
    @Secured({ "ROLE_ADMIN" })
    @PostMapping
    @Operation(summary = "Crear cuenta", description = "Crea una cuenta para el usuario identificado. El número de cuenta debe ser único y el usuario debe existir.")
    @ApiResponse(responseCode = "201", description = "Cuenta creada correctamente")
    @ApiResponse(responseCode = "400", description = "Datos de cuenta inválidos", content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "No existe el usuario asociado", content = @Content)
    @ApiResponse(responseCode = "409", description = "Ya existe una cuenta con el número indicado", content = @Content)
    @ApiResponse(responseCode = "401", description = "Token JWT ausente o inválido", content = @Content)
    @ApiResponse(responseCode = "403", description = "El usuario no posee el rol ADMIN", content = @Content)
    public ResponseEntity<BaseResponseDto<AccountResponse>> save(
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos de la cuenta a crear", required = true, content = @Content(examples = @ExampleObject(value = "{\"accountNumber\": 478758, \"accountType\": \"AHORROS\", \"initialBalance\": 1000.00, \"identification\": \"1710034065\"}"))) AccountRequest request) {
        if (Boolean.TRUE.equals(this.accountService.exist(request.getAccountNumber()))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(BaseResponseDto.<AccountResponse>builder()
                    .code(HttpStatus.CONFLICT.value()).message("La cuenta ya existe").build());
        }

        UserResponse userResponse = accountService.findUserByIdentification(request.getIdentification());
        if (userResponse == null || userResponse.getUserId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseDto.<AccountResponse>builder()
                    .code(HttpStatus.NOT_FOUND.value()).message("El usuario no existe").build());
        }

        AccountResponse response = accountService.processSaveAccount(request, userResponse);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseDto.<AccountResponse>builder()
                    .code(HttpStatus.NOT_FOUND.value()).message("La cuenta no existe").build());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseDto.<AccountResponse>builder()
                .code(HttpStatus.CREATED.value()).data(response).message("Cuenta creada con \u00E9xito").build());
    }

    /**
     * Update account
     *
     * @param request
     * @return
     */
    @Secured({ "ROLE_ADMIN" })
    @PutMapping(path = "/{id}")
    @Operation(summary = "Actualizar cuenta", description = "Actualiza los datos de una cuenta existente. El cuerpo debe incluir el identificador de la cuenta.")
    @ApiResponse(responseCode = "200", description = "Cuenta actualizada correctamente")
    @ApiResponse(responseCode = "400", description = "Datos de cuenta inválidos", content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "La cuenta no existe", content = @Content)
    @ApiResponse(responseCode = "409", description = "Conflicto de integridad", content = @Content)
    @ApiResponse(responseCode = "401", description = "Token JWT ausente o inválido", content = @Content)
    @ApiResponse(responseCode = "403", description = "El usuario no posee el rol ADMIN", content = @Content)
    public ResponseEntity<BaseResponseDto<AccountResponse>> update(
            @Parameter(description = "Identificador de la cuenta", required = true, example = "1") @PathVariable Long id,
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos actualizados de la cuenta", required = true, content = @Content(examples = @ExampleObject(value = "{\"accountId\": 1, \"accountNumber\": 478758, \"accountType\": \"AHORROS\", \"initialBalance\": 1200.00, \"identification\": \"1710034065\"}")))
            AccountRequest request) {
        AccountResponse response = accountService.processUpdateAccount(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.<AccountResponse>builder()
                .code(HttpStatus.OK.value()).data(response).message("Cuenta actualizada con \u00E9xito").build());
    }

    /**
     * Delete an account
     *
     * @param id
     * @return
     */
    @Secured({ "ROLE_ADMIN" })
    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Eliminar cuenta", description = "Elimina una cuenta por su identificador. No puede eliminarse si existen movimientos asociados.")
    @ApiResponse(responseCode = "200", description = "Cuenta eliminada correctamente")
    @ApiResponse(responseCode = "404", description = "La cuenta no existe", content = @Content)
    @ApiResponse(responseCode = "409", description = "La cuenta posee movimientos asociados", content = @Content)
    @ApiResponse(responseCode = "401", description = "Token JWT ausente o inválido", content = @Content)
    @ApiResponse(responseCode = "403", description = "El usuario no posee el rol ADMIN", content = @Content)
    public ResponseEntity<BaseResponseDto<Long>> deleteById(
            @Parameter(in = ParameterIn.PATH, description = "Identificador de la cuenta", example = "1", required = true) @PathVariable Long id) {
        if (this.accountService.deleteAccountById(id) >= 1) {
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.<Long>builder().code(HttpStatus.OK.value())
                    .message("Cuenta eliminada con \u00E9xito").build());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseDto.<Long>builder()
                    .code(HttpStatus.NOT_FOUND.value()).message("La cuenta no existe").build());
        }
    }

    @NonNull
    private ResponseEntity<BaseResponseDto<AccountResponse>> getBaseResponseDtoResponseEntity(
            AccountResponse response) {
        if (response == null || response.getAccountId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseDto.<AccountResponse>builder()
                    .code(HttpStatus.NOT_FOUND.value()).message("Cuenta no encontrada").build());
        }
        return ResponseEntity.ok(BaseResponseDto.<AccountResponse>builder().code(HttpStatus.OK.value()).data(response)
                .message("Cuenta encontrada con \u00E9xito").build());
    }
}

package com.sofka.msa.controller;

import com.sofka.msa.dto.BaseResponseDto;
import com.sofka.msa.dto.request.MovementRequest;
import com.sofka.msa.dto.response.MovementResponse;
import com.sofka.msa.exception.ExceptionManager;
import com.sofka.msa.service.IMovementService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

import javax.validation.Valid;

@RestController()
@RequestMapping("/api/movements")
@Validated
@Slf4j
public class MovementController {

    @Autowired
    private IMovementService movementService;


    /**
     * Save movement
     *
     * @param request
     * @return
     */
    @Secured({"ROLE_ADMIN"})
    @PostMapping
    @Operation(summary = "Create movement")
    public ResponseEntity<BaseResponseDto<Object>> save(@Valid @RequestBody MovementRequest request) {
        try {
            MovementResponse response = movementService.processSaveMovement(request);
            if (response == null) {
                return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.builder().code(HttpStatus.OK.value()).message("La cuenta no existe").build());
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseDto.builder().code(HttpStatus.CREATED.value()).data(response).message("Movimiento creado con \u00E9xito").build());
        } catch (ExceptionManager.BalanceNotAvailableException b) {
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(BaseResponseDto.builder().code(HttpStatus.PAYMENT_REQUIRED.value()).message("Saldo no disponible").build());
        } catch (ExceptionManager e) {
            log.error("save: {0}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponseDto.builder().message(e.getMessage()).code(HttpStatus.INTERNAL_SERVER_ERROR.value()).build());
        }
    }

    /**
     * Delete movement by id
     *
     * @param id
     * @return
     */
    @Secured({"ROLE_ADMIN"})
    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Delete movement")
    public ResponseEntity<BaseResponseDto<Object>> deleteById(@PathVariable Long id) {
        try {
            if (this.movementService.deleteMovementById(id) >= 1) {
                return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.builder().code(HttpStatus.OK.value()).message("Movimiento eliminado con \u00E9xito").build());
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.builder().code(HttpStatus.OK.value()).message("El movimiento no existe").build());
            }
        } catch (ExceptionManager e) {
            log.error("deleteById", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponseDto.builder().message(e.getMessage()).code(HttpStatus.INTERNAL_SERVER_ERROR.value()).build());
        }
    }
}

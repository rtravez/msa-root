package com.sofka.msa.controller;

import com.sofka.msa.dto.BaseResponseDto;
import com.sofka.msa.dto.request.MovementRequest;
import com.sofka.msa.dto.response.MovementResponse;
import com.sofka.msa.service.IMovementService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController()
@RequestMapping("/api/movements")
@Validated
@Slf4j
@RequiredArgsConstructor
public class MovementController {

    private final IMovementService movementService;


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
        MovementResponse response = movementService.processSaveMovement(request);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.builder().code(HttpStatus.OK.value()).message("La cuenta no existe").build());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseDto.builder().code(HttpStatus.CREATED.value()).data(response).message("Movimiento creado con \u00E9xito").build());
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
        if (this.movementService.deleteMovementById(id) >= 1) {
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.builder().code(HttpStatus.OK.value()).message("Movimiento eliminado con \u00E9xito").build());
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.builder().code(HttpStatus.OK.value()).message("El movimiento no existe").build());
        }
    }
}

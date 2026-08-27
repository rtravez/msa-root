package com.rtravez.msa.controller.exception;

import com.rtravez.msa.dto.BaseResponseDto;
import com.rtravez.msa.exception.ExceptionManager;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ExceptionManager.ForeignException.class)
    public ResponseEntity<BaseResponseDto<Object>> handleForeignException(ExceptionManager.ForeignException ex) {
        log.error("ForeignException: {}", ex.getMessage());
        BaseResponseDto<Object> response = BaseResponseDto.builder()
                .code(HttpStatus.CONFLICT.value())
                .message("Existen movimientos para esta cuenta")
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(ExceptionManager.BalanceNotAvailableException.class)
    public ResponseEntity<BaseResponseDto<Object>> handleBalanceNotAvailableException(ExceptionManager.BalanceNotAvailableException ex) {
        log.error("BalanceNotAvailableException: {}", ex.getMessage());
        BaseResponseDto<Object> response = BaseResponseDto.builder()
                .code(HttpStatus.PAYMENT_REQUIRED.value())
                .message("Saldo no disponible")
                .build();
        return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).body(response);
    }

    @ExceptionHandler(ExceptionManager.MovementDeletionException.class)
    public ResponseEntity<BaseResponseDto<Object>> handleMovementDeletionException(
            ExceptionManager.MovementDeletionException ex) {
        log.warn("Movement deletion rejected: {}", ex.getMessage());
        BaseResponseDto<Object> response = BaseResponseDto.builder()
                .code(HttpStatus.CONFLICT.value())
                .message("No se puede anular un movimiento con movimientos posteriores")
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(ExceptionManager.class)
    public ResponseEntity<BaseResponseDto<Object>> handleExceptionManager(ExceptionManager ex) {
        log.error("ExceptionManager: {}", ex.getMessage(), ex);
        BaseResponseDto<Object> response = BaseResponseDto.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .message("Ocurrió un error al procesar la solicitud")
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponseDto<Object>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage()).toList();
        log.error("Validation error: {}", errors);
        BaseResponseDto<Object> response = BaseResponseDto.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Error de validación")
                .errors(errors)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BaseResponseDto<Object>> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("Constraint violation error: ", ex);
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage()).toList();

        BaseResponseDto<Object> response = BaseResponseDto.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Error de validación de parámetros")
                .errors(errors)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<BaseResponseDto<Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.warn("Data integrity violation", ex);
        BaseResponseDto<Object> response = BaseResponseDto.builder()
                .code(HttpStatus.CONFLICT.value())
                .message("La cuenta ya existe o los datos violan una restricción de integridad")
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponseDto<Object>> handleGenericException(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        BaseResponseDto<Object> response = BaseResponseDto.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Ocurrió un error inesperado en el servidor")
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<BaseResponseDto<Object>> handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage());

        BaseResponseDto<Object> response = BaseResponseDto.builder()
                .code(HttpStatus.FORBIDDEN.value())
                .message("No tienes permisos para realizar esta operación")
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
}

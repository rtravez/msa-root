package com.sofka.msa.controller.exception;

import com.sofka.msa.dto.BaseResponseDto;
import com.sofka.msa.exception.ExceptionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(ExceptionManager.class)
    public ResponseEntity<BaseResponseDto<Object>> handleExceptionManager(ExceptionManager ex) {
        log.error("ExceptionManager: {}", ex.getMessage(), ex);
        BaseResponseDto<Object> response = BaseResponseDto.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage())
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

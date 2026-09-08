package com.rtravez.msa.controller.exception;

import com.rtravez.msa.exception.ExceptionManager;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
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
        public ResponseEntity<ProblemDetail> handleForeignException(ExceptionManager.ForeignException ex) {
        log.error("ForeignException: {}", ex.getMessage());
                return problem(HttpStatus.CONFLICT, "Existen movimientos para esta cuenta");
    }

    @ExceptionHandler(ExceptionManager.BalanceNotAvailableException.class)
        public ResponseEntity<ProblemDetail> handleBalanceNotAvailableException(
                        ExceptionManager.BalanceNotAvailableException ex) {
        log.error("BalanceNotAvailableException: {}", ex.getMessage());
                return problem(HttpStatus.PAYMENT_REQUIRED, "Saldo no disponible");
    }

    @ExceptionHandler(ExceptionManager.MovementDeletionException.class)
        public ResponseEntity<ProblemDetail> handleMovementDeletionException(
            ExceptionManager.MovementDeletionException ex) {
        log.warn("Movement deletion rejected: {}", ex.getMessage());
                return problem(HttpStatus.CONFLICT, "No se puede anular un movimiento con movimientos posteriores");
    }

        @ExceptionHandler(ExceptionManager.ServiceUnavailableException.class)
        public ResponseEntity<ProblemDetail> handleServiceUnavailableException(
                        ExceptionManager.ServiceUnavailableException ex) {
                log.error("Service unavailable: {}", ex.getMessage(), ex);
                return problem(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage());
        }

    @ExceptionHandler(ExceptionManager.class)
        public ResponseEntity<ProblemDetail> handleExceptionManager(ExceptionManager ex) {
        log.error("ExceptionManager: {}", ex.getMessage(), ex);
                return problem(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error al procesar la solicitud");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ProblemDetail> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage()).toList();
        log.error("Validation error: {}", errors);
                return problem(HttpStatus.BAD_REQUEST, "Error de validación", errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<ProblemDetail> handleConstraintViolationException(ConstraintViolationException ex) {
        log.error("Constraint violation error: ", ex);
        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage()).toList();

                return problem(HttpStatus.BAD_REQUEST, "Error de validación de parámetros", errors);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<ProblemDetail> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        log.warn("Data integrity violation", ex);
                return problem(HttpStatus.CONFLICT, "La cuenta ya existe o los datos violan una restricción de integridad");
    }

    @ExceptionHandler(Exception.class)
        public ResponseEntity<ProblemDetail> handleGenericException(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
                return problem(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado en el servidor");
    }

    @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<ProblemDetail> handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage());
                return problem(HttpStatus.FORBIDDEN, "No tienes permisos para realizar esta operación");
        }

        private ResponseEntity<ProblemDetail> problem(HttpStatus status, String detail) {
                ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(status.value()), detail);
                problem.setTitle(status.getReasonPhrase());
                return ResponseEntity.status(status).body(problem);
        }

        private ResponseEntity<ProblemDetail> problem(HttpStatus status, String detail, List<String> errors) {
                ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(status.value()), detail);
                problem.setTitle(status.getReasonPhrase());
                problem.setProperty("errors", errors);
                return ResponseEntity.status(status).body(problem);
    }
}

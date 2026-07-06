package com.sofka.msa.controller;

import com.sofka.msa.dto.BaseResponseDto;
import com.sofka.msa.dto.response.MovementReportResponse;
import com.sofka.msa.exception.ExceptionManager;
import com.sofka.msa.service.IMovementService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/api/reports")
@Validated
@Slf4j
public class ReportController {

    @Autowired
    private IMovementService movementService;


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
    @Operation(summary = "Find movements")
    public ResponseEntity<BaseResponseDto<Object>> findMovementByDateAndIdentification(@RequestParam("initialDate") String initialDate,
                                                                                       @RequestParam("finalDate") String finalDate,
                                                                                       @RequestParam("identification") String identification,
                                                                                       @RequestParam("accountType") String accountType) {
        try {
            List<MovementReportResponse> responses = movementService.findMovementByDateAndIdentification(initialDate, finalDate, identification, accountType);
            if (responses.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.builder().code(HttpStatus.OK.value()).message("No existen movimientos").build());
            }

            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.builder().code(HttpStatus.OK.value()).data(responses).message("Movimientos encontrados con \u00E9xito").build());
        } catch (ExceptionManager e) {
            log.error("findMovementByDateAndIdentification", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(BaseResponseDto.builder().message(e.getMessage()).code(HttpStatus.INTERNAL_SERVER_ERROR.value()).build());
        }
    }

}

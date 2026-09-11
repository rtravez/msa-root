package com.rtravez.msa.controller.common;

import com.rtravez.msa.dto.BaseResponseDto;
import com.rtravez.msa.service.common.ValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/validations")
@Validated
@Slf4j
@Tag(name = "Validaciones", description = "Validación de números de identificación ecuatorianos")
@SecurityRequirement(name = "bearerAuth")
public class ValidationController {

	private final ValidationService service;

	public ValidationController(ValidationService service) {
		this.service = service;
	}

	@GetMapping(path = "identification/{identification}")
	@Operation(
			summary = "Validar identificación",
			description = "Verifica si el número de cédula proporcionado tiene un formato válido. Requiere un JWT válido."
	)
	@ApiResponse(responseCode = "200", description = "Resultado de la validación de identificación; `data` es true o false")
	@ApiResponse(responseCode = "401", description = "Token JWT ausente o inválido")
	public ResponseEntity<BaseResponseDto<Boolean>> validationIdentification(
			@Parameter(description = "Número de cédula que se desea validar", example = "1710034065", required = true)
			@PathVariable String identification) {
		return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.<Boolean>builder().status(HttpStatus.OK.value())
				.data(service.validationIdentification(identification)).detail("La identificación ha sido validado con \u00E9xito").build());
	}

	@GetMapping(path = "ruc/{ruc}")
	@Operation(
			summary = "Validar RUC",
			description = "Verifica si el número de RUC proporcionado tiene un formato válido. Requiere un JWT válido."
	)
	@ApiResponse(responseCode = "200", description = "Resultado de la validación de RUC; `data` es true o false")
	@ApiResponse(responseCode = "401", description = "Token JWT ausente o inválido")
	public ResponseEntity<BaseResponseDto<Boolean>> validationRuc(
			@Parameter(description = "Número de RUC que se desea validar", example = "1790016919001", required = true)
			@PathVariable String ruc) {
		return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.<Boolean>builder().status(HttpStatus.OK.value()).data(service.validationRuc(ruc))
				.detail("El ruc ha sido validado con \u00E9xito").build());
	}

}

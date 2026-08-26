package com.rtravez.msa.controller.common;

import com.rtravez.msa.dto.BaseResponseDto;
import com.rtravez.msa.service.common.IValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ValidationController {

	@Autowired
	private IValidationService service;

	@GetMapping(path = "identification/{identification}")
	public ResponseEntity<BaseResponseDto<Object>> validationIdentification(@PathVariable String identification) {
		return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.builder().code(HttpStatus.OK.value())
				.data(service.validationIdentification(identification)).message("La identificación ha sido validado con \u00E9xito").build());
	}

	@GetMapping(path = "ruc/{ruc}")
	public ResponseEntity<BaseResponseDto<Object>> validationRuc(@PathVariable String ruc) {
		return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.builder().code(HttpStatus.OK.value()).data(service.validationRuc(ruc))
				.message("El ruc ha sido validado con \u00E9xito").build());
	}

}

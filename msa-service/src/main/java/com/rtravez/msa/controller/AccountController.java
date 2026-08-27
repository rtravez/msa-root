package com.rtravez.msa.controller;

import com.rtravez.msa.dto.BaseResponseDto;
import com.rtravez.msa.dto.request.AccountRequest;
import com.rtravez.msa.dto.response.AccountResponse;
import com.rtravez.msa.service.IAccountService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController()
@RequestMapping("/api/accounts")
@Validated
@Slf4j
@RequiredArgsConstructor
public class AccountController {

    private final IAccountService accountService;

    /**
     * Find account all
     *
     * @return
     */
    @GetMapping
    @Secured({"ROLE_ADMIN"})
    @Operation(summary = "Find account")
    public ResponseEntity<BaseResponseDto<Object>> findAccountAll() {
        List<AccountResponse> accountResponses = accountService.findAccountAll();
        if (accountResponses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.builder().code(HttpStatus.OK.value()).message("No existen cuentas").build());
        }

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.builder().code(HttpStatus.OK.value()).data(accountResponses).message("Cuentas encontradas con \u00E9xito").build());
    }


    /**
     * Save account
     *
     * @param request
     * @return
     */
    @Secured({"ROLE_ADMIN"})
    @PostMapping
    @Operation(summary = "Create account")
    public ResponseEntity<BaseResponseDto<Object>> save(@Valid @RequestBody AccountRequest request) {
        if (Boolean.TRUE.equals(this.accountService.exist(request.getAccountNumber()))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(BaseResponseDto.builder().code(HttpStatus.CONFLICT.value()).message("La cuenta ya existe").build());
        }

        if (!Boolean.TRUE.equals(this.accountService.existUser(request.getIdentification()))) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseDto.builder().code(HttpStatus.NOT_FOUND.value()).message("El usuario no existe").build());
        }

        AccountResponse response = accountService.processSaveAccount(request);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseDto.builder().code(HttpStatus.NOT_FOUND.value()).message("La cuenta no existe").build());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseDto.builder().code(HttpStatus.CREATED.value()).data(response).message("Cuenta creada con \u00E9xito").build());
    }

    /**
     * Update account
     *
     * @param request
     * @return
     */
    @Secured({"ROLE_ADMIN"})
    @PutMapping
    @Operation(summary = "Update account")
    public ResponseEntity<BaseResponseDto<Object>> update(@Valid @RequestBody AccountRequest request) {
        AccountResponse response = accountService.processUpdateAccount(request);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseDto.builder().code(HttpStatus.NOT_FOUND.value()).message("La cuenta no existe").build());
        }

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.builder().code(HttpStatus.OK.value()).data(response).message("Cuenta actualizada con \u00E9xito").build());
    }

    /**
     * Delete account
     *
     * @param id
     * @return
     */
    @Secured({"ROLE_ADMIN"})
    @DeleteMapping(path = "/{id}")
    @Operation(summary = "Delete account")
    public ResponseEntity<BaseResponseDto<Object>> deleteById(@PathVariable Long id) {
        if (this.accountService.deleteAccountById(id) >= 1) {
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.builder().code(HttpStatus.OK.value()).message("Cuenta eliminada con \u00E9xito").build());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseDto.builder().code(HttpStatus.NOT_FOUND.value()).message("La cuenta no existe").build());
        }
    }
}

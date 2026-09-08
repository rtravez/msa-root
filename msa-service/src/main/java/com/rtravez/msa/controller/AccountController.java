package com.rtravez.msa.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.rtravez.msa.dto.BaseResponseDto;
import com.rtravez.msa.dto.request.AccountRequest;
import com.rtravez.msa.dto.response.AccountResponse;
import com.rtravez.msa.dto.response.UserResponse;
import com.rtravez.msa.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController()
@RequestMapping("/api/accounts")
@Validated
@Slf4j
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    /**
     * Find account all
     *
     * @return
     */
    @GetMapping
    @Secured({"ROLE_ADMIN"})
    @Operation(summary = "Find account")
    public ResponseEntity<BaseResponseDto<Page<AccountResponse>>> findAccountAll(@PageableDefault(size = 20) Pageable pageable) {
        Page<AccountResponse> accountResponses = accountService.findAccountAll(pageable);
        if (accountResponses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.<Page<AccountResponse>>builder().code(HttpStatus.OK.value()).message("No existen cuentas").build());
        }

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.<Page<AccountResponse>>builder().code(HttpStatus.OK.value()).data(accountResponses).message("Cuentas encontradas con \u00E9xito").build());
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
    public ResponseEntity<BaseResponseDto<AccountResponse>> save(@Valid @RequestBody AccountRequest request) {
        if (Boolean.TRUE.equals(this.accountService.exist(request.getAccountNumber()))) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(BaseResponseDto.<AccountResponse>builder().code(HttpStatus.CONFLICT.value()).message("La cuenta ya existe").build());
        }

        UserResponse userResponse = accountService.findUserByIdentification(request.getIdentification());
        if (userResponse == null || userResponse.getUserId() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseDto.<AccountResponse>builder().code(HttpStatus.NOT_FOUND.value()).message("El usuario no existe").build());
        }

        AccountResponse response = accountService.processSaveAccount(request, userResponse);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseDto.<AccountResponse>builder().code(HttpStatus.NOT_FOUND.value()).message("La cuenta no existe").build());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponseDto.<AccountResponse>builder().code(HttpStatus.CREATED.value()).data(response).message("Cuenta creada con \u00E9xito").build());
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
    public ResponseEntity<BaseResponseDto<AccountResponse>> update(@Valid @RequestBody AccountRequest request) {
        AccountResponse response = accountService.processUpdateAccount(request);
        if (response == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseDto.<AccountResponse>builder().code(HttpStatus.NOT_FOUND.value()).message("La cuenta no existe").build());
        }

        return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.<AccountResponse>builder().code(HttpStatus.OK.value()).data(response).message("Cuenta actualizada con \u00E9xito").build());
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
    public ResponseEntity<BaseResponseDto<Long>> deleteById(@PathVariable Long id) {
        if (this.accountService.deleteAccountById(id) >= 1) {
            return ResponseEntity.status(HttpStatus.OK).body(BaseResponseDto.<Long>builder().code(HttpStatus.OK.value()).message("Cuenta eliminada con \u00E9xito").build());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(BaseResponseDto.<Long>builder().code(HttpStatus.NOT_FOUND.value()).message("La cuenta no existe").build());
        }
    }
}

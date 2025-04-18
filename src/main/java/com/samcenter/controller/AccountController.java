package com.samcenter.controller;

import com.samcenter.controller.request.AccountRequest;
import com.samcenter.controller.request.PasswordChangeRequest;
import com.samcenter.controller.request.ResetPasswordRequest;
import com.samcenter.controller.response.AccountResponse;
import com.samcenter.controller.response.ApiResponse;
import com.samcenter.controller.response.PageResponse;
import com.samcenter.entity.Account;
import com.samcenter.mapper.AccountMapper;
import com.samcenter.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Tag(name = "Account Controller")
@Slf4j(topic = "ACCOUNT-CONTROLLER")
@Validated
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @Operation(summary = "Get Account List", description = "API retrieve account from database")
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<AccountResponse>>> getAllAccount(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sort,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        log.info("get all account");

        Page<Account> account = accountService.getAll(keyword, sort, page, size);
        Page<AccountResponse> response = account.map(accountMapper::toAccountResponse);
        return ResponseEntity.ok(ApiResponse.success(new PageResponse<>(response), "Account list retrieved successfully"));
    }

    @Operation(summary = "Get Account Detail", description = "API retrieve account detail by ID from database")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AccountResponse>> getAccount(@PathVariable Integer id) {
        log.info("get account");

        Account account = accountService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(accountMapper.toAccountResponse(account), "Account retrieved successfully"));
    }

    @Operation(summary = "Create Account", description = "API add new account to database")
    @PostMapping
    public ResponseEntity<ApiResponse<AccountResponse>> createAccount(@Valid @RequestBody AccountRequest request) {
        log.info("create account");

        Account account = accountMapper.toAccount(request);
        Account savedAccount = accountService.save(account);
        return ResponseEntity.ok(ApiResponse.success(accountMapper.toAccountResponse(savedAccount), "Account created successfully"));
    }

    @Operation(summary = "Update Account", description = "API update account to database")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateAccount(@PathVariable Integer id, @RequestBody AccountRequest request) {
        log.info("update account");

        Account account = accountService.getById(id);
        accountMapper.updateAccount(account, request);
        accountService.save(account);
        return ResponseEntity.ok(ApiResponse.success(null, "Account updated successfully"));
    }

    @Operation(summary = "Forgot Password", description = "Send reset password email to user")
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@RequestParam String email) {
        log.info("forgot password for email: {}", email);

        accountService.forgotPassword(email);
        return ResponseEntity.ok(ApiResponse.success(null, "Reset password link sent to your email"));
    }

    @Operation(summary = "Reset Password", description = "Reset password by token")
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        log.info("reset password");

        accountService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok(ApiResponse.success(null, "Password reset successfully"));
    }

    @Operation(summary = "Change Password", description = "API change password for user to database")
    @PatchMapping("/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@RequestBody @Valid PasswordChangeRequest request) {
        log.info("change password");

        accountService.updatePassword(request);
        return ResponseEntity.ok(ApiResponse.success(null, "Account change password successfully"));
    }

    @Operation(summary = "Delete Account", description = "API delete account to database")
    @PreAuthorize("hasRole('DIRECTOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAccount(@PathVariable Integer id) {
        log.info("delete account");

        accountService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Account deleted successfully"));
    }

//    @GetMapping("/info")
//    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal Jwt jwt) {
//        if (jwt == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//
//        Map<String, Object> data = new HashMap<>();
//        data.put("id", jwt.getClaim("id"));
//        data.put("username", jwt.getClaim("sub"));
//        data.put("role", jwt.getClaim("role"));
//        return ResponseEntity.ok(ApiResponse.success(data, "Account info successfully"));
//    }
}

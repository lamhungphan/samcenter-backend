package com.samcenter.unit;

import com.samcenter.controller.request.PasswordChangeRequest;
import com.samcenter.entity.Account;
import com.samcenter.repository.AccountRepository;
import com.samcenter.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor
class PasswordTest {

    private AccountService service;
    private AccountRepository repo;
    private PasswordChangeRequest request;
    private PasswordEncoder encoder;

    private final String testEmail = "user@example.com";

    @BeforeEach
    void setUp() {
        if (!repo.existsByEmail(testEmail)){
            Account account = new Account();
            account.setEmail(testEmail);
            account.setPassword("password");
            repo.save(account);
        }
    }

//    @Test
//    void testForgotPassword_EmailExists() {
//        service.forgotPassword(testEmail);
//        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByUserEmail(testEmail);
//        assertTrue(tokenOpt.isPresent(), "Token phải được tạo");
//    }

    @Test
    void testForgotPassword_EmailNotExist() {
        String fakeEmail = "nonexistent@example.com";
        assertThrows(RuntimeException.class, () -> {
            service.forgotPassword(fakeEmail);
        });
    }

//    @Test
//    void testResetPassword_ValidToken() {
//        service.forgotPassword(testEmail);
//        PasswordResetToken token = tokenRepository.findByAccountEmail(testEmail).orElseThrow();
//        AccountService.resetPassword(token.getToken(), "newPassword123");
//
//        Account updatedUser = repo.findByEmail(testEmail).orElseThrow();
//        assertNotEquals("oldPassword", updatedUser.getPassword());
//    }

    @Test
    void testResetPassword_InvalidToken() {
        assertThrows(RuntimeException.class, () -> {
            service.resetPassword("invalid-token", "newPassword");
        });
    }

    @Test
    void testUpdatePassword_Success() {
        PasswordChangeRequest request = new PasswordChangeRequest();
        request.setOldPassword("1234");
        request.setNewPassword("4321");

        service.updatePassword(request);

        Account account = repo.findByUsername("user2");
        assertTrue(encoder.matches("newPass123", account.getPassword()));
    }

}

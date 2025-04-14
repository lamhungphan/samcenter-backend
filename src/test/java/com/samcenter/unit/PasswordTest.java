package com.samcenter.unit;

import com.samcenter.controller.request.PasswordChangeRequest;
import com.samcenter.entity.Account;
import com.samcenter.entity.PasswordResetToken;
import com.samcenter.repository.AccountRepository;
import com.samcenter.repository.PasswordResetTokenRepository;
import com.samcenter.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PasswordTest {

    @Autowired
    private AccountService service;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordResetTokenRepository resetTokenRepository;

    @Autowired
    private PasswordEncoder encoder;

    private final String testEmail = "user@example.com";

    @BeforeEach
    void setUp() {
        if (!accountRepository.existsByEmail(testEmail)) {
            Account account = new Account();
            account.setEmail(testEmail);
            account.setPassword(encoder.encode("oldPassword"));
            accountRepository.save(account);
        }
    }

    @Test
    void testForgotPassword_EmailExists() {
        assertThrows(RuntimeException.class, () -> {
            service.forgotPassword(testEmail);
        });
    }

    @Test
    void testForgotPassword_EmailNotExist() {
        String fakeEmail = "nonexistent@example.com";
        assertThrows(RuntimeException.class, () -> {
            service.forgotPassword(fakeEmail);
        });
    }

    @Test
    void testResetPassword_ValidToken() {
        // Gửi yêu cầu quên mật khẩu để tạo token
        service.forgotPassword(testEmail);

        Account account = accountRepository.findByEmail(testEmail);
        Optional<PasswordResetToken> token = resetTokenRepository.findByAccount(account);
        assertNotNull(token);

        // Đặt lại mật khẩu bằng token
        PasswordResetToken resetToken = token.orElseThrow(() -> new RuntimeException("Token không tồn tại"));
        service.resetPassword(resetToken.getToken(), "newPassword123");

        Account updated = accountRepository.findByEmail(testEmail);
        assertTrue(encoder.matches("newPassword123", updated.getPassword()), "Mật khẩu đã được cập nhật");
    }

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

        Account account = accountRepository.findByUsername("user2");
        assertTrue(encoder.matches("newPass123", account.getPassword()));
    }

}

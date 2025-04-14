package com.samcenter.unit;

import com.samcenter.entity.Account;
import com.samcenter.repository.AccountRepository;
import com.samcenter.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestRegister {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void testRegisterSuccess() {
        Account newAccount = new Account();
        newAccount.setUsername("newuser");
        newAccount.setEmail("new@example.com");
        newAccount.setPassword("password");

        // Mock các phương thức trả về boolean đúng kiểu
        when(accountRepository.existsByUsername("newuser")).thenReturn(false); // Chưa có user này
        when(accountRepository.existsByEmail("new@example.com")).thenReturn(false); // Chưa có email này
        when(passwordEncoder.encode("password")).thenReturn("encoded-password");
        when(accountRepository.save(any(Account.class))).thenReturn(newAccount);

        // Gọi phương thức save từ AccountService
        Account savedAccount = accountService.save(newAccount);

        // Kiểm tra kết quả
        assertNotNull(savedAccount);
        assertEquals("encoded-password", savedAccount.getPassword());
        verify(accountRepository).save(newAccount); // Kiểm tra gọi phương thức save
    }

    @Test
    void testRegisterDuplicateUsername() {
        Account account = new Account();
        account.setUsername("existingUser");
        account.setEmail("any@example.com");

        // Mock để giả lập username đã tồn tại
        when(accountRepository.existsByUsername("existingUser")).thenReturn(true); // User đã tồn tại

        assertThrows(RuntimeException.class, () -> accountService.save(account)); // Lỗi nếu username đã tồn tại
    }

    @Test
    void testRegisterDuplicateEmail() {
        Account account = new Account();
        account.setUsername("newuser");
        account.setEmail("existingEmail@example.com");

        // Mock để giả lập email đã tồn tại
        when(accountRepository.existsByUsername("newuser")).thenReturn(false); // Username chưa tồn tại
        when(accountRepository.existsByEmail("existingEmail@example.com")).thenReturn(true); // Email đã tồn tại

        assertThrows(RuntimeException.class, () -> accountService.save(account)); // Lỗi nếu email đã tồn tại
    }

}

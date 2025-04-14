package com.samcenter.service.impl;

import com.samcenter.common.SecurityUtil;
import com.samcenter.controller.request.AccountRequest;
import com.samcenter.controller.request.PasswordChangeRequest;
import com.samcenter.entity.Account;
import com.samcenter.entity.PasswordResetToken;
import com.samcenter.entity.Role;
import com.samcenter.exception.ResourceNotFoundException;
import com.samcenter.repository.AccountRepository;
import com.samcenter.repository.PasswordResetTokenRepository;
import com.samcenter.service.AbstractService;
import com.samcenter.service.AccountService;
import com.github.dockerjava.api.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AccountServiceImpl extends AbstractService<Account, Integer, AccountRequest> implements AccountService {
    @Value("${cors.allowed-origins}")
    private String frontend_url;

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository tokenRepository;
    private final JavaMailSender mailSender;

    public AccountServiceImpl(AccountRepository accountRepository,
                              PasswordEncoder passwordEncoder,
                              PasswordResetTokenRepository tokenRepository,
                              JavaMailSender mailSender) {
        super(accountRepository);
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepository = tokenRepository;
        this.mailSender = mailSender;
    }

    @Override
    public Account findByUsername(String username) {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new ResourceNotFoundException("Account not found with username: " + username);
        }
        return account;
    }

    @Override
    public Account findByEmail(String email) {
        Account account = accountRepository.findByEmail(email);
        if (account == null) {
            throw new ResourceNotFoundException("Account not found with email: " + email);
        }
        return account;
    }

    @Override
    public void updatePassword(PasswordChangeRequest request) {
        String username = SecurityUtil.getCurrentUsername();

        // Tìm account theo username
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new ResourceNotFoundException("User not found");
        }

        // So sánh mật khẩu cũ (đã mã hoá)
        if (!passwordEncoder.matches(request.getOldPassword(), account.getPassword())) {
            throw new BadRequestException("Password does not match");
        }

        account.setPassword(passwordEncoder.encode(request.getNewPassword()));
        accountRepository.save(account);
    }

    @Override
    @Transactional
    public void forgotPassword(String email) {
        Account account = accountRepository.findByEmail(email);
        if (account == null) {
            throw new ResourceNotFoundException("email not found");
        }

        // Xoá token cũ nếu có
        tokenRepository.deleteByAccount(account);

        // Tạo token mới
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setAccount(account);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        tokenRepository.save(resetToken);

        // Tạo đường dẫn reset
        String resetUrl = frontend_url + "/reset-password?token=" + token;

        // Gửi email
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setSubject("Reset your password");
        mail.setText("Click the link to reset your password: " + resetUrl);
        mailSender.send(mail);
    }


    @Override
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token);

        if (resetToken == null || resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Token invalid or expired");
        }

        Account account = resetToken.getAccount();
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);

        // Xoá token sau khi dùng
        tokenRepository.delete(resetToken);
    }


    @Override
    public Account save(Account account) {
        // Kiểm tra xem username đã tồn tại chưa
        if (accountRepository.existsByUsername(account.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // Kiểm tra xem email đã tồn tại chưa
        if (accountRepository.existsByEmail(account.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Mã hóa mật khẩu
        account.setPassword(passwordEncoder.encode(account.getPassword()));

        // Gán role mặc định là CUSTOMER (hoặc có thể thay đổi nếu cần)
        if (account.getRole() == null) {
            account.setRole(Role.CUSTOMER);
        }

        // Lưu account vào cơ sở dữ liệu và trả về đối tượng vừa lưu
        return super.save(account);
    }

    @Override
    public void update(AccountRequest request) {

    }
}

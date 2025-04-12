package com.samcenter.repository;

import com.samcenter.entity.Account;
import com.samcenter.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
    void deleteByAccount(Account account);
}


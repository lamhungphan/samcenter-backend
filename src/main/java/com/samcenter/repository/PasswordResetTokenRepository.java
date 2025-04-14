package com.samcenter.repository;

import com.samcenter.entity.Account;
import com.samcenter.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    PasswordResetToken findByToken(String token);
    void deleteByAccount(Account account);
    Optional<PasswordResetToken> findByAccount(Account account);
}


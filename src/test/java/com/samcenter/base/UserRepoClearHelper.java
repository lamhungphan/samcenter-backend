package com.samcenter.base;

import com.samcenter.repository.AccountRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

@Component
public class UserRepoClearHelper {

    private final AccountRepository accountRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public UserRepoClearHelper(AccountRepository repository) {
        this.accountRepository = repository;
    }

    public void clearAllUsersAndResetAutoIncrement() {
        try {
            accountRepository.deleteAll();
            entityManager.createNativeQuery("ALTER TABLE users AUTO_INCREMENT = 1").executeUpdate();
        } catch (Exception e) {
            System.err.println("Không thể reset AUTO_INCREMENT hoặc xóa dữ liệu: " + e.getMessage());
        }
    }
}


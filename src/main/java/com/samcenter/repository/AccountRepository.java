package com.samcenter.repository;

import com.samcenter.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
   Account findByUsername(String username);
   Account findByEmail(String email);
}

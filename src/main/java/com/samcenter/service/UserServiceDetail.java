package com.samcenter.service;

import com.samcenter.repository.AccountRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public record UserServiceDetail(AccountRepository accountRepository) {

    public UserDetailsService getAccountDetailsService() {
        return accountRepository::findByUsername;
    }
}

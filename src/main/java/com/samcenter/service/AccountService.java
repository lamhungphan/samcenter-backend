package com.samcenter.service;

import com.samcenter.controller.request.AccountRequest;
import com.samcenter.controller.request.PasswordChangeRequest;
import com.samcenter.entity.Account;

public interface AccountService extends BaseService<Account, Integer, AccountRequest> {
    Account findByUsername(String username);
    Account findByEmail(String email);
    void updatePassword(PasswordChangeRequest request);
}

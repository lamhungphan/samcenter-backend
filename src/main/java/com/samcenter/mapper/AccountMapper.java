package com.samcenter.mapper;

import com.samcenter.controller.request.AccountRequest;
import com.samcenter.controller.request.PasswordChangeRequest;
import com.samcenter.controller.response.AccountResponse;
import com.samcenter.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account toAccount(AccountRequest request);

    AccountResponse toAccountResponse(Account account);

    void updateAccount(@MappingTarget Account account, AccountRequest request);

    void updatePassword(@MappingTarget Account account, PasswordChangeRequest request);

}

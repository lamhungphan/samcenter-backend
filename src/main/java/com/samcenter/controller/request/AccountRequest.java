package com.samcenter.controller.request;

import com.samcenter.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequest {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String phone;
    private String address;
    private Role role;

    public AccountRequest(String newUser, String newPassword) {
    }
}

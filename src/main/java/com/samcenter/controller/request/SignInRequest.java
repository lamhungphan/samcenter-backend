package com.samcenter.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SignInRequest implements Serializable {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String platform; // web, mobile, tablet
    private String deviceToken; // for push notify
    private String versionApp;

}

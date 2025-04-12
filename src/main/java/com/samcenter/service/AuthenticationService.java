package com.samcenter.service;

import com.samcenter.controller.request.SignInRequest;
import com.samcenter.controller.response.TokenResponse;

public interface AuthenticationService {
    TokenResponse getAccessToken(SignInRequest request);

    TokenResponse getRefreshToken(String request);
}

package com.samcenter.service.impl;

import com.samcenter.controller.request.SignInRequest;
import com.samcenter.controller.response.AccountResponse;
import com.samcenter.controller.response.TokenResponse;
import com.samcenter.entity.Account;
import com.samcenter.exception.ForBiddenException;
import com.samcenter.exception.InvalidDataException;
import com.samcenter.repository.AccountRepository;
import com.samcenter.service.AuthenticationService;
import com.samcenter.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static com.samcenter.common.TokenType.REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "AUTHENTICATION-SERVICE")
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AccountRepository accountRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public TokenResponse getAccessToken(SignInRequest request) {
        log.info("Get access token");

        List<String> authorities = new ArrayList<>();
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            log.info("isAuthenticated = {}", authenticate.isAuthenticated());
            log.info("Authorities: {}", authenticate.getAuthorities().toString());

            // Lưu quyền
            authenticate.getAuthorities().forEach(auth -> authorities.add(auth.getAuthority()));

            // Gán vào SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authenticate);
        } catch (BadCredentialsException | DisabledException e) {
            log.error("Login fail, message={}", e.getMessage());
            throw new AccessDeniedException(e.getMessage());
        }

        // Lấy thông tin account
        Account account = accountRepository.findByUsername(request.getUsername());

        // Tạo token
        String accessToken = jwtService.generateAccessToken(request.getUsername(), authorities);
        String refreshToken = jwtService.generateRefreshToken(request.getUsername(), authorities);

        // Map sang AccountResponse
        AccountResponse accountResponse = AccountResponse.builder()
                .id(account.getId())
                .username(account.getUsername())
                .email(account.getEmail())
                .build();

        // Trả kết quả
        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .account(accountResponse)
                .build();
    }

    @Override
    public TokenResponse getRefreshToken(String request) {
        log.info("Get refresh token");

        if (!StringUtils.hasLength(request)) {
            throw new InvalidDataException("Token must be not blank");
        }

        try {
            // Verify token
            String userName = jwtService.extractUsername(request, REFRESH_TOKEN);

            // check user is active or inactivated
            Account user = accountRepository.findByUsername(userName);
            List<String> authorities = new ArrayList<>();
            user.getAuthorities().forEach(authority -> authorities.add(authority.getAuthority()));

            // generate new access token
            String accessToken = jwtService.generateAccessToken(user.getUsername(), authorities);

            return TokenResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(request)
                    .account(AccountResponse.builder()
                            .id(user.getId())
                            .username(user.getUsername())
                            .email(user.getEmail())
                            .build())
                    .build();
        } catch (Exception e) {
            log.error("Access denied! errorMessage: {}", e.getMessage());
            throw new ForBiddenException(e.getMessage());
        }
    }
}

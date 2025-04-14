package com.samcenter.service;

import com.samcenter.repository.AccountRepository;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler { // tạo token, dựa trên thông tin OAuth2User đã được xử lý từ OAuth2UserService


    private final JwtService jwtService;
    private final AccountRepository repository;

    @Value("${cors.allowed-origins}")
    private String frontend_url;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, java.io.IOException {
        // Lấy thông tin user từ Google
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        Integer id = oAuth2User.getAttribute("id");

        List<String> authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String accessToken = jwtService.generateAccessToken(email, id, authorities);
        String refreshToken = jwtService.generateRefreshToken(email, id, authorities);

        // Redirect về frontend cùng với accessToken
        String targetUrl = UriComponentsBuilder.fromUriString(frontend_url)
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .build().toUriString();

        response.sendRedirect(targetUrl);
    }
}

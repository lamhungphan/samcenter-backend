package com.samcenter.service;

import com.samcenter.entity.Account;
import com.samcenter.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService { //  kiểm tra và xác thực user từ DB sau khi login Google

    private final AccountRepository repository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        Optional<Account> userOpt = Optional.ofNullable(repository.findByEmail(email));

        if (userOpt.isEmpty()) {
            throw new OAuth2AuthenticationException("User not found in database");
        }

        // Optional: Bạn có thể đồng bộ các field khác
        Account account = userOpt.get();

        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        attributes.put("roles", account.getRole());

        return new DefaultOAuth2User(
                AuthorityUtils.createAuthorityList("ROLE_" + account.getRole()),
                attributes,
                "email"
        );
    }
}


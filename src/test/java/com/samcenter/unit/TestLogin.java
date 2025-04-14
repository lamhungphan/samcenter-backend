package com.samcenter.unit;

import com.samcenter.SamcenterApplication;
import com.samcenter.controller.request.SignInRequest;
import com.samcenter.controller.response.TokenResponse;
import com.samcenter.entity.Account;
import com.samcenter.entity.Role;
import com.samcenter.repository.AccountRepository;
import com.samcenter.service.JwtService;
import com.samcenter.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestLogin {

	@Mock
	private AccountRepository accountRepository;

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private JwtService jwtService;

	@InjectMocks
	private AuthenticationServiceImpl authService;

	@Test
	void testLoginSuccess() {
		// Arrange
		SignInRequest request = new SignInRequest();
		request.setUsername("staff");
		request.setPassword("123");

		List<GrantedAuthority> authorities = List.of(() -> "ROLE_USER");
		Authentication mockAuthentication = new UsernamePasswordAuthenticationToken(
				request.getUsername(), request.getPassword(), authorities
		);

		when(authenticationManager.authenticate(any())).thenReturn(mockAuthentication);

		Account mockAccount = new Account();
		mockAccount.setId(1);
		mockAccount.setUsername("staff");
		mockAccount.setRole(Role.STAFF);
		when(accountRepository.findByUsername("staff")).thenReturn(mockAccount);

		when(jwtService.generateAccessToken(anyString(), anyInt(), anyList())).thenReturn("mockAccessToken");
		when(jwtService.generateRefreshToken(anyString(), anyInt(), anyList())).thenReturn("mockRefreshToken");

		// Act
		TokenResponse response = authService.getAccessToken(request);

		// Assert
		assertNotNull(response);
		assertEquals("mockAccessToken", response.getAccessToken());
		assertEquals("mockRefreshToken", response.getRefreshToken());
	}

	@Test
	void testLoginWithWrongPassword() {
		// Arrange
		SignInRequest request = new SignInRequest();
		request.setUsername("staff");
		request.setPassword("wrongpass");

		when(authenticationManager.authenticate(any()))
				.thenThrow(new BadCredentialsException("Bad credentials"));

		// Act & Assert
		AccessDeniedException ex = assertThrows(AccessDeniedException.class,
				() -> authService.getAccessToken(request));

		assertEquals("Bad credentials", ex.getMessage());
	}

	@Test
	void testLoginWithNonExistentUser() {
		// Arrange
		SignInRequest request = new SignInRequest();
		request.setUsername("nonexistent");
		request.setPassword("123");

		List<GrantedAuthority> authorities = List.of(() -> "ROLE_USER");
		Authentication mockAuth = new UsernamePasswordAuthenticationToken(
				request.getUsername(), request.getPassword(), authorities
		);
		when(authenticationManager.authenticate(any())).thenReturn(mockAuth);
		when(accountRepository.findByUsername("nonexistent")).thenReturn(null);

		// Act & Assert
		UsernameNotFoundException ex = assertThrows(UsernameNotFoundException.class,
				() -> authService.getAccessToken(request));

		assertEquals("User not found", ex.getMessage());
	}
}

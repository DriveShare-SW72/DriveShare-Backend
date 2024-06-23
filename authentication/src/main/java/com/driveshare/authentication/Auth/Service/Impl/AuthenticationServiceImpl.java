package com.driveshare.authentication.Auth.Service.Impl;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.driveshare.authentication.Auth.DTO.AuthenticationResponseDTO;
import com.driveshare.authentication.Auth.Service.AuthenticationService;
import com.driveshare.authentication.Security.Service.JwtService;
import com.driveshare.authentication.Token.Token;
import com.driveshare.authentication.Token.TokenRepository;
import com.driveshare.authentication.Token.TokenType;
import com.driveshare.authentication.User.DTO.UserLoginDTO;
import com.driveshare.authentication.User.DTO.UserRegistrationDTO;
import com.driveshare.authentication.User.Entity.User;
import com.driveshare.authentication.User.Repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponseDTO register(UserRegistrationDTO userRegistrationDTO) {
        String normalizedEmail = userRegistrationDTO.getEmail().toLowerCase();
        var user = User.builder()
                .email(userRegistrationDTO.getEmail())
                .normalizedEmail(normalizedEmail)
                .email(userRegistrationDTO.getEmail())
                .name(userRegistrationDTO.getName())
                .lastName(userRegistrationDTO.getLastName())
                .password(passwordEncoder.encode(userRegistrationDTO.getPassword()))
                .role(userRegistrationDTO.getRole())
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponseDTO.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    @Transactional
    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findByUserIdAndExpiredFalseAndRevokedFalse(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);

    }

    @Override
    public AuthenticationResponseDTO authenticate(UserLoginDTO userLoginDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginDTO.getEamil().toLowerCase(),
                        userLoginDTO.getPassword()));
        var user = userRepository.findByNormalizedEmail(userLoginDTO.getEamil().toLowerCase())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponseDTO.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);
        if (username != null) {
            var user = this.userRepository.findByNormalizedEmail(username)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponseDTO.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

}

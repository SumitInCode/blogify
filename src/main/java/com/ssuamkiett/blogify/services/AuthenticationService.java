package com.ssuamkiett.blogify.services;

import com.ssuamkiett.blogify.dto.AuthenticationRequest;
import com.ssuamkiett.blogify.dto.AuthenticationResponse;
import com.ssuamkiett.blogify.dto.RegistrationRequest;
import com.ssuamkiett.blogify.exception.OperationNotPermittedException;
import com.ssuamkiett.blogify.models.Role;
import com.ssuamkiett.blogify.models.User;
import com.ssuamkiett.blogify.repositories.RoleRepository;
import com.ssuamkiett.blogify.repositories.UserRepository;
import com.ssuamkiett.blogify.security.JwtService;
import com.ssuamkiett.blogify.token.RefreshToken;
import com.ssuamkiett.blogify.token.RefreshTokenRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private static final int REFRESH_TOKEN_VALIDITY_DAYS = 10;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;

    public void register(RegistrationRequest request) {
        var userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("Role USER not initialized"));
        boolean isUserExists = userRepository.existsByEmail(request.getEmail());
        if (isUserExists) {
            throw new OperationNotPermittedException("User already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new OperationNotPermittedException("User already exists");
        }
        User user = createUser(request, userRole);
        userRepository.save(user);
    }

    private User createUser(RegistrationRequest request, Role userRole) {
        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(true)
                .roles(List.of(userRole))
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = (User) authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        ).getPrincipal();

        String refreshToken = generateAndSaveRefreshToken(user);
        String accessToken = jwtService.generateAccessToken(createClaims(user), user);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private String generateAndSaveRefreshToken(User user) {
        String refreshToken = jwtService.generateRefreshToken(user);
        RefreshToken refreshTokenEntity = refreshTokenRepository.findByEmail(user.getEmail())
                .orElseGet(() -> RefreshToken.builder()
                        .email(user.getEmail())
                        .build()
                );

        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setCreatedAt(LocalDateTime.now());
        refreshTokenEntity.setExpiresAt(LocalDateTime.now().plusDays(REFRESH_TOKEN_VALIDITY_DAYS));
        refreshTokenRepository.save(refreshTokenEntity);

        return refreshToken;
    }

    private HashMap<String, Object> createClaims(User user) {
        var claims = new HashMap<String, Object>();
        claims.put("username", user.getUsername());
        return claims;
    }

    public AuthenticationResponse refreshToken(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new OperationNotPermittedException("Refresh token required");
        }

        String refreshToken = authHeader.substring(7);
        String userEmail;
        try {
            userEmail = jwtService.extractUsername(refreshToken);
            if (userEmail == null || !isValidToken(refreshToken, userEmail)) {
                throw new OperationNotPermittedException("Invalid refresh token");
            }
        } catch (JwtException e) {
            throw new OperationNotPermittedException("Error while validating token");
        }

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new OperationNotPermittedException("User not found"));

        String accessToken = jwtService.generateAccessToken(createClaims(user), user);

        return AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private boolean isValidToken(String refreshToken, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new OperationNotPermittedException("User not found"));

        return jwtService.isValidToken(refreshToken, user);
    }
}

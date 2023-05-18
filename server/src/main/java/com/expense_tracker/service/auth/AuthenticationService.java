package com.expense_tracker.service.auth;

import com.expense_tracker.config.security.UserDetailsImpl;
import com.expense_tracker.config.security.jwt.JwtUtils;
import com.expense_tracker.model.User;
import com.expense_tracker.model.db.UserEntity;
import com.expense_tracker.model.util.UserConverter;
import com.expense_tracker.repository.UserRepository;
import com.expense_tracker.service.auth.requests.AuthenticationRequest;
import com.expense_tracker.service.auth.requests.RegisterRequest;
import com.expense_tracker.service.auth.response.AuthenticationResponse;
import com.expense_tracker.service.auth.response.RegisterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtService;
    private final AuthenticationManager authenticationManager;

    private final HashMap<String, UserEntity> pendingUsers = new HashMap<>();

    public RegisterResponse register(RegisterRequest request) {
        var user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstname());
        user.setLastName(request.getLastname());
        user.setRole(request.getRole());

        var jwtToken = jwtService.generateActivationCode(UserDetailsImpl.build(user));

        pendingUsers.put(jwtToken, user);

        return RegisterResponse.builder()
                .activationCode(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var user = repository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(UserDetailsImpl.build(user));
        var refreshToken = jwtService.generateRefreshToken(UserDetailsImpl.build(user));

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public User activate(String activationCode) {
        UserEntity user = pendingUsers.get(activationCode);
        user.setCreatedAt(LocalDate.now());
        user.setUpdatedAt(LocalDate.now());
        repository.save(user);
        pendingUsers.remove(activationCode);
        return UserConverter.convertToUser(user);
    }
}
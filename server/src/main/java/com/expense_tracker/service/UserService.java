package com.expense_tracker.service;

import com.expense_tracker.exceptions.JpaEntityNotFoundException;
import com.expense_tracker.model.User;
import com.expense_tracker.model.util.UserConverter;
import com.expense_tracker.repository.UserRepository;
import com.expense_tracker.exceptions.DuplicateResourceException;
import com.expense_tracker.model.db.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<User> getAll(Pageable pageable) {
        return StreamSupport
                .stream(userRepository.findAll(pageable).spliterator(), false)
                .map(UserConverter::convertToUser)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public User getById(Integer id) {
        return userRepository.findById(id)
                .map(UserConverter::convertToUser)
                .orElseThrow(() -> new JpaEntityNotFoundException("User with id: " + id + " not found"));
    }

    @Transactional(readOnly = true)
    public User getByUsername(String username) {
        final UserEntity user =  userRepository.findByUsername(username);
        if (user == null) {
            throw new JpaEntityNotFoundException("User with username: " + username + " not found");
        }

        return UserConverter.convertToUser(user);
    }

    @Transactional
    public User create(UserEntity userEntity) {
        if (userEntity.getId() != null) {
            throw new IllegalArgumentException("User id must be null");
        }

        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setCreatedAt(LocalDate.now());
        userEntity.setUpdatedAt(LocalDate.now());
        try {
            return UserConverter.convertToUser(userRepository.save(userEntity));
        } catch (DataIntegrityViolationException e) {
            log.error("Error while creating user: User with username: {} / email: {} already exists!", userEntity.getUsername(), userEntity.getEmail(), e);
            throw new DuplicateResourceException("User with username: " + userEntity.getUsername() + " / email: " + userEntity.getEmail() + " already exists!");
        }
    }

    @Transactional
    public User update(UserEntity userEntity) {
        if (userEntity.getId() == null) {
            throw new IllegalArgumentException("User id must not be null");
        }

        if (userEntity.getPassword() != null) {
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        }

        try {
            return UserConverter.convertToUser(userRepository.save(userEntity));
        } catch (DataIntegrityViolationException e) {
            log.error("Error while creating user: User with username: {} / email: {} already exists!", userEntity.getUsername(), userEntity.getEmail(), e);
            throw new DuplicateResourceException("User with username: " + userEntity.getUsername() + " / email: " + userEntity.getEmail() + " already exists!");
        }
    }

    @Transactional
    public void delete(Integer id) {
        userRepository.deleteById(id);
    }
}

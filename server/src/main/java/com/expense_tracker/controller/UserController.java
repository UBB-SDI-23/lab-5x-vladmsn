package com.expense_tracker.controller;

import com.expense_tracker.model.User;
import com.expense_tracker.model.db.UserEntity;
import com.expense_tracker.model.user.UserExpenses;
import com.expense_tracker.model.user.UserGroupDetails;
import com.expense_tracker.service.ExpenseTrackerService;
import com.expense_tracker.service.UserService;

import com.expense_tracker.validators.UserValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ExpenseTrackerService expenseTrackerService;

    @Operation(summary = "Get all users")
    @ApiResponse(responseCode = "200", description = "Users found")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Iterable<User>> getAll(@PageableDefault() Pageable pageable) {
        return new ResponseEntity<>(userService.getAll(pageable), HttpStatus.OK);
    }

    @Operation(summary = "Get all users with group details, by group id, sorted by balance")
    @ApiResponse(responseCode = "200", description = "Users found")
    @RequestMapping(value = "/group_details/{id}", method = RequestMethod.GET)
    public ResponseEntity<Iterable<UserGroupDetails>> getAllByGroupId(@PathVariable Integer id) {
        return new ResponseEntity<>(expenseTrackerService.getUserGroupDetails(id), HttpStatus.OK);
    }

    @Operation(summary = "Get user by id with detailed expenses by user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @RequestMapping(value = "/details/{user_id}", method = RequestMethod.GET)
    public ResponseEntity<UserExpenses> getById(@PathVariable Integer user_id, @RequestParam(required = false) Integer groupId) {
        return new ResponseEntity<>(expenseTrackerService.getUserExpensesByGroup(user_id, groupId), HttpStatus.OK);
    }

    @Operation(summary = "Create user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Invalid user"),
            @ApiResponse(responseCode = "409", description = "Username/email already in use")
    })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<User> create(@RequestBody UserEntity user) {
        UserValidator.validateUser(user);
        return new ResponseEntity<>(userService.create(user), HttpStatus.CREATED);
    }

    @Operation(summary = "Update user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "400", description = "Invalid user"),
            @ApiResponse(responseCode = "409", description = "Username/email already in use")
    })
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<User> update(@RequestBody UserEntity user) {
        UserValidator.validateUser(user);
        return new ResponseEntity<>(userService.update(user), HttpStatus.OK);
    }

    @Operation(summary = "Delete user")
    @ApiResponse(responseCode = "204", description = "User deleted")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

package com.expense_tracker.controller;


import com.expense_tracker.model.User;
import com.expense_tracker.service.auth.*;
import com.expense_tracker.service.auth.requests.AuthenticationRequest;
import com.expense_tracker.service.auth.requests.RegisterRequest;
import com.expense_tracker.service.auth.response.AuthenticationResponse;
import com.expense_tracker.service.auth.response.RegisterResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @Operation(summary = "Register user")
    @RequestMapping( value = "/register",method = RequestMethod.POST )
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest register) {

        return new ResponseEntity<>(authenticationService.register(register), HttpStatus.OK);
    }

    @Operation(summary = "Login user")
    @RequestMapping( value = "/login", method = RequestMethod.POST )
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {

        return new ResponseEntity<>(authenticationService.authenticate(authenticationRequest), HttpStatus.OK);
    }

    @Operation(summary = "Confirm registration")
    @RequestMapping( value = "/confirm/{activation_code}", method = RequestMethod.POST )
    public ResponseEntity<User> confirmRegistration(@PathVariable String activation_code) {

        return new ResponseEntity<>(authenticationService.activate(activation_code), HttpStatus.OK);
    }
}

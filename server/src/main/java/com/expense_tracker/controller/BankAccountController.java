package com.expense_tracker.controller;

import com.expense_tracker.model.BankAccount;
import com.expense_tracker.model.db.BankAccountEntity;
import com.expense_tracker.service.BankAccountService;
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
@RequestMapping(path = "api/v1/bank_accounts")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @Operation(summary = "Get all bank accounts")
    @ApiResponse(responseCode = "200", description = "Bank account found")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Iterable<BankAccount>> getAll(@PageableDefault Pageable pageable) {
        return new ResponseEntity<>(bankAccountService.getAll(pageable), HttpStatus.OK);
    }

    @Operation(summary = "Get bank account by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bank account  found"),
            @ApiResponse(responseCode = "404", description = "Bank account  not found")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<BankAccountEntity> getById(@PathVariable Integer id) {
        return new ResponseEntity<>(bankAccountService.getById(id), HttpStatus.OK);
    }

    @Operation(summary = "Get bank account by user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bank account found"),
            @ApiResponse(responseCode = "404", description = "Bank account not found")
    })
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public ResponseEntity<Iterable<BankAccountEntity>> getByUserId(@PathVariable Integer userId) {
        return new ResponseEntity<>(bankAccountService.getByUserId(userId), HttpStatus.OK);
    }

    @Operation(summary = "Create bank account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bank account created"),
            @ApiResponse(responseCode = "400", description = "Invalid bank account")
    })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<BankAccountEntity> create(@RequestBody BankAccountEntity bankAccountEntity) {
        return new ResponseEntity<>(bankAccountService.create(bankAccountEntity), HttpStatus.CREATED);
    }

    @Operation(summary = "Update bank account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bank account created"),
            @ApiResponse(responseCode = "400", description = "Invalid bank account")
    })
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<BankAccountEntity> update(@RequestBody BankAccountEntity bankAccountEntity) {
        return new ResponseEntity<>(bankAccountService.update(bankAccountEntity), HttpStatus.OK);
    }

    @Operation(summary = "Delete bank account")
    @ApiResponse(responseCode = "204", description = "Bank account deleted")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        bankAccountService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

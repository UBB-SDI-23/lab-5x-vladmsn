package com.expense_tracker.controller;

import com.expense_tracker.model.Expense;
import com.expense_tracker.model.db.ExpenseEntity;
import com.expense_tracker.model.expense.ExpenseDetails;
import com.expense_tracker.service.ExpenseService;

import com.expense_tracker.service.ExpenseTrackerService;
import com.expense_tracker.validators.ExpenseValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


@RestController
@RequestMapping(path = "api/v1/expenses")
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseTrackerService expenseTrackerService;
    private final ExpenseService expenseService;

    @Operation(summary = "Get all expenses")
    @ApiResponse(responseCode = "200", description = "expenses found")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Iterable<Expense>> getAll() {
        return new ResponseEntity<>(expenseService.getAll(), HttpStatus.OK);
    }

    @Operation(summary = "Get detailed expense by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "expense found"),
        @ApiResponse(responseCode = "404", description = "expense not found")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ExpenseDetails> getById(@PathVariable Integer id) {
        return new ResponseEntity<>(expenseTrackerService.getExpenseDetails(id), HttpStatus.OK);
    }

    @Operation(summary = "Get expenses by group id, and optionally filter by date")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "expenses found"),
        @ApiResponse(responseCode = "404", description = "expenses not found")
    })
    @RequestMapping(value = "/group/{id}", method = RequestMethod.GET)
    public ResponseEntity<Iterable<Expense>> getByGroupId(@PathVariable Integer id, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateAfter) {
        return new ResponseEntity<>(expenseService.getByGroupId(id, dateAfter), HttpStatus.OK);
    }

    @Operation(summary = "Get expenses by payer id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "expenses found"),
        @ApiResponse(responseCode = "404", description = "expenses not found")
    })
    @RequestMapping(value = "/payer/{id}", method = RequestMethod.GET)
    public ResponseEntity<Iterable<Expense>> getByPayerId(@PathVariable Integer id) {
        return new ResponseEntity<>(expenseService.getByPayerId(id), HttpStatus.OK);
    }

    @Operation(summary = "Create expense")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "expense created"),
            @ApiResponse(responseCode = "400", description = "Invalid expense")
    })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Expense> create(@RequestBody ExpenseEntity expenseEntity) {
        ExpenseValidator.validateExpense(expenseEntity);
        return new ResponseEntity<>(expenseService.create(expenseEntity), HttpStatus.CREATED);
    }

    @Operation(summary = "Add expense participant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "expense participant added"),
            @ApiResponse(responseCode = "400", description = "Invalid expense")
    })
    @RequestMapping(value = "/{id}/participant", method = RequestMethod.POST)
    public ResponseEntity<ExpenseDetails> addParticipant(@PathVariable Integer id, @RequestParam Integer participantId, @RequestParam Double amount) {
        return new ResponseEntity<>(expenseTrackerService.addParticipantToExpense(id, participantId, amount), HttpStatus.OK);
    }

    @Operation(summary = "Update expense")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "expense updated"),
            @ApiResponse(responseCode = "400", description = "Invalid expense")
    })
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<Expense> update(@RequestBody ExpenseEntity expenseEntity) {
        ExpenseValidator.validateExpense(expenseEntity);
        return new ResponseEntity<>(expenseService.update(expenseEntity), HttpStatus.OK);
    }

    @Operation(summary = "Delete expense by id")
    @ApiResponse(responseCode = "204", description = "expense deleted")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        expenseService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

package com.expense_tracker.controller;

import com.expense_tracker.model.Group;
import com.expense_tracker.model.db.GroupEntity;
import com.expense_tracker.model.group.GroupExpenseSummary;
import com.expense_tracker.model.group.GroupWithBalance;
import com.expense_tracker.service.ExpenseTrackerService;
import com.expense_tracker.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;
    private final ExpenseTrackerService expenseTrackerService;

    @Operation(summary = "Get all groups")
    @ApiResponse(responseCode = "200", description = "Groups found")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Iterable<Group>> getAll(@PageableDefault Pageable pageable) {
        return new ResponseEntity<>(groupService.getAllGroups(pageable), HttpStatus.OK);
    }

    @Operation(summary = "Get group by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Group found"),
            @ApiResponse(responseCode = "404", description = "Group not found")
    })
    @RequestMapping(value = "/{group_id}", method = RequestMethod.GET)
    public ResponseEntity<Group> getById(@PathVariable Integer group_id) {
        return new ResponseEntity<>(groupService.getGroupById(group_id), HttpStatus.OK);
    }

    @Operation(summary = "Get total spent by group by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Group found"),
            @ApiResponse(responseCode = "404", description = "Group not found")
    })
    @RequestMapping(value = "/{group_id}/total", method = RequestMethod.GET)
    public ResponseEntity<GroupExpenseSummary> getTotalById(@PathVariable Integer group_id) {
        return new ResponseEntity<>(expenseTrackerService.getTotalExpenseSummary(group_id), HttpStatus.OK);
    }

    @Operation(summary = "Get all groups by user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Groups found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @RequestMapping(value = "/user/{user_id}", method = RequestMethod.GET)
    public ResponseEntity<Page<GroupWithBalance>> getAllByUserId(@PathVariable Integer user_id, @PageableDefault Pageable pageable) {
        return new ResponseEntity<>(groupService.getGroupsByUserId(user_id, pageable), HttpStatus.OK);
    }

    @Operation(summary = "Create group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Group created"),
            @ApiResponse(responseCode = "400", description = "Invalid group"),
            @ApiResponse(responseCode = "409", description = "Group already exists")
    })
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<GroupEntity> create(@RequestBody GroupEntity group) {
        return new ResponseEntity<>(groupService.createGroup(group), HttpStatus.CREATED);
    }

    @Operation(summary = "Add participant to group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Participant created"),
            @ApiResponse(responseCode = "400", description = "Invalid participant"),
    })
    @RequestMapping(value = "/participant", method = RequestMethod.POST)
    public ResponseEntity<Void> addParticipant(@RequestParam(value = "groupId") Integer groupId, @RequestParam(value = "userId") Integer userId) {
        groupService.addParticipant(groupId, userId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Remove participant from group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Participant deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid participant"),
    })
    @RequestMapping(value = "/participant", method = RequestMethod.DELETE)
    public ResponseEntity<Void> removeParticipant(@RequestParam(value = "groupId") Integer groupId, @RequestParam(value = "userId") Integer userId) {
        groupService.removeParticipant(groupId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @Operation(summary = "Update group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Group updated"),
            @ApiResponse(responseCode = "400", description = "Invalid group"),
            @ApiResponse(responseCode = "404", description = "Group not found")
    })
    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<GroupEntity> update(@RequestBody GroupEntity group) {
        return new ResponseEntity<>(groupService.updateGroup(group), HttpStatus.OK);
    }

    @Operation(summary = "Delete group")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Group deleted"),
            @ApiResponse(responseCode = "404", description = "Group not found")
    })
    @RequestMapping(value = "/{group_id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@RequestBody Integer groupId) {
        groupService.deleteGroup(groupId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

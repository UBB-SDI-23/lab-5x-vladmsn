package com.expense_tracker.model;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class Group {
    private Integer id;
    private String name;
    private String description;
    private String currency;
    private Set<User> participants;
}

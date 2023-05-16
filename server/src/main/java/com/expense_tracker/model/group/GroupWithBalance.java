package com.expense_tracker.model.group;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class GroupWithBalance {
    private Integer id;
    private String name;
    private String description;
    private String currency;
    private LocalDate joinedAt;
    private Double balance;
}

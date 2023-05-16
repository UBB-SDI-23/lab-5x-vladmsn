package com.expense_tracker.model.db;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "group_user")
public class GroupUserEntity {

    @EmbeddedId
    private GroupUsersPk pk;
    @Column(name = "nickname")
    private String nickname;
    @Column(name = "is_active")
    private Boolean isActive;
    @Column(name = "joined_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDate joinedAt;

    @Embeddable
    @Data
    public static class GroupUsersPk implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
        @Column(name = "user_id")
        private Integer userId;
        @Column(name = "group_id")
        private Integer groupId;
    }
}

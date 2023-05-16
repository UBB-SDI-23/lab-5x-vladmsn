-- create user_expense table

CREATE TABLE IF NOT EXISTS `expense_participant` (
    `user_id` INTEGER NOT NULL,
    `expense_id` INTEGER NOT NULL,
    `amount_owed` DOUBLE NOT NULL,
    PRIMARY KEY (user_id, expense_id),
    FOREIGN KEY (user_id) REFERENCES `users`(id) ON DELETE CASCADE,
    FOREIGN KEY (expense_id) REFERENCES `expenses`(id) ON DELETE CASCADE
);

-- create the user_group table

CREATE TABLE IF NOT EXISTS `group_user` (
    `user_id` INTEGER NOT NULL,
    `group_id` INTEGER NOT NULL,
    `nickname` VARCHAR(256) NOT NULL,
    `is_active` BOOLEAN NOT NULL DEFAULT TRUE,
    `joined_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id, group_id),
    FOREIGN KEY (user_id) REFERENCES `users`(id) ON DELETE CASCADE,
    FOREIGN KEY (group_id) REFERENCES `lists`(id) ON DELETE CASCADE
);

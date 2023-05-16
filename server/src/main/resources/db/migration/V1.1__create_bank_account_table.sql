-- create bank account table

CREATE TABLE IF NOT EXISTS `bank_accounts` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(256) NOT NULL,
    `card_number` VARCHAR(256) NOT NULL,
    `currency` VARCHAR(3) NOT NULL,
    `user_id` INTEGER NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);
-- create expense table

CREATE TABLE IF NOT EXISTS `expenses` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `amount` DOUBLE NOT NULL,
    `currency` VARCHAR(3) NOT NULL,
    `category` VARCHAR(256),
    `description` VARCHAR(256) NOT NULL,
    `payer_id` INTEGER NOT NULL,
    `group_id` INTEGER NOT NULL,
    `transaction_date` DATE NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (payer_id) REFERENCES `users`(id),
    FOREIGN KEY (group_id) REFERENCES `lists`(id)
)
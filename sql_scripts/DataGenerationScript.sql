DELIMITER $$
CREATE PROCEDURE reset_tables()
BEGIN
  SET FOREIGN_KEY_CHECKS = 0;

  TRUNCATE TABLE users;
  TRUNCATE TABLE lists;
  TRUNCATE TABLE group_user;
  TRUNCATE TABLE expenses;
  TRUNCATE TABLE expense_participant;
  
  SET FOREIGN_KEY_CHECKS = 1;
END$$
DELIMITER ;

-- Call the procedure to reset all tables
CALL reset_tables();


-- Create Users
DELIMITER $$
CREATE PROCEDURE generate_users(IN user_count INT, IN start_value INT)
BEGIN
  DECLARE i INT DEFAULT start_value;
  WHILE i < start_value + user_count DO
    INSERT INTO users (username, password, email, first_name, last_name, preferred_currency) 
    VALUES (CONCAT('Username-', i), '$2a$10$KfN0k2T2FbYuGtqpbcprTuKpxqaVMHYJ.sJ51Gg732OdlTE6pZs9C', CONCAT('email-', i, '@example.com'), CONCAT('FirstName-', i), CONCAT('LastName-', i), 'USD');
    SET i = i + 1;
  END WHILE;
END$$
DELIMITER ;

-- Create Lists
DELIMITER $$
CREATE PROCEDURE generate_lists(IN list_count INT, IN start_value INT)
BEGIN
  DECLARE i INT DEFAULT start_value;
  WHILE i < start_value + list_count DO
    INSERT INTO lists (list_name, description, preferred_currency) 
    VALUES (CONCAT('ListName-', i), CONCAT('Description-', i), 'USD');
    SET i = i + 1;
  END WHILE;
END$$
DELIMITER ;


-- Create Group_User
DELIMITER $$
CREATE PROCEDURE generate_group_user(IN group_user_count INT, IN start_value INT)
BEGIN
  DECLARE i INT DEFAULT start_value;
  WHILE i < start_value + group_user_count DO
    INSERT INTO group_user (user_id, group_id, nickname, is_active) 
    VALUES (i+1, i+1, CONCAT('Nickname-', i), 1);
    SET i = i + 1;
  END WHILE;
END$$
DELIMITER ;


-- Create Expenses
DELIMITER $$
CREATE PROCEDURE generate_expenses(IN expense_count INT, IN start_value INT)
BEGIN
  DECLARE i INT DEFAULT start_value;
  WHILE i < start_value + expense_count DO
    INSERT INTO expenses (amount, currency, description, payer_id, group_id, transaction_date) 
    VALUES (FLOOR(100 + RAND() * 100), 'USD', CONCAT('Description-', i), i+1, i+1, CURDATE());
    SET i = i + 1;
  END WHILE;
END$$
DELIMITER ;


-- Create Expense_Participant
DELIMITER $$
CREATE PROCEDURE generate_expense_participant(IN expense_count INT, IN start_value INT)
BEGIN
  DECLARE i INT DEFAULT start_value;
  DECLARE group_id INT;
  DECLARE participant_count INT;
  WHILE i < start_value + expense_count DO
    SELECT group_id INTO group_id FROM expenses WHERE id = i+1;
    SET participant_count = FLOOR(10 + RAND() * 5);
    WHILE participant_count > 0 DO
      INSERT INTO expense_participant (user_id, expense_id, amount_owed) 
      SELECT user_id, i+1, FLOOR(5 + RAND() * 10) 
      FROM group_user
      WHERE group_id = group_id
      ORDER BY RAND()
      LIMIT 1;
      SET participant_count = participant_count - 1;
    END WHILE;
    SET i = i + 1;
  END WHILE;
END$$
DELIMITER ;


CALL generate_users(10000, 0);
CALL generate_lists(10000, 0);
CALL generate_group_user(10000, 0);
CALL generate_expenses(10000, 0);
CALL generate_expense_participant(1000, 0);


CALL generate_lists(10000, 10000);
CALL generate_group_user(10000, 10000);
CALL generate_expenses(10000, 10000);
CALL generate_expense_participant(1000, 1000);


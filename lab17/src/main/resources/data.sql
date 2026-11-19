-- No ids: AUTO_INCREMENT numbers them 1, 2, 3, ... so new courses continue from there
INSERT INTO course(code, name, description, credits) VALUES ('CS101', 'Introduction to Programming', 'Basics of programming in Java', 3);
INSERT INTO course(code, name, description, credits) VALUES ('CS210', 'Data Structures', 'Lists, stacks, queues, trees and graphs', 3);
INSERT INTO course(code, name, description, credits) VALUES ('CS285', 'Discrete Mathematics', 'Logic, sets, relations and counting', 3);
INSERT INTO course(code, name, description, credits) VALUES ('SE311', 'Software Requirements', 'Eliciting and specifying requirements', 3);
INSERT INTO course(code, name, description, credits) VALUES ('SE411', 'Software Construction', 'Advanced OO, design patterns, concurrency and Spring', 3);
INSERT INTO course(code, name, description, credits) VALUES ('CS340', 'Database Systems', 'Relational model, SQL and normalization', 3);
INSERT INTO course(code, name, description, credits) VALUES ('ENG101', 'English Composition', 'Academic writing', 2);

-- Step 5: one user, usera / pwd12345. The column holds the BCrypt hash
-- (cost 12), never the plain password. INSERT IGNORE skips the row if usera
-- already exists, so restarts do not fail on the UNIQUE constraint.
INSERT IGNORE INTO users(username, password)
VALUES ('usera', '$2a$12$mdo0uJkXOLq3/CYjgbd2men1lfGwXXqDZvbnUzq5Fznwg0hbz.2IC');

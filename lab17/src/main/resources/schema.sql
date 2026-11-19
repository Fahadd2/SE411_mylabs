-- MySQL keeps its data between runs (unlike H2 in memory), so the table is
-- dropped and recreated at every start to begin from the same data.
DROP TABLE IF EXISTS course;

CREATE TABLE course
(
    id          BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    code        VARCHAR(20)  NOT NULL,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    credits     INT          NOT NULL
);

-- Users for Spring Security. IF NOT EXISTS keeps registered users across
-- restarts (the course table above is still dropped and recreated).
CREATE TABLE IF NOT EXISTS users
(
    id       INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL -- BCrypt hash, never plain text
);

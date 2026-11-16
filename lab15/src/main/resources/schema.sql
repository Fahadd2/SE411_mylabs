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

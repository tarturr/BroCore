CREATE TABLE IF NOT EXISTS bros (
    uuid VARCHAR(24) PRIMARY KEY,
    pseudo VARCHAR(32) UNIQUE,
    rank VARCHAR(255),
    exp INT(11),
    balance INT(11)
);
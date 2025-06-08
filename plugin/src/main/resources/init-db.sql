CREATE TABLE IF NOT EXISTS bros (
    uuid VARCHAR(24) PRIMARY KEY,
    pseudo VARCHAR(32) UNIQUE,
    exp FLOAT,
    balance FLOAT
);
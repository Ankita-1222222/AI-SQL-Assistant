create database AI_SQL_Assistant;
use AI_SQL_Assistant;

CREATE TABLE users (
    userid VARCHAR(50) PRIMARY KEY,
    username VARCHAR(100),
    password VARCHAR(100)
);

CREATE TABLE userdetails (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    age INT,
    gender VARCHAR(10),
    city VARCHAR(50),
    dob DATE,
    CreatedBy VARCHAR(50),
    CreatedOn TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (CreatedBy) REFERENCES users(userid)
);
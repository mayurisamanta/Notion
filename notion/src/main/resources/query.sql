CREATE DATABASE notion;

USE notion;

CREATE TABLE user
(
    userid      INT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(255) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    emailid     VARCHAR(255) NOT NULL UNIQUE,
    xstatus     TINYINT      NOT NULL COMMENT '0: inactive, 1: active',
    lastloginat DATETIME,
    createdat   DATETIME     NOT NULL,
    updatedat   DATETIME
);
CREATE TABLE IF NOT EXISTS sys_user (
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255),
    nickname VARCHAR(255),
    password VARCHAR(255),
    role_id  BIGINT
);

CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    description VARCHAR(255)
);

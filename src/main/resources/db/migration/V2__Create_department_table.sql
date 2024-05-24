-- Создание таблицы department
CREATE TABLE user (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    description VARCHAR(255),
    name VARCHAR(255) NOT NULL
);
-- V1__Create_tables.sql

-- Создание таблицы department
CREATE TABLE department (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    description VARCHAR(255),
    name VARCHAR(255) NOT NULL
);

-- Создание таблицы employee
CREATE TABLE employee (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    department VARCHAR(255),
    name VARCHAR(255) NOT NULL
);

-- Создание таблицы manager
CREATE TABLE manager (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    department VARCHAR(255),
    name VARCHAR(255) NOT NULL
);

-- Создание таблицы testentity
CREATE TABLE testentity (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(255) NOT NULL
);

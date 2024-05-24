#!/bin/bash

# Переменная с путем к директории сервисов
SERVICE_DIR="src/main/java/com/ems/service"

com.example.ems.controller
# Создание директории, если она не существует
mkdir -p $SERVICE_DIR

# Создание пустых файлов для классов сервисов
touch $SERVICE_DIR/DepartmentService.java
touch $SERVICE_DIR/ManagerService.java
touch $SERVICE_DIR/EmployeeService.java

echo "Скелеты классов сервисов созданы в $SERVICE_DIR"

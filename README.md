# Employee Management System

## Employee Management System - учебный проект для управления сотрудниками, департаментами и менеджерами, включающий функционал авторизации, генерации отчетов и мониторинга статусов сотрудников.

> ### "Kaizen means improvement. Moreover, it means continuing improvement in personal life, home life, social life, and working life." - Imai Masaaki
> _Постоянное улучшение – ключ к качеству. Быстрое действие важно, но постоянство – основа успеха._

## Оглавление

1. [Цель проекта](#цели-проекта)
2. [Краткое Техническое задание. Требования к приложению.](#краткое-техническое-задание-требования-к-приложению)
   - [Бизнес-потребность](#бизнес-потребность)
   - [Основной функционал](#основной-функционал)
   - [Дополнительные артефакты](#дополнительные-артефакты)
3. [Architectural Decision Record (ADR)](#adr-architectural-decision-record) 
4. [Реализация (development approach)](#реализация-development-approach)
   - [Уровень 1. Подготовка точки входа и скелета приложения](#уровень-1-подготовка-точки-входа-и-скелета-приложения)
   - [Уровень 2. Использование QueryDSL. Flyway](#уровень-2-использование-querydsl-flyway)
   - [Уровень 3. Использование Spring Security](#уровень-3-использование-spring-security)

## Цели проекта

**Цель проекта** - применить и познакомиться на практике со следующими технологиями языка Java:

| Технология        | Описание                                                                                   |
|-------------------|--------------------------------------------------------------------------------------------|
| Spring Boot       | Фреймворк для создания автономных, готовых к производству приложений на Java с минимальной настройкой. |
| Spring Security   | Фреймворк для аутентификации и управления доступом, легко расширяемый для удовлетворения специфических требований. |
| Spring Data JPA   | Часть Spring Data, упрощающая реализацию репозиториев на основе JPA (Java Persistence API). |
| QueryDSL          | Фреймворк для построения типобезопасных SQL-запросов на основе Java.                               |
| Flyway            | Инструмент для управления версионностью схемы базы данных с использованием миграций.               |
| Swagger           | Инструмент для документирования и тестирования REST API.                                           |
| Lombok            | Библиотека для автоматизации создания шаблонного кода, такого как геттеры, сеттеры и конструкторы.     |
| Maven             | Инструмент для управления проектами и зависимостями, упрощающий сборку и развертывание приложений.   |
| Log4j2            | Стандартный инструмент для логирования, обеспечивающий высокую производительность и гибкость конфигурации. |
| javax.validation  | Стандартный фреймворк для валидации данных в приложениях на Java. |
| filters  | Поиск с помощью фильтров. |

## Краткое Техническое задание. Требования к приложению.

В разделе описаны основные требования для обеспечения контекста разработки приложения. Контекст необходим для более осознанного подхода к разработке приложения и отработки трассировки требований с реализуемым функционалом.

### Бизнес-потребность

Компания **АтомПромАвтоматика** _(вымышленное название)_ хочет автоматизировать бизнес-процесс управления своими сотрудниками, департаментами и менеджерами. В рамках этого процесса необходимо выполнять следующие задачи:

1. **Управление сотрудниками**: Включает в себя найм (изменение статуса), назначение в департаменты, перевод между департаментами и увольнение сотрудников. Управление осуществляет Менеджер.
2. **Управление менеджерами**: Включает в себя найм (изменение статуса), назначение в департаменты, перевод между департаментами и увольнение сотрудников. Управление осуществляет Начальник кадровой службы (HR) (в приложении имитирует пользователь с ролью Админ).
3. **Управление департаментами**: Включает создание департаментов и другие CRUDLs операции. Управление осуществляет пользователь с правами роли Админ.
4. **Генерация отчетов**: Менеджеры должны иметь возможность генерировать отчеты по структуре департаментов и количеству сотрудников в них.

Для выполнения этих задач компания поставила задачу разработать **Employee Management System** (EMS), которая обеспечит:

- Автоматизацию процессов управления персоналом и департаментами.
- Возможность аутентификации и авторизации пользователей с различными уровнями доступа.
- Инструменты для фильтрации и поиска информации о сотрудниках и менеджерах.
- Инструменты по формированию Департаментов согласно штатному расписанию.
- Генерацию отчетов по запросу менеджеров.

Проведя онтологический анализ предметной области и целей бизнеса, выделены следующие сущности и их зависимости:

#### Сущности:
- Департамент
- Пользователь в роли:
- Менеджер
- Сотрудник
- Администратор

### Основной функционал

#### Авторизация и аутентификация пользователей:
1. Логин и логаут пользователей.
2. Получение информации о пользователях.

#### Управление сотрудниками менеджером:
1. CRUDLs операции.
2. Фильтрация сотрудников по различным критериям.
3. Поиск сотрудников по имени.
4. Назначение сотрудников в департамент.
5. Вывод имени и фамилии Руководителя для каждого Сотрудника.

#### Управление менеджерами ролью Администратор (Админ):
1. CRUDLs операции.
2. Фильтрация менеджеров по различным критериям.
3. Поиск менеджеров по фамилии.
4. Назначение менеджеров в департамент.

   Менеджеры управляют наймом сотрудников, их назначением в департаменты.

#### Управление департаментами ролью Администратор (Админ):
1. CRUDLs операции.
2. Фильтрация департаментов по различным критериям.
3. Назначение начальников департаментов (менеджеров). _Детали реализации: продумать где assignManagerToDepartament - в классе менеджеров или в классе департамента_
4. Назначение сотрудников в департаменты. _Детали реализации: продумать где assignEmployeeToDepartament - в классе менеджеров или в классе департамента_
5. Перевод сотрудника из одного департамента в другой. _Детали реализации: продумать где switchPeopleBeetwenDepartaments - в классе менеджеров, сотрудников или в классе департамента_

### ADR (Architectural Decision Record)

<details>
<summary> &#128736; ADR-001: Выбор архитектурного подхода для системы Employee Management System (EMS) </summary>

**Дата:** 2024-05-18

**Контекст:**

Компания АтомПромАвтоматика хочет автоматизировать бизнес-процесс управления своими сотрудниками, департаментами и менеджерами. Система должна включать следующие основные функции:
- Управление сотрудниками, менеджерами и департаментами.
- Аутентификация и авторизация пользователей.
- Генерация отчетов.
- Фильтрация и поиск информации.

**Выбор системного дизайна:**

В части системного дизайна рассматривались следующие варианты:
1. **Монолит:**
   - Все компоненты системы собраны в одном приложении.

2. **Модульный монолит:**
   - Компоненты разделены на модули внутри одного приложения, что облегчает их поддержку и развитие.

3. **Микросервисы на основе событийно-ориентированной архитектуры (EDA):**
   - Разделение системы на независимые сервисы, взаимодействующие через события и REST API.
   - Использование событий для связи между сервисами, что позволяет системе быть более асинхронной и гибкой.

Мы остановились на **монолите**, так как на этапе проверки продуктовой гипотезы главная задача – максимально сократить время выхода на рынок (это учебная легенда).

**Альтернативы внутренней архитектуры приложения:**

1. **Классическая многослойная (N-tier) архитектура:**
   - **Презентационный слой (Presentation Layer):**
     - Отвечает за интерфейсы пользователя и взаимодействие с пользователем.
     - Использует Spring MVC для создания REST-контроллеров и представлений.
   - **Слой бизнес-логики (Business Logic Layer):**
     - Содержит бизнес-правила и процессы.
     - Реализует основные бизнес-сервисы.
   - **Слой доступа к данным (Data Access Layer):**
     - Управляет доступом к данным и взаимодействием с базой данных.
     - Использует Spring Data JPA для ORM и QueryDSL для сложных запросов.
   - **Слой базы данных (Database Layer):**
     - Содержит физическую базу данных.
     - Flyway используется для миграции и версионирования схемы базы данных.

2. **Гексагональная архитектура (Hexagonal Architecture) и DDD:**
   - **Доменный слой (Domain Layer):**
     - Содержит бизнес-логику и правила.
     - Независим от инфраструктурных деталей.
   - **Адаптеры (Adapters):**
     - Входящие адаптеры (Inbound Adapters): REST-контроллеры, которые принимают запросы от клиентов.
     - Исходящие адаптеры (Outbound Adapters): Компоненты для взаимодействия с внешними системами, такими как базы данных и сервисы.
   - **Порты (Ports):**
     - Входные порты (Inbound Ports): Интерфейсы, которые определяют, как внешние агенты могут взаимодействовать с бизнес-логикой.
     - Исходные порты (Outbound Ports): Интерфейсы, которые бизнес-логика использует для взаимодействия с внешними системами.

**Решение:**

После анализа различных архитектурных подходов для внутренней архитектуры приложения было принято решение использовать **классическую многослойную (N-tier) архитектуру** для разработки системы Employee Management System.

**Причины выбора:**

1. **Простота и понятность:**
   - Многослойная архитектура хорошо известна и понятна большинству разработчиков.
   - Обеспечивает четкое разделение ответственности между слоями, что упрощает разработку и поддержку системы.

2. **Легкость в реализации и начальном освоении:**
   - Проектирование и реализация многослойной архитектуры требуют меньше времени и усилий на начальном этапе по сравнению с гексагональной архитектурой и DDD.
   - Подходит для учебного проекта, где важно быстрое освоение основных принципов и технологий.

3. **Тестируемость и поддерживаемость:**
   - Многослойная архитектура обеспечивает хорошую тестируемость благодаря четкому разделению логики на слои.
   - Поддержка такой архитектуры также проще, особенно для небольших команд или начинающих разработчиков.

4. **Соответствие текущим требованиям:**
   - Текущие требования проекта не требуют сложных бизнес-правил и высокой гибкости, которые могли бы потребовать применения DDD или гексагональной архитектуры.
   - Концентрация на освоении указанных технологий:
     - Spring Boot
     - Spring Security
     - Spring Data JPA
     - QueryDSL
     - Flyway
     - Swagger
     - Lombok
     - Maven
     - Log4j2
     - javax.validation

**Отклоненные альтернативы:**

1. **Гексагональная архитектура и DDD:**
   - Более сложное проектирование и внедрение, что требует большего времени и усилий.
   - Высокая гибкость и возможность легкой замены компонентов не являются критичными для текущих требований проекта.

2. **Модульный монолит и микросервисы на основе EDA:**
   - **Модульный монолит:** Хороший компромисс между монолитом и микросервисами, но требует навыков модульного проектирования.
   - **Микросервисы:** Высокая гибкость и масштабируемость, но значительно усложняет управление и требует большого опыта в оркестрации и управлении сервисами.
   - **EDA:** Увеличивает сложность за счет необходимости в управлении событиями и согласованностью данных.

**Последствия:**

- В будущем, по мере роста системы и увеличения сложности бизнес-логики, возможно, потребуется пересмотр архитектуры и переход на более сложные подходы, такие как гексагональная архитектура или DDD.
- Текущая архитектура позволит быстро начать разработку и получить рабочий прототип системы в короткие сроки, что важно для учебного проекта.

**Статус:**
Принято

**Автор:** Alex Po

---


</details>



### План график разработки (устарело. привести в соответствии с ADR)

![your-UML-diagram-name](http://www.plantuml.com/plantuml/proxy?cache=no&src=https://raw.githubusercontent.com/PortnovAlex80/EmployeeManagementSystem/main/diagrams/employee_management_system_plan.gantt.iuml)

###  Дополнительные артефакты
<details>
<summary> &#128736;  Дополнительные артефакты</summary>
<details>
<summary> &#128736; Ролевая модель </summary>
   
1. **Сотрудник**:
   - **Привилегии**: Просмотр собственного профиля и данных о зарплате, запрос отпуска.
   - **Атрибуты**: ID сотрудника, департамент, должность.

2. **Менеджер**:
   - **Привилегии**: Управление подчиненными, назначение задач, генерация отчетов, управление расписаниями.
   - **Атрибуты**: ID менеджера, уровень доступа, список подчиненных, департамент.

3. **Администратор**:
   - **Привилегии**: Полный доступ к системе, включая управление пользователями, настройку системных параметров, создание и изменение структуры департаментов.
   - **Атрибуты**: ID администратора, специальные административные права.
</details>

<details>
<summary> &#128736; Статусы сущностей </summary>
   
#### Пользователь:
- **Активен (Active)**
- **Заблокирован (Blocked)**
- **Удален (Deleted)**

#### Сотрудник:
- **Нанят (Hired)**
- **На испытательном сроке (Probation)**
- **Постоянный (Permanent)**
- **Уволен (Terminated)**

#### Менеджер:
- **Активен (Active)**
- **В отпуске (On Leave)**
- **Уволен (Terminated)**

#### Департамент:
- **Активен (Active)**
- **Закрыт (Closed)**

</details>

<details>
<summary> &#128736; Жизненный цикл сущностей. Данные для State Diagram </summary>
   
#### Пользователь:
1. **Активен (Active)** → **Заблокирован (Blocked)** → **Активен (Active)**
2. **Активен (Active)** → **Удален (Deleted)**

#### Сотрудник:
1. **Нанят (Hired)** → **На испытательном сроке (Probation)** → **Постоянный (Permanent)** → **Уволен (Terminated)**
2. **На испытательном сроке (Probation)** → **Уволен (Terminated)**
3. **Постоянный (Permanent)** → **Уволен (Terminated)**

#### Менеджер:
1. **Активен (Active)** → **В отпуске (On Leave)** → **Активен (Active)**
2. **Активен (Active)** → **Уволен (Terminated)**

#### Департамент:
1. **Активен (Active)** → **Закрыт (Closed)**

</details>

<details>
<summary> &#128736; Данные для Activity Diagram </summary>
   
#### Назначение сотрудника в департамент:
1. **Менеджер** выбирает департамент.
2. **Менеджер** выбирает сотрудника.
3. **Система** назначает сотрудника в выбранный департамент.
4. **Система** обновляет статус сотрудника.

#### Перевод сотрудника между департаментами:
1. **Менеджер** инициирует перевод сотрудника.
2. **Система** проверяет текущий департамент сотрудника.
3. **Система** обновляет департамент сотрудника.
4. **Система** уведомляет старый и новый департамент.
</details>

<details>
<summary> &#128736; Реестр Use Cases восстановленный из кода Workshop'a. </summary>

Изменять нельзя, так как получено реинжинирингом кода из репозитория учебного задания

| Актор                  | Наименование Use Case                  | Описание                                                                 | Методы                                                                                             |
|------------------------|----------------------------------------|--------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|
| Пользователь           | Авторизация и аутентификация           | Пользователь логинится в систему, используя свой логин и пароль.         | `LoginController.login(String token)`                                                             |
| Пользователь           | Выход из системы                       | Пользователь выходит из системы.                                         | `LoginController.logout()`                                                                        |
| Пользователь           | Получение информации о пользователе     | Получение данных пользователя по его логину.                             | `UsersRepository.getUserByLogin(String login)`                                                    |
| Пользователь           | Получение информации о пользователе     | Получение данных пользователя по его логину.                             | `UserService.loadUserByUsername(String username)`                                                 |
| Пользователь, Администратор | Управление сотрудниками                 | Получение списка сотрудников по фильтру.                                   | `EmployeeRepository.getEmployeesByFilter(FilterDto filter)`                                       |
| Пользователь, Администратор | Управление сотрудниками                 | Получение списка сотрудников по старому фильтру.                           | `EmployeeRepository.getEmployeesByFilterOld(String name, String lastName, Double salary, Integer salaryOp)` |
| Пользователь, Администратор | Управление сотрудниками                 | Получение всех сотрудников.                                                | `EmployeeRepository.getEmployees()`                                                               |
| Пользователь, Администратор | Управление сотрудниками                 | Получение сотрудников по имени.                                            | `EmployeeRepository.getByName(String name)`                                                       |
| Пользователь, Администратор | Управление сотрудниками                 | Получение всех сотрудников с аутентификацией.                              | `EmployeeController.getAll(AuthUser authUser)`                                                    |
| Пользователь, Администратор | Управление сотрудниками                 | Получение сотрудников по фильтру с аутентификацией.                        | `EmployeeController.getByFilter(FilterDto filter, AuthUser authUser)`                             |
| Пользователь, Администратор | Управление менеджерами                  | Получение менеджеров по фильтру.                                           | `ManagerRepository.getEmployeesByFilter(FilterDto filter)`                                        |
| Пользователь, Администратор | Управление менеджерами                  | Получение менеджеров по имени и фамилии.                                   | `ManagerRepository.getEmployeesByFilter(String name, String lastName)`                            |
| Пользователь, Администратор | Управление менеджерами                  | Получение всех менеджеров.                                                 | `ManagerRepository.getManagers()`                                                                 |
| Пользователь, Администратор | Управление менеджерами                  | Получение менеджеров по фамилии.                                           | `ManagerRepository.getByLastname(String lastName)`                                                |
| Пользователь, Администратор | Управление менеджерами                  | Получение всех менеджеров.                                                 | `ManagerController.getAll()`                                                                      |
| Пользователь, Администратор | Управление департаментами               | Получение всех департаментов.                                              | `DepartmentRepository.getDepartments()`                                                           |
| Администратор           | Назначение начальника департамента      | Назначение менеджера начальником департамента.                             | `DepartmentRepository.assignManagerToDepartment(Long departmentId, Long managerId)`               |
| Администратор           | Назначение сотрудника в департамент     | Назначение сотрудника в департамент.                                        | `DepartmentRepository.assignEmployeeToDepartment(Long departmentId, Long employeeId)`             |
| Администратор           | Перевод сотрудника между департаментами | Перевод сотрудника или менеджера в другой департамент.                      | `DepartmentRepository.transferEmployee(Long employeeId, Long newDepartmentId)`                    |
| Менеджер                | Управление подчиненными                | Получение списка подчиненных менеджера.                                     | `ManagerRepository.getSubordinates(Long managerId)`                                               |
| Менеджер                | Управление подчиненными                | Назначение сотрудника подчиненным менеджера.                                | `ManagerRepository.assignEmployee(Long managerId, Long employeeId)`                               |
| Менеджер                | Генерация отчетов                      | Генерация отчетов по структуре департаментов, количеству сотрудников и зарплате за месяц. | `ReportService.generateDepartmentReport(Long departmentId, String month)`                           |

</details>

</details>


---

## Реализация (development approach)

### Краткое резюме:

**Системный дизайн:** Мы выбрали монолитную архитектуру для быстрого выхода на рынок на этапе проверки продуктовой гипотезы.
**Внутренняя архитектура:** Классическая многослойная (N-tier) архитектура для обеспечения простоты и удобства разработки учебного проекта.

Если вы не знаете, с чего начать писать код, давайте рассмотрим некоторые подходы. Для начала обратимся к нашему разделу, в котором выбран технологический стек, и к ADR. Мы начнем с базовых технологий: Spring Boot, OpenApi (Swagger), REST-API и библиотек для логгирования Log4j2 и сокращения синтаксиса Lombok.

### Уровень 1. Подготовка точки входа и скелета приложения

<details>
<summary> &#128736; Уровень 1. Подготовка точки входа и скелета приложения</summary>

### Этап 1: Создание точки входа в приложение

<details>
<summary> &#128736; Создание точки входа в приложение </summary>

### Шаг 1: Создание точки входа в приложение

1. **Создание класса запуска приложения:**
   - Создадим класс, который будет точкой входа в наше приложение, используя Spring Boot.

```java
package com.example.ems;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmsApplication.class, args);
    }
}
```

### Шаг 2: Настройка сервисного слоя

1. **Создание пакетов для сервиса:**
   - Создадим пакеты для управления департаментами, менеджерами и сотрудниками.

2. **Создание сервисов:**
   - В каждом сервисе применим логгер для вывода приветственного сообщения.

### Bash скрипт для создания скелетов классов сервисов

Создайте Bash скрипт, который создаст три пустых файла для классов сервисов (DepartmentService, ManagerService и EmployeeService) в текущей директории. Следите за директориями и наименованием пакетов.

 - Создайте файл `create_service_classes.sh` со следующим содержимым:

```bash
#!/bin/bash

# Создание директории, если она не существует
mkdir -p src/main/java/com/example/ems/service

# Создание пустых файлов для классов сервисов
touch src/main/java/com/example/ems/service/DepartmentService.java
touch src/main/java/com/example/ems/service/ManagerService.java
touch src/main/java/com/example/ems/service/EmployeeService.java

echo "Скелеты классов сервисов созданы в src/main/java/com/example/ems/service"
```

 - Сделайте файл исполняемым:

```bash
chmod +x create_service_classes.sh
```

 - Запустите скрипт:

```bash
./create_service_classes.sh
```

- Напишите классы сервисов
  
```java
package com.example.ems.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

public class DepartmentService {
    private static final Logger logger = LoggerFactory.getLogger(DepartmentService.class);

    public DepartmentService() {
        logger.info("DepartmentService initialized");
    }
}
и другие сервисы
```

### Шаг 3: Настройка базовой конфигурации Spring

1. **Настройка конфигурации без использования аннотаций, с использованием бинов:**

```java
package com.example.ems.config;

import com.example.ems.service.DepartmentService;
import com.example.ems.service.EmployeeService;
import com.example.ems.service.ManagerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public DepartmentService departmentService() {
        return new DepartmentService();
    }

    @Bean
    public EmployeeService employeeService() {
        return new EmployeeService();
    }

    @Bean
    public ManagerService managerService() {
        return new ManagerService();
    }
}
```

2. **Добавление зависимости Log4j2 для логирования:**
   - Добавьте в файл `pom.xml` зависимость для Log4j2.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-log4j2</artifactId>
    <version>2.7.3</Version>
</dependency>
```

3. **Настройка файла конфигурации Log4j2 (log4j2.xml):**

Файл переместите в папку ресурсов.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="WARN">
    <Appenders>
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n"/>
        </Console>
    </Appenders>
    <Loggers>
        <Root level="info">
            <AppenderRef ref="Console"/>
        </Root>
    </Loggers>
</Configuration>
```
</details>

### Этап 2: Общение с приложением через HTTP (REST API)

<details>
<summary> &#128736; Rest controllers </summary>
   
Далее, когда создан "скелет" сервисов, для того чтобы сделать работу с приложением наглядной, создадим слой REST-контроллеров и настроим Swagger для проверки состояния сервисов. Это позволит взаимодействовать с приложением через HTTP и обеспечит удобную документацию и тестирование API.

#### Что нужно сделать:

1. **Создание REST-контроллеров:**
   - Создать три REST-контроллера для управления департаментами, менеджерами и сотрудниками.
   - Добавить базовые эндпоинты для проверки состояния сервисов (health).

2. **Настройка Swagger:**
   - Подключить Swagger для автоматической генерации документации по нашему API.
   - Настроить базовый конфигурационный класс для Swagger.

#### Подробные шаги:

#### Шаг 1: Добавление зависимостей Springdoc OpenAPI

Добавьте следующую зависимость в `pom.xml` для использования Springdoc OpenAPI:

```xml
<dependencies>
    <!-- Другие ваши зависимости -->

    <!-- Springdoc OpenAPI dependency -->
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        <version>2.3.0</version>
    </dependency>
</dependencies>
```

Следите за версионностью. Пакеты должны быть совместимимы со Spring boot.

#### Шаг 2: Конфигурационный класс для Springdoc OpenAPI

Создайте конфигурационный класс для настройки OpenAPI:

```java
package com.example.ems.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Employee Management System API")
                        .version("1.0")
                        .description("API documentation for the Employee Management System"));
    }
}
```

#### Шаг 3: Файл `application.properties`

Добавить в файл `src/main/resources/application.properties`:

```properties
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

Если нужно отключить Swagger:
springdoc.swagger-ui.disable-swagger-default-url=true

#### Шаг 4: Создайте REST-контроллеры

Если у вас еще нет контроллеров, создайте их:

```java
....

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("/health")
    public String checkHealth() {
        return "Department Service is up and running";
    }
}
```

#### Шаг 4: Проверка доступности Swagger UI

- Запустите ваше приложение.
- Перейдите по адресу `http://localhost:8080/swagger-ui/` для просмотра документации Swagger.
- Проверьте эндпоинты `/api/departments/health`, `/api/managers/health` и `/api/employees/health` через Swagger или напрямую через браузер или инструмент, такой как Postman.

</details>

### Этап 3: Работа с базой данных (JPA)

<details>
<summary> &#128736; Работа с базой данных (JPA) </summary>

Для того чтобы начать работу с базой данных в Spring Boot, необходимо выполнить несколько шагов по настройке и созданию необходимых классов и зависимостей.

#### Шаг 1: Настройка зависимостей в `pom.xml`

Добавьте зависимости для работы с JPA и PostgreSQL в `pom.xml`:

```xml
<dependencies>
    <!-- Spring Data JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
        <version>${spring.boot.version}</version>
    </dependency>

    <!-- PostgreSQL Driver -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <version>42.3.6</version>
    </dependency>
</dependencies>
```

#### Шаг 2: Настройки подключения к базе данных

Добавьте настройки подключения к базе данных в файл `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=admin
spring.datasource.driver-class-name=org.postgresql.Driver

# Настройки Hibernate
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
```

#### Шаг 3: Создание сущностей JPA

Создайте классы сущностей для представления таблиц в базе данных. Например, для сущности `Employee`:

```java
package com.example.ems.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String department;
}
```

#### Шаг 4: Создание репозиториев JPA

Создайте интерфейсы репозиториев для доступа к данным. Например, для сущности `Employee`:

```java
package com.example.ems.repository;

import com.example.ems.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
```

#### Шаг 5: Создание сервисного слоя

Создайте сервис для управления логикой работы с сущностями. Например, для `Employee`:

```java
package com.example.ems.service;

import com.example.ems.model.Employee;
import com.example.ems.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }
}
```

#### Шаг 6: Создание REST-контроллеров для доступа к данным

Создайте контроллеры для предоставления API доступа к данным. Например, для `Employee`:

```java
package com.example.ems.controller;

import com.example.ems.model.Employee;
import com.example.ems.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeService.saveEmployee(employee);
    }
}
```

### Проверка и запуск

   Перейдите в Swagger для проверки

</details>

</details>

### Уровень 2. Использование QueryDSL. Flyway
<details>
<summary> &#128736; Уровень 2. Использование QueryDSL. Flyway. </summary>

### Настройка автогенерации Q-классов для безопасных запросов

В проекте выбран подход, который генерирует Q классы на основе существующей БД. Этот подход требует действующей БД с необходимыми таблицами. А также для автогенерации классов, требуется маппинг Java типов к типам в БД. 

- Важно! Обязательно добавьте директорию target/generated-sources/java как Generated Sources Root в настройках вашей IDE.
- Важно! Обратите внимание, что имена таблиц и столбцов чувствительны к регистру, и настройка tableNamePattern должна учитывать это.
- Важно! IDEA может не верно собирать проект при отключенной опции  "Delegate IDE build/run..."

### 1. Подключение необходимых зависимостей

Для начала, в вашем `pom.xml` добавьте следующие зависимости и плагин для генерации Q-классов на основе структуры базы данных:

```xml
<dependencies>
    <!-- PostgreSQL Driver -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <version>42.7.3</version>
    </dependency>

    <!-- QueryDSL SQL -->
    <dependency>
        <groupId>com.querydsl</groupId>
        <artifactId>querydsl-sql</artifactId>
        <version>5.0.0</version>
    </dependency>

    <!-- QueryDSL SQL Spring -->
    <dependency>
        <groupId>com.querydsl</groupId>
        <artifactId>querydsl-sql-spring</artifactId>
        <version>5.0.0</version>
    </dependency>

    <!-- QueryDSL APT (Annotation Processing Tool) -->
    <dependency>
        <groupId>com.querydsl</groupId>
        <artifactId>querydsl-apt</artifactId>
        <version>5.0.0</version>
        <scope>provided</scope>
    </dependency>
</dependencies>

<build>
    <plugins>
        <!-- Новый плагин для генерации Q-классов по SQL таблицам -->
        <plugin>
            <groupId>com.querydsl</groupId>
            <artifactId>querydsl-maven-plugin</artifactId>
            <version>5.0.0</version>
            <executions>
                <execution>
                    <goals>
                        <goal>export</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <jdbcDriver>org.postgresql.Driver</jdbcDriver>
                <jdbcUrl>jdbc:postgresql://localhost:5432/postgres</jdbcUrl>
                <jdbcUser>postgres</jdbcUser>
                <jdbcPassword>admin</jdbcPassword>
                <packageName>com.querydsl</packageName>
                <targetFolder>${project.basedir}/target/generated-sources/java</targetFolder>
                <schemaPattern>public</schemaPattern>
                <tableNamePattern>department, employee, manager, testentity</tableNamePattern>
                <typeMappings>
                    <typeMapping>
                        <table>department</table>
                        <column>id</column>
                        <type>java.lang.Long</type>
                    </typeMapping>
                    <typeMapping>
                        <table>department</table>
                        <column>description</column>
                        <type>java.lang.String</type>
                    </typeMapping>
                    <typeMapping>
                        <table>department</table>
                        <column>name</column>
                        <type>java.lang.String</type>
                    </typeMapping>
                    <typeMapping>
                        <table>employee</table>
                        <column>id</column>
                        <type>java.lang.Long</type>
                    </typeMapping>
                    <typeMapping>
                        <table>employee</table>
                        <column>department</column>
                        <type>java.lang.String</type>
                    </typeMapping>
                    <typeMapping>
                        <table>employee</table>
                        <column>name</column>
                        <type>java.lang.String</type>
                    </typeMapping>
                    <typeMapping>
                        <table>manager</table>
                        <column>id</column>
                        <type>java.lang.Long</type>
                    </typeMapping>
                    <typeMapping>
                        <table>manager</table>
                        <column>department</column>
                        <type>java.lang.String</type>
                    </typeMapping>
                    <typeMapping>
                        <table>manager</table>
                        <column>name</column>
                        <type>java.lang.String</type>
                    </typeMapping>
                    <typeMapping>
                        <table>testentity</table>
                        <column>id</column>
                        <type>java.lang.Long</type>
                    </typeMapping>
                    <typeMapping>
                        <table>testentity</table>
                        <column>name</column>
                        <type>java.lang.String</type>
                    </typeMapping>
                </typeMappings>
                <sourceFolder/>
            </configuration>
            <dependencies>
                <dependency>
                    <groupId>org.postgresql</groupId>
                    <artifactId>postgresql</artifactId>
                    <version>42.7.3</version>
                </dependency>
            </dependencies>
        </plugin>
    </plugins>
</build>
```

### 2. Подготовка базы данных

Для запуска базы данных используйте следующий скрипт Docker Compose:

```bash
#!/bin/bash

# Запуск контейнера PostgreSQL
docker-compose -f ../src/main/java/com/ems/dockerfiles/docker-compose.yml up -d
```

### 3. Проверка структуры базы данных

Для проверки таблиц и их типов выполните следующий скрипт:

```bash
#!/bin/bash

# Получение имени контейнера PostgreSQL
CONTAINER_NAME=$(docker ps --filter "ancestor=postgres" --format "{{.Names}}")

if [ -z "$CONTAINER_NAME" ]; then
  echo "PostgreSQL контейнер не найден."
  exit 1
fi

echo "Подключение к контейнеру: $CONTAINER_NAME"

# Запрос для получения информации о таблицах и столбцах
SQL_QUERY="
SELECT
    table_name,
    column_name,
    data_type,
    character_maximum_length
FROM
    information_schema.columns
WHERE
    table_schema = 'public'
ORDER BY
    table_name, ordinal_position;
"

# Выполнение SQL-запроса в контейнере PostgreSQL
docker exec -i $CONTAINER_NAME psql -U postgres -d postgres -c "$SQL_QUERY"

if [ $? -eq 0 ]; then
  echo "Запрос выполнен успешно."
else
  echo "Ошибка выполнения запроса."
  exit 1
fi
```

### 4. Настройка маппинга

Обратите внимание, что в PostgreSQL чувствительность к регистру является важным фактором. Важно использовать строчные буквы в `tableNamePattern` и `typeMappings` для корректного маппинга.

### Преимущества генерации Q-классов на основе таблиц

1. **Отражение текущей схемы БД**: Генерация Q-классов непосредственно из базы данных позволяет всегда иметь актуальные классы, соответствующие текущей схеме БД.
2. **Автоматизация**: Автоматическая генерация классов упрощает поддержку и исключает ошибки, связанные с ручным обновлением классов.
3. **Гибкость**: Поддержка различных типов БД и возможность кастомизации маппингов.

### Сравнение с генерацией Q-классов на основе сущностей

| Подход                         | Преимущества                                                                 | Недостатки                                                                 |
|--------------------------------|----------------------------------------------------------------------------|---------------------------------------------------------------------------|
| Генерация на основе таблиц     | Всегда актуальная схема, автоматизация, гибкость                            | Требует точной настройки маппингов, чувствительность к регистру            |
| Генерация на основе сущностей  | Простой процесс, автоматическое обновление при изменении сущностей           | Возможные рассогласования с реальной схемой БД, сложнее поддерживать ручной маппинг |

</details>

### Уровень 3. Использование Spring Security
<details>
<summary> &#128736; Уровень 3. Использование Spring Security </summary>

</details>

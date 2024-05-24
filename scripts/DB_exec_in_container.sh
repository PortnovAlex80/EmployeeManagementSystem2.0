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

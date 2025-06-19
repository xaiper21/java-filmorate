# java-filmorate
[Схема базы данных](diagramdb/diagramdb.png)


### Примеры запросов к DB:

- ####  Добавление пользователя 
  ```sql 
        INSERT INTO user (email, login, name, birthday)
        VALUES ('there_was_my_cat@gmail.com', 'mouse_hanter', 'Leopold', '2014-04-04');

- ####  Получение списка всех пользователей

  ```sql
        SELECT * FROM user;
      
- ####  Добавление фильма
  ```sql
        INSERT INTO film (name, description, release_date, duration, rating)
        VALUES ('Спасаем динозавров', 'Описание данного фильма', '2030-10-10', 900, 'R');

> Проверка валидности значения поля "rating" предусмотрена логикой приложения


- ####  Получение {N} топ фильмов
    ```sql
          SELECT * FROM film
          WHERE id IN (SELECT film_id FROM film_like
                          GROUP BY film_id 
                          ORDER BY COUNT (film_id) DESC
                          LIMIT N);
- ####  Создание дружбы
    ```sql
          INSERT INTO friendship (user_one_id, user_two_id)
          VALUES (1,2),
                 (2,1)
          IF EXISTS (SELECT id FROM user
                       WHERE user.id = 1 OR user.id = 2)
          ON CONFLICT DO NOTHING; 

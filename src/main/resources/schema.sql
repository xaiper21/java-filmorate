
CREATE TABLE IF NOT EXISTS users (
             id BIGINT PRIMARY KEY AUTO_INCREMENT,
             email VARCHAR(256) UNIQUE NOT NULL,
             login VARCHAR(50) UNIQUE NOT NULL,
             name VARCHAR(50),
             birthday DATE NOT NULL
);


CREATE TABLE IF NOT EXISTS film (
            id BIGINT PRIMARY KEY AUTO_INCREMENT,
            name VARCHAR(100) NOT NULL,
            description VARCHAR(200) NOT NULL,
            release_date DATE,
            duration INT,
            rating_id INT

);

CREATE TABLE IF NOT EXISTS genre (
             id INT PRIMARY KEY ,
             name VARCHAR(40) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genre (
             film_id BIGINT NOT NULL,
             genre_id INT NOT NULL,
             PRIMARY KEY (film_id, genre_id),
             CONSTRAINT fk_film_genre_film FOREIGN KEY (film_id) REFERENCES film(id) ON DELETE CASCADE,
             CONSTRAINT fk_film_genre_genre FOREIGN KEY (genre_id) REFERENCES genre(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS film_like (
             user_id BIGINT NOT NULL,
             film_id BIGINT NOT NULL,
             created_at TIMESTAMP NOT NULL DEFAULT NOW(),
             CONSTRAINT unique_ids UNIQUE (user_id, film_id),
             CONSTRAINT fk_film_like_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
             CONSTRAINT fk_film_like_film FOREIGN KEY (film_id) REFERENCES film(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS friendship (
            id LONG AUTO_INCREMENT PRIMARY KEY ,
            user_one_id BIGINT NOT NULL,
            user_two_id BIGINT NOT NULL,
            created_at TIMESTAMP NOT NULL DEFAULT NOW(),
            CONSTRAINT unique_id_users UNIQUE (user_one_id, user_two_id),
            CONSTRAINT check_user_order CHECK (user_one_id <> user_two_id),
            CONSTRAINT fk_friendship_user_one FOREIGN KEY (user_one_id) REFERENCES users(id) ON DELETE CASCADE,
            CONSTRAINT fk_friendships_user_two FOREIGN KEY (user_two_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE  IF NOT EXISTS rating (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(10) UNIQUE NOT NULL
);

 
-- ON DELETE CASCADE здесь важен: при удалении фильма или режиссёра
-- соответствующая запись в этой таблице будет удалена автоматически.

CREATE TABLE IF NOT EXISTS reviews (
         id BIGINT PRIMARY KEY AUTO_INCREMENT,
         content TEXT NOT NULL,
         is_positive BOOLEAN NOT NULL,
         user_id BIGINT NOT NULL,
         film_id BIGINT NOT NULL,
         useful INT NOT NULL DEFAULT 0,
         CONSTRAINT fk_review_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
         CONSTRAINT fk_review_film FOREIGN KEY (film_id) REFERENCES film(id) ON DELETE CASCADE
    );
-- поле useful из json задачи переименовано в useful_rating во избежание конфликтов с ключевыми словами SQL.

CREATE TABLE IF NOT EXISTS review_likes (
         review_id BIGINT NOT NULL,
         user_id BIGINT NOT NULL,
        is_like BOOLEAN NOT NULL, -- true для лайка, false для дизлайка
        PRIMARY KEY (review_id, user_id),
        CONSTRAINT fk_review_like_review FOREIGN KEY (review_id) REFERENCES reviews(id) ON DELETE CASCADE,
        CONSTRAINT fk_review_like_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );
--Использование одного поля is_like вместо отдельных эндпоинтов для удаления лайка/дизлайка позволяет реализовать логику
--"один пользователь — одна оценка". Если пользователь меняет лайк на дизлайк, вы просто делаете UPDATE этой записи.
--    Удаление оценки — DELETE записи.

CREATE TABLE IF NOT EXISTS events (
          event_id BIGINT PRIMARY KEY AUTO_INCREMENT,
          event_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
          user_id BIGINT NOT NULL,
          event_type ENUM('LIKE', 'REVIEW', 'FRIEND') NOT NULL,
          operation_type ENUM('REMOVE', 'ADD', 'UPDATE') NOT NULL,
          entity_id BIGINT NOT NULL,
          CONSTRAINT fk_event_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );
--event_type и operation_type: Использование ENUM (важен ваш комментарий по этому поводу)
--entity_id: Это идентификатор сущности, с которой связано событие (ID фильма для LIKE, ID отзыва для REVIEW,
--ID друга для FRIEND).
--Здесь не ставится внешний ключ, так как поле ссылается на разные таблицы в зависимости от event_type.
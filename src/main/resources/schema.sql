
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

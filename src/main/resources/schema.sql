
   CREATE TABLE IF NOT EXISTS user(
       id SERIAL PRIMARY KEY,
       email VARCHAR(256) UNIQUE NOT NULL,
       login VARCHAR(50)  UNIQUE NOT NULL,
       name VARCHAR(50) NOT NULL,
       birthday DATE NOT NULL
   );

    CREATE TABLE IF NOT EXISTS film(
        id SERIAL PRIMARY KEY,
        name VARCHAR(100) NOT NULL,
        description VARCHAR(200) NOT NULL,
        release_date DATE,
        duration INT,
        rating VARCHAR(10)
    );

   CREATE TABLE IF NOT EXISTS film_genre(
       film_id BIGINT NOT NULL,
       genre_id INT NOT NULL
   );

   CREATE TABLE IF NOT EXISTS genre(
       id SERIAL PRIMARY KEY,
       name VARCHSR(40) UNIQUE NOT NULL
   );

   CREATE TABLE film_like (
        user_id BIGINT NOT NULL,
        film_id INT NOT NULL,
        created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
        PRIMARY KEY (user_id, film_id),
        CONSTRAINT fk_film_likes_user
        FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
        CONSTRAINT fk_film_likes_film
        FOREIGN KEY (film_id) REFERENCES film(film_id) ON DELETE CASCADE
   );

   CREATE TYPE friendship_status AS ENUM ('pending', 'accepted', 'blocked');

   CREATE TABLE friendships (
       user_one_id BIGINT NOT NULL,
       user_two_id BIGINT NOT NULL,
       status friendship_status NOT NULL,
       action_user_id BIGINT NOT NULL,
       created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
       updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
       PRIMARY KEY (user_one_id, user_two_id),

       CONSTRAINT check_user_order CHECK (user_one_id < user_two_id),

       CONSTRAINT fk_friendships_user_one
       FOREIGN KEY (user_one_id) REFERENCES user(id) ON DELETE CASCADE,
       CONSTRAINT fk_friendships_user_two
       FOREIGN KEY (user_two_id) REFERENCES user(id) ON DELETE CASCADE,
       CONSTRAINT fk_friendships_action_user
       FOREIGN KEY (action_user_id) REFERENCES user(id) ON DELETE CASCADE
   );


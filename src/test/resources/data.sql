INSERT INTO users (name, login, email, birthday) VALUES ('Иванов Иван', 'ivanov', 'ivanov@yandex.ru', '2000-02-20');
INSERT INTO users (name, login, email, birthday) VALUES ('Петр Петров', 'petrov', 'petrov@yandex.ru', '1989-12-28');
INSERT INTO users (name, login, email, birthday) VALUES ('Мария Петрова', 'mpetrova', 'mpetrova@yandex.ru', '1990-11-03');
INSERT INTO users (name, login, email, birthday) VALUES ('Алексей Сидоров', 'sidorov', 'sidorov@yandex.ru', '1997-02-05');
INSERT INTO users (name, login, email, birthday) VALUES ('Наталья Алексеева', 'alekseeva', 'alekseeva@yandex.ru', '2001-01-30');

INSERT INTO friendship (user_id, friend_id) VALUES (1, 3);
INSERT INTO friendship (user_id, friend_id) VALUES (1, 5);
INSERT INTO friendship (user_id, friend_id) VALUES (2, 3);
INSERT INTO friendship (user_id, friend_id) VALUES (5, 4);

INSERT INTO mpa (name) VALUES ('G');
INSERT INTO mpa (name) VALUES ('PG');
INSERT INTO mpa (name) VALUES ('PG-13');
INSERT INTO mpa (name) VALUES ('R');
INSERT INTO mpa (name) VALUES ('NC-17');

INSERT INTO genres (name) VALUES ('Комедия');
INSERT INTO genres (name) VALUES ('Драма');
INSERT INTO genres (name) VALUES ('Мультфильм');
INSERT INTO genres (name) VALUES ('Триллер');
INSERT INTO genres (name) VALUES ('Документальный');
INSERT INTO genres (name) VALUES ('Боевик');

INSERT INTO films (name, description, release_date, duration, mpa_id) VALUES ('Дурак', 'Фильм про дурака', '2010-02-20', 120, 1);
INSERT INTO films (name, description, release_date, duration, mpa_id) VALUES ('Буратино', 'Фильм про деревянного мальчика', '1969-11-03', 90, 2);
INSERT INTO films (name, description, release_date, duration, mpa_id) VALUES ('Моряк', 'Фильм про моряка', '1956-01-24', 95, 1);

INSERT INTO films_genres (film_id, genre_id) VALUES (1, 1);
INSERT INTO films_genres (film_id, genre_id) VALUES (1, 2);
INSERT INTO films_genres (film_id, genre_id) VALUES (2, 1);
INSERT INTO films_genres (film_id, genre_id) VALUES (3, 1);

INSERT INTO films_likes (film_id, user_id) VALUES (1, 1);
INSERT INTO films_likes (film_id, user_id) VALUES (2, 1);
INSERT INTO films_likes (film_id, user_id) VALUES (2, 2);
INSERT INTO films_likes (film_id, user_id) VALUES (3, 2);

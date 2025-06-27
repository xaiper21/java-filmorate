
MERGE INTO rating (ID, NAME) KEY(ID) VALUES (1, 'G');
MERGE INTO rating (ID, NAME) KEY(ID) VALUES (2, 'PG');
MERGE INTO rating (ID, NAME) KEY(ID) VALUES (3, 'PG-13');
MERGE INTO rating (ID, NAME) KEY(ID) VALUES (4, 'R');
MERGE INTO rating (ID, NAME) KEY(ID) VALUES (5, 'NC-17');


MERGE INTO genre (ID, NAME) KEY(ID) VALUES (1, 'Комедия');
MERGE INTO genre (ID, NAME) KEY(ID) VALUES (2, 'Драма');
MERGE INTO genre (ID, NAME) KEY(ID) VALUES (3, 'Мультфильм');
MERGE INTO genre (ID, NAME) KEY(ID) VALUES (4, 'Триллер');
MERGE INTO genre (ID, NAME) KEY(ID) VALUES (5, 'Документальный');
MERGE INTO genre (ID, NAME) KEY(ID) VALUES (6, 'Боевик');
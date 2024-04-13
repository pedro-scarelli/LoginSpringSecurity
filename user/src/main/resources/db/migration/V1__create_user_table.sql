create table users (
    id UUID PRIMARY KEY,
    name VARCHAR(100),
    mail VARCHAR(100) UNIQUE,
    login VARCHAR(50) UNIQUE,
    password VARCHAR(100)
);

--As senhas desses usuários é senha123
INSERT INTO users (id, name, mail, login, password) 
VALUES 
    ('d8311b34-5b8b-46b3-8556-5a78f46e39ae', 'João', 'joao@example.com', 'joao123', '$2a$10$hIPL.snkUGjCzqRy0G5Xl.WImJvN71ACZTkQqSEOsmxR8jwN.wg9a'),
    ('a4e38f33-ff2e-4ff0-8195-91e8794c0b6a', 'Maria', 'maria@example.com', 'maria456', '$2a$10$hIPL.snkUGjCzqRy0G5Xl.WImJvN71ACZTkQqSEOsmxR8jwN.wg9a'),
    ('51fb2551-d796-4761-8590-f4fb688f4bdc', 'Pedro', 'pedro@example.com', 'pedro789', '$2a$10$hIPL.snkUGjCzqRy0G5Xl.WImJvN71ACZTkQqSEOsmxR8jwN.wg9a'),
    ('f8ec1d7c-7e9d-4d2a-96c3-19a7c671c62d', 'Ana', 'ana@example.com', 'ana123', '$2a$10$hIPL.snkUGjCzqRy0G5Xl.WImJvN71ACZTkQqSEOsmxR8jwN.wg9a'),
    ('db48c1fd-8673-4aaf-8376-d0721e31dc09', 'Lucas', 'lucas@example.com', 'lucas456', '$2a$10$hIPL.snkUGjCzqRy0G5Xl.WImJvN71ACZTkQqSEOsmxR8jwN.wg9a');



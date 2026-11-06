CREATE TABLE IF NOT EXISTS users (
                                     id            SERIAL PRIMARY KEY,
                                     username      VARCHAR(100) NOT NULL UNIQUE,
    name          VARCHAR(255),
    email         VARCHAR(255) NOT NULL UNIQUE,
    password      VARCHAR(255) NOT NULL,
    date_of_birth DATE         NOT NULL
    );

CREATE TABLE IF NOT EXISTS media (
                                     id              SERIAL PRIMARY KEY,
                                     creator_id      INTEGER      NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    title           VARCHAR(255) NOT NULL,
    description     TEXT,
    media_type      VARCHAR(20)  NOT NULL,
    release_year    INTEGER,
    age_restriction INTEGER,
    duration        INTEGER,
    -- Genres als kommagetrennte Enum Namen, zum Beispiel ACTION,THRILLER
    genres          VARCHAR(500) NOT NULL DEFAULT ''
    );

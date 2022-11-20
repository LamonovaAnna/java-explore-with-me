DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS compilations CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS events_compilations CASCADE;

CREATE TABLE users
(
    user_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
    );

CREATE TABLE categories
(
    category_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE events
(
    event_id           BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    annotation         VARCHAR(350) NOT NULL,
    category_id        BIGINT REFERENCES categories (category_id) NOT NULL,
    description        VARCHAR(1500) NOT NULL,
    event_date         TIMESTAMP NOT NULL,
    initiator_id       BIGINT REFERENCES users (user_id) ON DELETE CASCADE NOT NULL,
    paid               BOOLEAN NOT NULL,
    participant_limit  BIGINT,
    created_on         TIMESTAMP,
    published_on       TIMESTAMP,
    request_moderation BOOLEAN,
    confirmed_requests BIGINT,
    state              TEXT CHECK (state IN ('PUBLISHED', 'PENDING', 'CANCELED')) NOT NULL,
    title              VARCHAR(150) NOT NULL,
    available          BOOLEAN,
    lat                FLOAT,
    lon                FLOAT,
    views              BIGINT
);

CREATE TABLE requests
(
    request_id   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    status       TEXT CHECK (status IN ('CONFIRMED', 'PENDING', 'CANCELED', 'REJECTED')) NOT NULL,
    event_id     BIGINT REFERENCES events (event_id) ON DELETE CASCADE NOT NULL,
    requester_id BIGINT REFERENCES users (user_id) ON DELETE CASCADE NOT NULL ,
    created      TIMESTAMP WITHOUT TIME ZONE
    );

CREATE TABLE compilations
(
    compilation_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    pinned         BOOLEAN,
    title          VARCHAR(150) NOT NULL
);

CREATE TABLE events_compilations
(
    ec_compilation_id BIGINT REFERENCES compilations (compilation_id) ON DELETE CASCADE,
    ec_event_id BIGINT REFERENCES events (event_id) ON DELETE CASCADE
);
DROP TABLE IF EXISTS endpoint_hits CASCADE;

CREATE TABLE endpoint_hits
(
    endpoint_hit_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    app             VARCHAR(512) NOT NULL,
    uri             VARCHAR(512) NOT NULL,
    ip              VARCHAR(512) NOT NULL,
    timestamp       TIMESTAMP WITHOUT TIME ZONE
);
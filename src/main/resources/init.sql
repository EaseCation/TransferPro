CREATE TABLE IF NOT EXISTS tspro_servers (
    `group`          varchar(32)   NOT NULL,
    server           varchar(32)   NOT NULL,
    address          TEXT          NOT NULL,
    port             int           NOT NULL,
    player_count     int DEFAULT 0 NOT NULL,
    max_player_count int DEFAULT 0 NOT NULL,
    tps              FLOAT DEFAULT 0 NOT NULL,
    last_update      datetime      NULL,
    PRIMARY KEY (`group`, server)
);
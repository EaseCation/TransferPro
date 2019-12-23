CREATE TABLE tspro_servers IF NOT EXISTS (
    `group`          varchar(32)   NOT NULL,
    server           varchar(32)   NOT NULL,
    address          varchar(16)   NOT NULL,
    port             int           NOT NULL,
    player_count     int DEFAULT 0 NOT NULL,
    max_player_count int DEFAULT 0 NOT NULL,
    tps              int DEFAULT 0 NOT NULL,
    last_update      datetime      NULL,
    PRIMARY KEY (`group`, server)
);
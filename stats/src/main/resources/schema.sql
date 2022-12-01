CREATE TABLE IF NOT EXISTS stats (
  id            BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  app           VARCHAR(255) NOT NULL,
  uri           VARCHAR(512) NOT NULL,
  ip            VARCHAR(255) NOT NULL,
  time_stamp    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
  CONSTRAINT pk_stats PRIMARY KEY (id)
);


CREATE TABLE IF NOT EXISTS temperature
(
    id                  BIGINT       NOT NULL AUTO_INCREMENT,
    measure_id          TINYTEXT     NOT NULL,
    sensor_id           INT          NOT NULL,
    current_temperature INT          NOT NULL,
    status              VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
) ENGINE = INNODB;

ALTER TABLE temperature
    AUTO_INCREMENT = 100;

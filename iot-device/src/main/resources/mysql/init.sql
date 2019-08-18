CREATE TABLE IF NOT EXISTS temperature
(
    measure_id          INT AUTO_INCREMENT,
    sensor_id           TINYINT      NOT NULL,
    current_temperature TINYINT      NOT NULL,
    status              VARCHAR(255) NOT NULL,
    PRIMARY KEY (measure_id)
) ENGINE = INNODB;

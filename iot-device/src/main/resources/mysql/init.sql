CREATE TABLE IF NOT EXISTS temperature
(
    measure_id          INT AUTO_INCREMENT,
    sensor_id           INT          NOT NULL,
    current_temperature INT          NOT NULL,
    status              VARCHAR(255) NOT NULL,
    PRIMARY KEY (measure_id)
) ENGINE = INNODB;

CREATE TABLE IF NOT EXISTS lt_platform_devices_mobile_web (
                                                              id INT PRIMARY KEY AUTO_INCREMENT,
                                                              platform_key VARCHAR(255) NOT NULL,
    os_name VARCHAR(255),
    os_version VARCHAR(255),
    device_name VARCHAR(255),
    browser_name VARCHAR(255),
    os_id VARCHAR(255),
    FOREIGN KEY (platform_key) REFERENCES platform_devices_mobile_web(platform_key),
    INDEX (platform_key)
    );

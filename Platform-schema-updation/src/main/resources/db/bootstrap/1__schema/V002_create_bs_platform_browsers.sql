CREATE TABLE IF NOT EXISTS bs_platform_browsers (
                                                    id INT PRIMARY KEY AUTO_INCREMENT,
                                                    platform_key VARCHAR(255) NOT NULL,
    os_name VARCHAR(255),
    os_version VARCHAR(255),
    browser_name VARCHAR(255),
    browser_version VARCHAR(255),
    FOREIGN KEY (platform_key) REFERENCES platform_browsers(platform_key),
    INDEX (platform_key)
    );

CREATE TABLE IF NOT EXISTS platform_browsers (
                                                 id INT PRIMARY KEY AUTO_INCREMENT,
                                                 platform_key VARCHAR(255) NOT NULL UNIQUE,
    os_name VARCHAR(255),
    os_version VARCHAR(255),
    browser_name VARCHAR(255),
    browser_version VARCHAR(255),
    is_bs_supported BOOLEAN,
    is_lt_supported BOOLEAN,
    is_sl_supported BOOLEAN,
    helix_supported BOOLEAN,
    INDEX (platform_key)
    );

CREATE TABLE upload_file (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    origin_name VARCHAR(255) NOT NULL,
    saved_name VARCHAR(255) NOT NULL,
    file_size BIGINT NOT NULL,
    content_type VARCHAR(100),
    reg_date DATETIME DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE upload_file ADD COLUMN deleted TINYINT DEFAULT 0;
ALTER TABLE upload_file ADD COLUMN download_count INT DEFAULT 0;

select * from upload_file;
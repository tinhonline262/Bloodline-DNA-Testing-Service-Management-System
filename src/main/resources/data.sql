CREATE TABLE IF NOT EXISTS dna_service (
                                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                           service_name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    service_type VARCHAR(50) NOT NULL
    );

CREATE TABLE IF NOT EXISTS customer (
                                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                        name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL
    );

CREATE TABLE IF NOT EXISTS booking (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       customer_id BIGINT,
                                       service_id BIGINT,
                                       collection_method VARCHAR(50) NOT NULL,
    appointment_date DATETIME NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customer(id),
    FOREIGN KEY (service_id) REFERENCES dna_service(id)
    );

INSERT INTO dna_service (service_name, price, service_type) VALUES
                                                                ('Xét nghiệm Paternity', 2000000.00, 'CIVIL_Paternity'),
                                                                ('Xét nghiệm Maternity', 2000000.00, 'CIVIL_Maternity'),
                                                                ('Xét nghiệm Sibling', 2500000.00, 'CIVIL_Sibling'),
                                                                ('Xét nghiệm Legal', 3000000.00, 'ADMINISTRATIVE_Legal');
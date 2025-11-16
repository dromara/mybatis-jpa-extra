CREATE TABLE IF NOT EXISTS test_user (
	id BIGINT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(100) NOT NULL,
	email VARCHAR(100),
	data_source VARCHAR(50)
);
                
delete from test_user;
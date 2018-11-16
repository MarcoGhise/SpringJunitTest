DROP TABLE IF EXISTS Customers;

CREATE TABLE Customers (
  id INT NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(30),
  last_name  VARCHAR(30),
  PRIMARY KEY (`id`));
DROP TABLE Customers IF EXISTS;

CREATE TABLE Customers (
  id         INTEGER IDENTITY PRIMARY KEY,
  first_name VARCHAR(30),
  last_name  VARCHAR(30)
);
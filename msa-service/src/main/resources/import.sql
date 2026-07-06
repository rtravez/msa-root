INSERT INTO persons (creation_date, creation_host, creation_user, status, address, age, gender, identification, lastname, "name", telephone) VALUES(NOW(), '127.0.0.1', 'admin', true, 'La Concepción', 36, 'M', '1717172631', 'Administrador', 'Administrador', '0987350473');

INSERT INTO customers (creation_date, creation_host, creation_user, status, "password", username, person_id) VALUES (NOW(), '127.0.0.1', 'admin', true, '$2a$10$itMT/hPoLNo/FqNuzh69LeNMMUsca/j2WIdOn7P0ZYhjs6rKK/way', 'admin', 1);

INSERT INTO roles (creation_date, creation_host, creation_user, name, status) VALUES (NOW(), '127.0.0.1', 'admin', 'ROLE_ADMIN', true);

INSERT INTO role_customers (creation_date, creation_host, creation_user, customer_id, rol_id, status) VALUES (NOW(), '127.0.0.1', 'admin', 1, 1, true);

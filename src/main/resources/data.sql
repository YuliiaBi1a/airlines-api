--Airport
INSERT INTO airports (name, city, country, code) VALUES ('John F. Kennedy International Airport', 'New York', 'USA', 'JFK');
INSERT INTO airports (name, city, country, code) VALUES ('Heathrow Airport', 'London', 'UK', 'LHR');
INSERT INTO airports (name, city, country, code) VALUES ('Charles de Gaulle Airport', 'Paris', 'France', 'CDG');
INSERT INTO airports (name, city, country, code) VALUES ('Haneda Airport', 'Tokyo', 'Japan', 'HND');
INSERT INTO airports (name, city, country, code) VALUES ('Dubai International Airport', 'Dubai', 'UAE', 'DXB');
INSERT INTO airports (name, city, country, code) VALUES ('Frankfurt Airport', 'Frankfurt', 'Germany', 'FRA');
INSERT INTO airports (name, city, country, code) VALUES ('Singapore Changi Airport', 'Singapore', 'Singapore', 'SIN');
INSERT INTO airports (name, city, country, code) VALUES ('Sydney Kingsford Smith Airport', 'Sydney', 'Australia', 'SYD');
INSERT INTO airports (name, city, country, code) VALUES ('Toronto Pearson International Airport', 'Toronto', 'Canada', 'YYZ');
INSERT INTO airports (name, city, country, code) VALUES ('Los Angeles International Airport', 'Los Angeles', 'USA', 'LAX');

--Flight

INSERT INTO flights (departure_airport_id, arrival_airport_id, departure_time, arrival_time, available_seats, status, price)
VALUES (1, 2, '2025-01-10 14:00:00', '2025-01-10T18:00:00', 150, 'EXPIRED', 300.00);
INSERT INTO flights (departure_airport_id, arrival_airport_id, departure_time, arrival_time, available_seats, status, price)
VALUES (2, 3, '2025-02-12 09:00:00', '2025-01-12T13:00:00', 100, 'AVAILABLE', 250.00);
INSERT INTO flights (departure_airport_id, arrival_airport_id, departure_time, arrival_time, available_seats, status, price)
VALUES (3, 4, '2025-02-15 20:00:00', '2025-01-16T04:00:00', 120, 'AVAILABLE', 350.00);
INSERT INTO flights (departure_airport_id, arrival_airport_id, departure_time, arrival_time, available_seats, status, price)
VALUES (4, 5, '2025-02-18 07:00:00', '2025-01-18T11:00:00', 200, 'AVAILABLE', 400.00);
INSERT INTO flights (departure_airport_id, arrival_airport_id, departure_time, arrival_time, available_seats, status, price)
VALUES (5, 6, '2025-02-20 10:30:00', '2025-01-20T15:00:00', 180, 'AVAILABLE', 450.00);
INSERT INTO flights (departure_airport_id, arrival_airport_id, departure_time, arrival_time, available_seats, status, price)
VALUES (6, 7, '2025-02-22 12:00:00', '2025-01-22T16:30:00', 140, 'AVAILABLE', 500.00);
INSERT INTO flights (departure_airport_id, arrival_airport_id, departure_time, arrival_time, available_seats, status, price)
VALUES (7, 8, '2025-02-25 18:00:00', '2025-01-26T06:00:00', 220, 'AVAILABLE', 600.00);
INSERT INTO flights (departure_airport_id, arrival_airport_id, departure_time, arrival_time, available_seats, status, price)
VALUES (8, 9, '2025-02-28 08:00:00', '2025-01-28T20:00:00', 250, 'AVAILABLE', 700.00);

--Role
INSERT INTO roles (id, name) VALUES (1, 'ROLE_CLIENT');
INSERT INTO roles (id, name) VALUES (2, 'ROLE_ADMIN');

--User ADM

-- Locations
INSERT INTO locations (name, country, city, location_code)
VALUES ('Istanbul Airport', 'Turkey', 'Istanbul', 'IST'),
       ('Sabiha Gökçen Airport', 'Turkey', 'Istanbul', 'SAW'),
       ('Istanbul City Center', 'Turkey', 'Istanbul', 'CCIST'),
       ('London Heathrow Airport', 'UK', 'London', 'LHR'),
       ('Webley Stadium', 'UK', 'London', 'WSLDN');

-- Transportations
INSERT INTO transportations (origin_location_id, destination_location_id, transportation_type)
VALUES (1, 2, 'BUS'),    -- ROUTE
       (2, 3, 'FLIGHT'),
       (3, 4, 'UBER'),

       (1, 4, 'FLIGHT'), -- ROUTE

       (1, 3, 'FLIGHT'), -- ROUTE
       (3, 4, 'SUBWAY'),

       (1, 2, 'UBER'),   -- ROUTE
       (2, 4, 'FLIGHT');

-- Transportation Operating Days
INSERT INTO transportation_operating_days (transportation_id, operating_day)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (1, 6),
       (1, 7),
       (2, 1),
       (2, 2),
       (2, 3),
       (2, 4),
       (2, 5),
       (2, 6),
       (2, 7),
       (3, 1),
       (3, 2),
       (3, 3),
       (3, 4),
       (3, 5),
       (3, 6),
       (3, 7),
       (4, 1),
       (4, 2),
       (4, 3),
       (4, 4),
       (4, 5),
       (4, 6),
       (4, 7),
       (5, 1),
       (5, 2),
       (5, 3),
       (5, 4),
       (5, 5),
       (5, 6),
       (5, 7),
       (6, 1),
       (6, 2),
       (6, 3),
       (6, 4),
       (6, 5),
       (6, 6),
       (6, 7),
       (7, 1),
       (7, 2),
       (7, 3),
       (7, 4),
       (7, 5),
       (7, 6),
       (7, 7),
       (8, 1),
       (8, 2),
       (8, 3),
       (8, 4),
       (8, 5),
       (8, 6),
       (8, 7);



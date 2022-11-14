START TRANSACTION;

-- DATA ELIMINATION
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE gav.assistance_order;
TRUNCATE TABLE gav.assistance;
TRUNCATE TABLE gav.user;
SET FOREIGN_KEY_CHECKS = 1;

-- USER INSERT
INSERT INTO gav.user (id, email_address, first_name, last_name, telephone_number, type, debts)
VALUES
    (UUID_TO_BIN('5f700ac8-0efe-4683-9798-6595c52d1668'), 'pedro@gmail.com', 'Pedro', 'Escamoso', '1145671234', 'ASSISTANCE', 0.0),
    (UUID_TO_BIN('c2d0f55a-59a5-4291-b936-a03f9bb7c932'), 'elpatron@gmail.com', 'Pablo', 'Escobar', '1166607770', 'ASSISTANCE', 0.0),
    (UUID_TO_BIN('07217cc1-d9ca-40a8-b466-5aa08656cd55'), 'lagrua@gmail.com', 'La Grúa', 'S.A.', '1197445548', 'ASSISTANCE', 0.0),
    (UUID_TO_BIN('4db16830-d9a7-4159-94aa-0e6f9467a3c9'), 'raul@gmail.com', 'Raúl', 'Ruíz', '1158964125', 'ASSISTANCE', 0.0),
    (UUID_TO_BIN('fc65a065-5299-4567-afb6-d3d0cec7046b'), 'megagrua@gmail.com', 'Megagrúa', 'R.R.L.', '115556879', 'ASSISTANCE', 0.0),
    (UUID_TO_BIN('acb1418d-5dee-49fe-81f3-55acb147582e'), 'roque@gmail.com', 'Roque', 'Saenz Peña', '1155543331', 'ASSISTANCE', 0.0),
    (UUID_TO_BIN('e1308c95-3b34-4d5a-9249-35921af87b64'), 'lamasrapida@gmail.com', 'La Más Rápida', 'S.A.', '1154624321', 'ASSISTANCE', 0.0),
    (UUID_TO_BIN('ee08ac6c-44b6-4dda-a3e3-8f9002a91263'), 'reysol@gmail.com', 'Luis', 'Miguel', '1155558787', 'ASSISTANCE', 0.0),
    (UUID_TO_BIN('f9651f3b-5a20-4cbe-800d-c377260c29d0'), 'standard@gmail.com', 'Ricky', 'Foot', '1144657852', 'ASSISTANCE', 0.0),
    (UUID_TO_BIN('27db7c25-d928-4d5b-b62a-073f26a98db8'), 'marvel@gmail.com', 'Stan', 'Lee', '1132167878', 'ASSISTANCE', 0.0),
    (UUID_TO_BIN('bfb3aae0-7c31-4f07-a535-dcecc9d8e8ed'), 'homero.simpson@gmail.com', 'Homero', 'Simpson', '1122223333', 'CLIENT', 0.0),
    (UUID_TO_BIN('717bf49e-8f55-4762-84f0-45bc0ef34ad0'), 'moe.szyslak@gmail.com', 'Moe', 'Szyslak', '1144445555', 'CLIENT', 0.0);

-- ASSISTANCE INSERT
INSERT INTO gav.assistance (id, user_id, kind, cost_per_km, fixed_cost, cancellation_cost)
VALUES
    (UUID_TO_BIN('f61cafec-511f-4be7-a6a8-554dced3ffab'), UUID_TO_BIN('5f700ac8-0efe-4683-9798-6595c52d1668'), 'START_UP', 85.0, 250.0, 125.0),
    (UUID_TO_BIN('f1b834e6-9e0c-42ac-b505-c6b4199796c0'), UUID_TO_BIN('c2d0f55a-59a5-4291-b936-a03f9bb7c932'), 'SMALL', 90.0, 120.0, 150.0),
    (UUID_TO_BIN('35d7c588-81c9-4adc-bc23-fe33e7acca1c'), UUID_TO_BIN('07217cc1-d9ca-40a8-b466-5aa08656cd55'), 'MEDIUM', 110.0, 350.0, 200.0),
    (UUID_TO_BIN('f345c3aa-bc8f-4422-ba8e-931966deab9e'), UUID_TO_BIN('4db16830-d9a7-4159-94aa-0e6f9467a3c9'), 'LARGE', 135.0, 395.0, 225.0),
    (UUID_TO_BIN('886bf8c6-f29d-454c-a300-55b7ed0b7ab7'), UUID_TO_BIN('fc65a065-5299-4567-afb6-d3d0cec7046b'), 'LARGE', 145.0, 410.0, 250.0),
    (UUID_TO_BIN('536b5f90-589e-4c02-96db-668327fa8e45'), UUID_TO_BIN('acb1418d-5dee-49fe-81f3-55acb147582e'), 'MEDIUM', 105.0, 325.0, 195.0),
    (UUID_TO_BIN('4644eb65-14fe-4254-bbaa-7ab5d3923fe9'), UUID_TO_BIN('e1308c95-3b34-4d5a-9249-35921af87b64'), 'BATTERY', 75.0, 225.0, 125.0),
    (UUID_TO_BIN('86f8ab60-cb2d-4504-95e1-abc4bd45cd46'), UUID_TO_BIN('ee08ac6c-44b6-4dda-a3e3-8f9002a91263'), 'LARGE', 150.0, 425.0, 250.0),
    (UUID_TO_BIN('f60006b9-9fb9-40bd-9eae-23eea8a30218'), UUID_TO_BIN('f9651f3b-5a20-4cbe-800d-c377260c29d0'), 'MEDIUM', 110.0, 415.0, 200.0),
    (UUID_TO_BIN('6a460fcf-1b00-4f75-aba0-de1b30c5f35a'), UUID_TO_BIN('27db7c25-d928-4d5b-b62a-073f26a98db8'), 'LARGE', 155.0, 430.0, 255.0);

-- ORDERS
INSERT INTO gav.assistance_order (id, status, street, between_streets, city, province, cost_per_km, fixed_cost, cancellation_cost, km_traveled, phone_number, score, assistance_id, user_id)
VALUES
    (UUID_TO_BIN('586c599f-987a-48f4-838d-4fa2e3d4b8be'), 'IN_PROGRESS', 'Evergreen 123', '1 y 2', 'Springfield', 'Springfield', 85.0, 250.0, 125.0, 0, '1122223333', 0, UUID_TO_BIN('f61cafec-511f-4be7-a6a8-554dced3ffab'), UUID_TO_BIN('bfb3aae0-7c31-4f07-a535-dcecc9d8e8ed')),
    (UUID_TO_BIN('e866b3ce-f38a-4e98-bfae-7d8e041b0856'), 'COMPLETED', 'Evergreen 123', '1 y 2', 'Springfield', 'Springfield', 90.0, 120.0, 155.0, 0, '1122223333', 5, UUID_TO_BIN('35d7c588-81c9-4adc-bc23-fe33e7acca1c'), UUID_TO_BIN('bfb3aae0-7c31-4f07-a535-dcecc9d8e8ed')),
    (UUID_TO_BIN('0864d2df-09d0-47ca-b1b1-842977e8d711'), 'CANCELLED', 'Principal 443', '5 y 6', 'Springfield', 'Springfield', 90.0, 120.0, 155.0, 0, '1144445555', 0, UUID_TO_BIN('6a460fcf-1b00-4f75-aba0-de1b30c5f35a'), UUID_TO_BIN('717bf49e-8f55-4762-84f0-45bc0ef34ad0')),
    (UUID_TO_BIN('4256006d-55d6-4aaa-872c-5d9d87bf9759'), 'COMPLETED', 'Principal 443', '5 y 6', 'Springfield', 'Springfield', 110.0, 415.0, 200.0, 0, '1144445555', 3, UUID_TO_BIN('f60006b9-9fb9-40bd-9eae-23eea8a30218'), UUID_TO_BIN('717bf49e-8f55-4762-84f0-45bc0ef34ad0'));

COMMIT;

START TRANSACTION;

-- DATA ELIMINATION
SET FOREIGN_KEY_CHECKS = 0;
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
    (UUID_TO_BIN('27db7c25-d928-4d5b-b62a-073f26a98db8'), 'marvel@gmail.com', 'Stan', 'Lee', '1132167878', 'ASSISTANCE', 0.0);

-- ASSISTANCE INSERT
INSERT INTO gav.assistance (id, cost_per_km, fixed_cost, user_id, kind)
values
    (UUID_TO_BIN('f61cafec-511f-4be7-a6a8-554dced3ffab'), 250.0, 1000.0, UUID_TO_BIN('5f700ac8-0efe-4683-9798-6595c52d1668'),'START_UP'),
    (UUID_TO_BIN('f1b834e6-9e0c-42ac-b505-c6b4199796c0'), 200.0, 1120.0, UUID_TO_BIN('c2d0f55a-59a5-4291-b936-a03f9bb7c932'),'SMALL'),
    (UUID_TO_BIN('35d7c588-81c9-4adc-bc23-fe33e7acca1c'), 290.0, 1111.0, UUID_TO_BIN('07217cc1-d9ca-40a8-b466-5aa08656cd55'),'MEDIUM'),
    (UUID_TO_BIN('f345c3aa-bc8f-4422-ba8e-931966deab9e'), 275.0, 1050.0, UUID_TO_BIN('4db16830-d9a7-4159-94aa-0e6f9467a3c9'),'LARGE'),
    (UUID_TO_BIN('886bf8c6-f29d-454c-a300-55b7ed0b7ab7'), 350.0, 950.0, UUID_TO_BIN('fc65a065-5299-4567-afb6-d3d0cec7046b'),'LARGE'),
    (UUID_TO_BIN('536b5f90-589e-4c02-96db-668327fa8e45'), 150.0, 890.0, UUID_TO_BIN('acb1418d-5dee-49fe-81f3-55acb147582e'),'MEDIUM'),
    (UUID_TO_BIN('4644eb65-14fe-4254-bbaa-7ab5d3923fe9'), 250.0, 1650.0, UUID_TO_BIN('e1308c95-3b34-4d5a-9249-35921af87b64'),'BATTERY'),
    (UUID_TO_BIN('86f8ab60-cb2d-4504-95e1-abc4bd45cd46'), 250.0, 999.0, UUID_TO_BIN('ee08ac6c-44b6-4dda-a3e3-8f9002a91263'),'LARGE'),
    (UUID_TO_BIN('f60006b9-9fb9-40bd-9eae-23eea8a30218'), 300.0, 1100.0, UUID_TO_BIN('f9651f3b-5a20-4cbe-800d-c377260c29d0'),'MEDIUM'),
    (UUID_TO_BIN('6a460fcf-1b00-4f75-aba0-de1b30c5f35a'), 450.0, 1200.0, UUID_TO_BIN('27db7c25-d928-4d5b-b62a-073f26a98db8'),'LARGE');

COMMIT;

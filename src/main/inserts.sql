-- borrado de datos
SET FOREIGN_KEY_CHECKS = 0;
truncate table gav.assistance;
truncate table gav.user;
SET FOREIGN_KEY_CHECKS = 1;
-- insert de USER
insert into gav.user (id, emailAddress, firstName, lastName, telephoneNumber, type,debts)
values (1, 'pedro@gmail.com', 'Pedro', 'Escamoso', '1145671234', 'ASSISTANCE',0.0);
insert into gav.user (id, emailAddress, firstName, lastName, telephoneNumber, type,debts)
values (2, 'elpatron@gmail.com', 'Pablo', 'Escobar', '1166607770', 'ASSISTANCE',0.0);
insert into gav.user (id, emailAddress, firstName, lastName, telephoneNumber, type,debts)
values (3, 'lagrua@gmail.com', 'La Grua', 'S.A.', '1197445548', 'ASSISTANCE',0.0);
insert into gav.user (id, emailAddress, firstName, lastName, telephoneNumber, type,debts)
values (4, 'raul@gmail.com', 'Raul', 'Ruiz', '1158964125', 'ASSISTANCE',0.0);
insert into gav.user (id, emailAddress, firstName, lastName, telephoneNumber, type,debts)
values (5, 'megagrua@gmail.com', 'Megagrua', 'R.R.L.', '115556879', 'ASSISTANCE',0.0);
insert into gav.user (id, emailAddress, firstName, lastName, telephoneNumber, type,debts)
values (6, 'roque@gmail.com', 'Roque', 'Saenz Penia', '1155543331', 'ASSISTANCE',0.0);
insert into gav.user (id, emailAddress, firstName, lastName, telephoneNumber, type,debts)
values (7, 'lamasrapida@gmail.com', 'La Mas Rapida', 'S.A.', '1154624321', 'ASSISTANCE',0.0);
insert into gav.user (id, emailAddress, firstName, lastName, telephoneNumber, type,debts)
values (8, 'reysol@gmail.com', 'Luis', 'Miguel', '1155558787', 'ASSISTANCE',0.0);
insert into gav.user (id, emailAddress, firstName, lastName, telephoneNumber, type,debts)
values (9, 'standard@gmail.com', 'Ricky', 'Foot', '1144657852', 'ASSISTANCE',0.0);
insert into gav.user (id, emailAddress, firstName, lastName, telephoneNumber, type,debts)
values (10, 'marvel@gmail.com', 'Stan', 'Lee', '1132167878', 'ASSISTANCE',0.0);
commit;
-- insert de assistance
insert into gav.assistance (id, costperkm, fixedcost, user_id, kind)
values (1, 250.0, 1000.0, 1,'START_UP');
insert into gav.assistance (id, costperkm, fixedcost, user_id, kind)
values (2, 200.0, 1120.0, 2,'SMALL');
insert into gav.assistance (id, costperkm, fixedcost, user_id, kind)
values (3, 290.0, 1111.0, 3,'MEDIUM');
insert into gav.assistance (id, costperkm, fixedcost, user_id, kind)
values (4, 275.0, 1050.0, 4,'LARGE');
insert into gav.assistance (id, costperkm, fixedcost, user_id, kind)
values (5, 350.0, 950.0, 5,'LARGE');
insert into gav.assistance (id, costperkm, fixedcost, user_id, kind)
values (6, 150.0, 890.0, 6,'MEDIUM');
insert into gav.assistance (id, costperkm, fixedcost, user_id, kind)
values (7, 250.0, 1650.0, 7,'BATTERY');
insert into gav.assistance (id, costperkm, fixedcost, user_id, kind)
values (8, 250.0, 999.0, 8,'LARGE');
insert into gav.assistance (id, costperkm, fixedcost, user_id, kind)
values (9, 300.0, 1100.0, 9,'MEDIUM');
insert into gav.assistance (id, costperkm, fixedcost, user_id, kind)
values (10, 450.0, 1200.0, 10,'LARGE');
commit;
USE proyecto;

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE relation;
TRUNCATE eetakemon;
SET FOREIGN_KEY_CHECKS = 1;

Insert INTO Eetakemon (nombre, tipo, nivel, foto) VALUES ('Bernorlax', 'Normal', 15, 'http://localhost:8081/images/bernorlax.png');
Insert INTO Eetakemon (nombre, tipo, nivel, foto) VALUES ('Davyphno', 'Normal', 15, 'http://localhost:8081/images/davyphno.png');
Insert INTO Eetakemon (nombre, tipo, nivel, foto) VALUES ('Francerpie', 'Inferior', 1, 'http://localhost:8081/images/francerpie.png');
Insert INTO Eetakemon (nombre, tipo, nivel, foto) VALUES ('Jesuskou', 'Inferior', 1, 'http://localhost:8081/images/jesuscou.png');
Insert INTO Eetakemon (nombre, tipo, nivel, foto) VALUES ('Jordinine', 'Normal', 15, 'http://localhost:8081/images/jordinine.png');
Insert INTO Eetakemon (nombre, tipo, nivel, foto) VALUES ('Lluiscarp', 'Legendario', 30, 'http://localhost:8081/images/lluiscarp.png');
Insert INTO Eetakemon (nombre, tipo, nivel, foto) VALUES ('Mewdecerio', 'Legendario', 30, 'http://localhost:8081/images/mewdecerio.png');
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema estoque_api
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `estoque_api` ;

-- -----------------------------------------------------
-- Schema estoque_api
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `estoque_api` DEFAULT CHARACTER SET utf8mb3 ;
USE `estoque_api` ;

-- -----------------------------------------------------
-- Table `estoque_api`.`itens`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `estoque_api`.`itens` (
  `id_item` BIGINT NOT NULL,
  `descricao` VARCHAR(255) NULL DEFAULT NULL,
  `estoque_seguranca` FLOAT NOT NULL,
  `familia` VARCHAR(255) NULL DEFAULT NULL,
  `grupo` VARCHAR(255) NULL DEFAULT NULL,
  `unidade` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id_item`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `estoque_api`.`estoque`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `estoque_api`.`estoque` (
  `id_estoque` BIGINT NOT NULL AUTO_INCREMENT,
  `estoque_real` FLOAT NULL DEFAULT NULL,
  `localizacao` VARCHAR(255) NULL DEFAULT NULL,
  `item_id_item` BIGINT NULL DEFAULT NULL,
  PRIMARY KEY (`id_estoque`),
  INDEX `FK7wtgxxsw28rkmrj3knahqqqp2` (`item_id_item` ASC) VISIBLE,
  CONSTRAINT `FK7wtgxxsw28rkmrj3knahqqqp2`
    FOREIGN KEY (`item_id_item`)
    REFERENCES `estoque_api`.`itens` (`id_item`))
ENGINE = InnoDB
AUTO_INCREMENT = 44
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `estoque_api`.`usuarios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `estoque_api`.`usuarios` (
  `id_usuario` INT NOT NULL,
  `email` VARCHAR(255) NULL DEFAULT NULL,
  `nome` VARCHAR(255) NULL DEFAULT NULL,
  `perfil` INT DEFAULT 1,
  `senha` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE INDEX `UK_kfsp0s1tflm1cwlj8idhqsad0` (`email` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `estoque_api`.`movimentacoes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `estoque_api`.`movimentacoes` (
  `id_movimentacao` BIGINT NOT NULL AUTO_INCREMENT,
  `data_movimentacao` DATETIME NULL DEFAULT NULL,
  `origem_destino` VARCHAR(255) NULL DEFAULT NULL,
  `quantidade` FLOAT NOT NULL,
  `tipo` VARCHAR(255) NULL DEFAULT NULL,
  `estoque_id_estoque` BIGINT NULL DEFAULT NULL,
  `item_id_item` BIGINT NULL DEFAULT NULL,
  `usuario_id_usuario` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id_movimentacao`),
  INDEX `FKmlrwe7g4qfcnhigbdtny5qyaj` (`estoque_id_estoque` ASC) VISIBLE,
  INDEX `FK67w6o5d89ppypol7qxosxx8hv` (`item_id_item` ASC) VISIBLE,
  INDEX `FKtatxoxnh5b1akevmr8p9kfvnw` (`usuario_id_usuario` ASC) VISIBLE,
  CONSTRAINT `FK67w6o5d89ppypol7qxosxx8hv`
    FOREIGN KEY (`item_id_item`)
    REFERENCES `estoque_api`.`itens` (`id_item`),
  CONSTRAINT `FKmlrwe7g4qfcnhigbdtny5qyaj`
    FOREIGN KEY (`estoque_id_estoque`)
    REFERENCES `estoque_api`.`estoque` (`id_estoque`),
  CONSTRAINT `FKtatxoxnh5b1akevmr8p9kfvnw`
    FOREIGN KEY (`usuario_id_usuario`)
    REFERENCES `estoque_api`.`usuarios` (`id_usuario`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `estoque_api`.`previsoes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `estoque_api`.`previsoes` (
  `id_previsao` INT NOT NULL AUTO_INCREMENT,
  `data_prevista` DATE NULL DEFAULT NULL,
  `finalizada` BIT(1) NOT NULL,
  `ordem` VARCHAR(255) NULL DEFAULT NULL,
  `quantidade_prevista` FLOAT NOT NULL,
  `item_id_item` BIGINT NULL DEFAULT NULL,
  `usuario_id_usuario` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id_previsao`),
  INDEX `FKnhwd4l823pnqb2mky2e12evvc` (`item_id_item` ASC) VISIBLE,
  INDEX `FKbcsscvtdalqmwch1tqchwp6g` (`usuario_id_usuario` ASC) VISIBLE,
  CONSTRAINT `FKbcsscvtdalqmwch1tqchwp6g`
    FOREIGN KEY (`usuario_id_usuario`)
    REFERENCES `estoque_api`.`usuarios` (`id_usuario`),
  CONSTRAINT `FKnhwd4l823pnqb2mky2e12evvc`
    FOREIGN KEY (`item_id_item`)
    REFERENCES `estoque_api`.`itens` (`id_item`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `estoque_api`.`reservas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `estoque_api`.`reservas` (
  `id_reserva` BIGINT NOT NULL AUTO_INCREMENT,
  `data_prevista` DATETIME(6) NULL DEFAULT NULL,
  `finalizada` BIT(1) NOT NULL,
  `ordem` VARCHAR(255) NULL DEFAULT NULL,
  `quantidade_reserva` FLOAT NOT NULL,
  `item_id_item` BIGINT NULL DEFAULT NULL,
  `usuario_id_usuario` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id_reserva`),
  INDEX `FK26fhcjq4dr40d17mrbcauwplu` (`item_id_item` ASC) VISIBLE,
  INDEX `FKchsmfq5ef8quiy1l94jt7920u` (`usuario_id_usuario` ASC) VISIBLE,
  CONSTRAINT `FK26fhcjq4dr40d17mrbcauwplu`
    FOREIGN KEY (`item_id_item`)
    REFERENCES `estoque_api`.`itens` (`id_item`),
  CONSTRAINT `FKchsmfq5ef8quiy1l94jt7920u`
    FOREIGN KEY (`usuario_id_usuario`)
    REFERENCES `estoque_api`.`usuarios` (`id_usuario`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;

USE `estoque_api` ;

-- -----------------------------------------------------
-- procedure iniciarTabelas
-- -----------------------------------------------------

/*## ESTOQUE INICIAL ANTES DAS MOVIMENTACOES

INSERT INTO `estoque` VALUES (1,161,'A123',1400),(2,88,'A124',1410),(3,171,'A125',1420),(4,121,'A126',1430),(5,81,'A127',1440),(6,96,'A128',1450)
								,(7,113,'A129',1460),(8,177,'A130',1470),(9,104,'A131',1480),(10,108,'A132',1490),(11,170,'A133',1500),(12,154,'A134',1510)
                                ,(13,125,'A135',1520),(14,100,'A136',1530),(15,165,'A137',1540),(16,163,'A138',1550),(17,81,'A139',1560),(18,155,'A140',1570)
                                ,(19,114,'A141',1580),(20,95,'A142',1590),(21,88,'A143',1600),(22,126,'A144',1610),(23,97,'A145',1620),(24,113,'A146',1630)
                                ,(25,134,'A147',1640),(26,86,'A148',1650),(27,147,'A149',1660),(28,122,'A150',1670),(29,92,'A151',1680),(30,131,'A152',1690)
                                ,(31,170,'A153',1700),(32,124,'A154',1710),(33,162,'A155',1720),(34,171,'A156',1730),(35,142,'A157',1740),(36,112,'A158',1750)
                                ,(37,145,'A159',1760),(38,171,'A160',1770),(39,137,'A161',1780),(40,110,'A162',1790),(41,105,'A163',1800),(42,92,'A164',1810),(43,108,'A165',1820);
*/

						
INSERT INTO `usuarios` VALUES (1,'joao.silva@empresa.com','Joao Silva',1,'1234'), (2,'marina.alves@empresa.com','Marina Alves',0,'1234');
                        
INSERT INTO `itens` VALUES (1400,'PARAF SEXT RI 3/16 X 1/2 BSW24 C5/16 A2,8 POLIDO',131,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1410,'PARAF SEXT RI 3/16 X 5/8 BSW24 C5/16 A2,8 POLIDO',58,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1420,'PARAF SEXT RI 3/16 X 3/4 BSW24 C5/16 A2,8 POLIDO',141,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1430,'PARAF SEXT RI 3/16 X 7/8 BSW24 C5/16 A2,8 POLIDO',91,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1440,'PARAF SEXT RI 3/16 X 1 BSW24 C5/16 A2,8 POLIDO',51,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1450,'PARAF SEXT RI 3/16 X 1-1/4 BSW24 C5/16 A2,8 POLIDO',66,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1460,'PARAF SEXT RI 3/16 X 1-1/2 BSW24 C5/16 A2,8 POLIDO',83,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1470,'PARAF SEXT RI 3/16 X 1-3/4 BSW24 C5/16 A2,8 POLIDO',147,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1480,'PARAF SEXT RI 3/16 X 2 BSW24 C5/16 A2,8 POLIDO',74,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1490,'PARAF SEXT RI 3/16 X 2-1/4 BSW24 C5/16 A2,8 POLIDO',78,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1500,'PARAF SEXT RI 3/16 X 2-1/2 BSW24 C5/16 A2,8 POLIDO',140,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1510,'PARAF SEXT RI 3/16 X 2-3/4 BSW24 C5/16 A2,8 POLIDO',124,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1520,'PARAF SEXT RI 3/16 X 3 BSW24 C5/16 A2,8 POLIDO',95,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1530,'PARAF SEXT RI 1/4 X 1/2 UNC20 C7/16 A3,8 POLIDO',70,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1540,'PARAF SEXT RI 1/4 X 5/8 UNC20 C7/16 A3,8 POLIDO',135,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1550,'PARAF SEXT RI 1/4 X 3/4 UNC20 C7/16 A3,8 POLIDO',133,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1560,'PARAF SEXT RI 1/4 X 7/8 UNC20 C7/16 A3,8 POLIDO',51,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1570,'PARAF SEXT RI 1/4 X 1 UNC20 C7/16 A3,8 POLIDO',125,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1580,'PARAF SEXT RI 1/4 X 1-1/4 UNC20 C7/16 A3,8 POLIDO',84,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1590,'PARAF SEXT RI 1/4 X 1-1/2 UNC20 C7/16 A3,8 POLIDO',65,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1600,'PARAF SEXT RI 1/4 X 1-3/4 UNC20 C7/16 A3,8 POLIDO',58,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1610,'PARAF SEXT RI 1/4 X 2 UNC20 C7/16 A3,8 POLIDO',96,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1620,'PARAF SEXT RI 1/4 X 2-1/4 UNC20 C7/16 A3,8 POLIDO',67,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1630,'PARAF SEXT RI 1/4 X 2-1/2 UNC20 C7/16 A3,8 POLIDO',83,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1640,'PARAF SEXT RI 1/4 X 2-3/4 UNC20 C7/16 A3,8 POLIDO',104,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1650,'PARAF SEXT RI 1/4 X 3 UNC20 C7/16 A3,8 POLIDO',56,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1660,'PARAF SEXT RI 1/4 X 3-1/2 UNC20 C7/16 A3,8 POLIDO',117,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1670,'PARAF SEXT RI 1/4 X 4 UNC20 C7/16 A3,8 POLIDO',92,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1680,'PARAF SEXT RI 1/4 X 4-1/2 UNC20 C7/16 A3,8 POLIDO',62,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1690,'PARAF SEXT RI 1/4 X 5 UNC20 C7/16 A3,8 POLIDO',101,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1700,'PARAF SEXT RI 1/4 X 5-1/2 UNC20 C7/16 A3,8 POLIDO',140,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1710,'PARAF SEXT RI 1/4 X 6 UNC20 C7/16 A3,8 POLIDO',94,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1720,'PARAF SEXT RI 5/16 X 1/2 UNC18 C1/2 A4,9 POLIDO',132,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1730,'PARAF SEXT RI 5/16 X 5/8 UNC18 C1/2 A4,9 POLIDO',141,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1740,'PARAF SEXT RI 5/16 X 3/4 UNC18 C1/2 A4,9 POLIDO',112,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1750,'PARAF SEXT RI 5/16 X 7/8 UNC18 C1/2 A4,9 POLIDO',82,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1760,'PARAF SEXT RI 5/16 X 1 UNC18 C1/2 A4,9 POLIDO',115,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1770,'PARAF SEXT RI 5/16 X 1-1/4 UNC18 C1/2 A4,9 POLIDO',141,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1780,'PARAF SEXT RI 5/16 X 1-1/2 UNC18 C1/2 A4,9 POLIDO',107,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1790,'PARAF SEXT RI 5/16 X 1-3/4 UNC18 C1/2 A4,9 POLIDO',80,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1800,'PARAF SEXT RI 5/16 X 2 UNC18 C1/2 A4,9 POLIDO',75,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1810,'PARAF SEXT RI 5/16 X 2-1/4 UNC18 C1/2 A4,9 POLIDO',62,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC')
,(1820,'PARAF SEXT RI 5/16 X 2-1/2 UNC18 C1/2 A4,9 POLIDO',78,'PARAFUSOS','INDUSTRIALIZAÇÃO','PC');




## MOVIMENTACOES, PREVISOES, RESERVAS E ESTOQUE GERADOS VIA ScriptGeracaoDeMovimentos
INSERT INTO `movimentacoes` VALUES (1,'2022-11-14 22:51:53','PO1010',88,'OUT',15,1540,1)
,(2,'2022-11-13 22:51:53','PO1011',52,'OUT',11,1500,1),(3,'2022-11-12 22:51:53','PO1012',75,'OUT',20,1590,1)
,(4,'2022-11-11 22:51:53','PO1013',72,'OUT',36,1750,1),(5,'2022-11-09 22:51:53','PO1015',68,'OUT',12,1510,1)
,(6,'2022-11-07 22:51:53','PO1017',71,'OUT',9,1480,1),(7,'2022-11-06 22:51:53','PO1018',53,'OUT',24,1630,1)
,(8,'2022-11-05 22:51:53','PO1019',68,'OUT',42,1810,1),(9,'2022-11-14 00:00:00','CP1010',95,'IN',31,1700,1)
,(10,'2022-11-14 00:00:00','CP1011',60,'IN',25,1640,1),(11,'2022-11-13 00:00:00','CP1012',70,'IN',12,1510,1)
,(12,'2022-11-13 00:00:00','CP1013',56,'IN',7,1460,1),(13,'2022-11-12 00:00:00','CP1014',98,'IN',29,1680,1)
,(14,'2022-11-12 00:00:00','CP1015',57,'IN',30,1690,1),(15,'2022-11-11 00:00:00','CP1016',54,'IN',13,1520,1)
,(16,'2022-11-09 00:00:00','CP1017',61,'IN',18,1570,1),(17,'2022-11-11 00:00:00','CP1018',71,'IN',13,1520,1)
,(18,'2022-11-09 00:00:00','CP1019',85,'IN',28,1670,1),(19,'2022-11-19 22:51:53','avulso',21,'OUT',23,1620,1)
,(20,'2022-11-19 22:51:54','avulso',88,'OUT',30,1690,1),(21,'2022-11-19 22:51:54','avulso',56,'OUT',35,1740,1)
,(22,'2022-11-19 22:51:54','avulso',63,'OUT',14,1530,1),(23,'2022-11-19 22:51:54','avulso',78,'OUT',38,1770,1)
,(24,'2022-11-19 22:51:54','avulso',19,'OUT',23,1620,1),(25,'2022-11-19 22:51:54','avulso',16,'OUT',17,1560,1)
,(26,'2022-11-19 22:51:54','avulso',2,'OUT',32,1710,1),(27,'2022-11-19 22:51:54','avulso',47,'OUT',23,1620,1)
,(28,'2022-11-19 22:51:54','avulso',10,'OUT',29,1680,1);

INSERT INTO `previsoes` VALUES (1,'2022-11-24',_binary '\0','CP1000',69,1600,1)
,(2,'2022-11-23',_binary '\0','CP1001',55,1750,1)
,(3,'2022-11-22',_binary '\0','CP1002',66,1610,1)
,(4,'2022-11-21',_binary '\0','CP1003',80,1500,1)
,(5,'2022-11-20',_binary '\0','CP1004',75,1800,1)
,(6,'2022-11-19',_binary '\0','CP1005',88,1480,1)
,(7,'2022-11-18',_binary '\0','CP1006',64,1510,1)
,(8,'2022-11-17',_binary '\0','CP1007',76,1730,1)
,(9,'2022-11-16',_binary '\0','CP1008',91,1540,1)
,(10,'2022-11-15',_binary '\0','CP1009',70,1670,1)
,(11,'2022-11-14',_binary '','CP1010',95,1700,1)
,(12,'2022-11-13',_binary '','CP1011',60,1640,1)
,(13,'2022-11-12',_binary '','CP1012',70,1510,1)
,(14,'2022-11-11',_binary '','CP1013',56,1460,1)
,(15,'2022-11-10',_binary '','CP1014',98,1680,1)
,(16,'2022-11-09',_binary '','CP1015',57,1690,1)
,(17,'2022-11-08',_binary '','CP1016',54,1520,1)
,(18,'2022-11-07',_binary '','CP1017',61,1570,1)
,(19,'2022-11-06',_binary '','CP1018',71,1520,1)
,(20,'2022-11-05',_binary '','CP1019',85,1670,1);

INSERT INTO `reservas` VALUES (1,'2022-11-24 22:51:53.000000',_binary '\0','PO1000',61,1780,1)
,(2,'2022-11-23 22:51:53.000000',_binary '\0','PO1001',97,1510,1)
,(3,'2022-11-22 22:51:53.000000',_binary '\0','PO1002',84,1470,1)
,(4,'2022-11-21 22:51:53.000000',_binary '\0','PO1003',95,1620,1)
,(5,'2022-11-20 22:51:53.000000',_binary '\0','PO1004',67,1710,1)
,(6,'2022-11-19 22:51:53.000000',_binary '\0','PO1005',60,1430,1)
,(7,'2022-11-18 22:51:53.000000',_binary '\0','PO1006',57,1540,1)
,(8,'2022-11-17 22:51:53.000000',_binary '\0','PO1007',96,1430,1)
,(9,'2022-11-16 22:51:53.000000',_binary '\0','PO1008',52,1610,1)
,(10,'2022-11-15 22:51:53.000000',_binary '\0','PO1009',63,1620,1)
,(11,'2022-11-14 22:51:53.000000',_binary '','PO1010',88,1540,1)
,(12,'2022-11-13 22:51:53.000000',_binary '','PO1011',52,1500,1)
,(13,'2022-11-12 22:51:53.000000',_binary '','PO1012',75,1590,1)
,(14,'2022-11-11 22:51:53.000000',_binary '','PO1013',72,1750,1)
,(15,'2022-11-10 22:51:53.000000',_binary '','PO1014',95,1410,1)
,(16,'2022-11-09 22:51:53.000000',_binary '','PO1015',68,1510,1)
,(17,'2022-11-08 22:51:53.000000',_binary '','PO1016',91,1560,1)
,(18,'2022-11-07 22:51:53.000000',_binary '','PO1017',71,1480,1)
,(19,'2022-11-06 22:51:53.000000',_binary '','PO1018',53,1630,1)
,(20,'2022-11-05 22:51:53.000000',_binary '','PO1019',68,1810,1);


INSERT INTO `estoque` VALUES (1,161,'A123',1400)
,(2,88,'A124',1410),(3,171,'A125',1420),(4,121,'A126',1430),(5,81,'A127',1440),(6,96,'A128',1450),(7,169,'A129',1460)
,(8,177,'A130',1470),(9,33,'A131',1480),(10,108,'A132',1490),(11,118,'A133',1500),(12,156,'A134',1510),(13,250,'A135',1520)
,(14,37,'A136',1530),(15,77,'A137',1540),(16,163,'A138',1550),(17,65,'A139',1560),(18,216,'A140',1570),(19,114,'A141',1580)
,(20,20,'A142',1590),(21,88,'A143',1600),(22,126,'A144',1610),(23,10,'A145',1620),(24,60,'A146',1630),(25,194,'A147',1640)
,(26,86,'A148',1650),(27,147,'A149',1660),(28,207,'A150',1670),(29,180,'A151',1680),(30,100,'A152',1690),(31,265,'A153',1700)
,(32,122,'A154',1710),(33,162,'A155',1720),(34,171,'A156',1730),(35,86,'A157',1740),(36,40,'A158',1750),(37,145,'A159',1760)
,(38,93,'A160',1770),(39,137,'A161',1780),(40,110,'A162',1790),(41,105,'A163',1800),(42,24,'A164',1810),(43,108,'A165',1820);


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

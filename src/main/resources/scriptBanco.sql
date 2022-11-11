-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema estoque_api
-- -----------------------------------------------------

DROP SCHEMA IF EXISTS `estoque_api`;

-- -----------------------------------------------------
-- Schema estoque_api
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `estoque_api` DEFAULT CHARACTER SET utf8 ;
USE `estoque_api` ;

-- -----------------------------------------------------
-- Table `estoque_api`.`itens`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `estoque_api`.`itens` (
  `idItem` INT NOT NULL,
  `descricao` VARCHAR(100) NULL,
  `grupo` VARCHAR(45) NULL COMMENT 'Industrialização, UsoConsumo',
  `familia` VARCHAR(45) NULL,
  `unidade` VARCHAR(45) NULL,
  `estoqueSeguranca` FLOAT NULL,
  PRIMARY KEY (`idItem`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `estoque_api`.`estoque`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `estoque_api`.`estoque` (
  `idEstoque` INT NOT NULL,
	`itens_idItem` INT NOT NULL,
  `localização` VARCHAR(45) NULL,
  `estoque_real` FLOAT NULL,

  PRIMARY KEY (`idEstoque`),
  INDEX `fk_estoque_itens1_idx` (`itens_idItem` ASC) VISIBLE,
  CONSTRAINT `fk_estoque_itens1`
    FOREIGN KEY (`itens_idItem`)
    REFERENCES `estoque_api`.`itens` (`idItem`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `estoque_api`.`usuarios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `estoque_api`.`usuarios` (
  `idUsuario` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(45) NULL,
  `senha` VARCHAR(45) NULL,
  PRIMARY KEY (`idUsuario`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `estoque_api`.`movimentacoes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `estoque_api`.`movimentacoes` (
  `idmovimentações` INT NOT NULL AUTO_INCREMENT,
  `estoque_idEstoque` INT NOT NULL,
  `dataMovimentacao` DATETIME NULL,
  `in_out` VARCHAR(45) NULL,
  `tipo_movimentacao` VARCHAR(45) NULL,
  `quantidade` FLOAT NULL,
  `movimentaçõescol` VARCHAR(45) NULL,
  `usuarios_idUsuario` INT NOT NULL,
  `itens_idItem` INT NOT NULL,
  PRIMARY KEY (`idmovimentações`),
  INDEX `fk_movimentações_estoque1_idx` (`estoque_idEstoque` ASC) VISIBLE,
  INDEX `fk_movimentacoes_usuarios1_idx` (`usuarios_idUsuario` ASC) VISIBLE,
  INDEX `fk_movimentacoes_itens1_idx` (`itens_idItem` ASC) VISIBLE,
  CONSTRAINT `fk_movimentações_estoque1`
    FOREIGN KEY (`estoque_idEstoque`)
    REFERENCES `estoque_api`.`estoque` (`idEstoque`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_movimentacoes_usuarios1`
    FOREIGN KEY (`usuarios_idUsuario`)
    REFERENCES `estoque_api`.`usuarios` (`idUsuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_movimentacoes_itens1`
    FOREIGN KEY (`itens_idItem`)
    REFERENCES `estoque_api`.`itens` (`idItem`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `estoque_api`.`reservas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `estoque_api`.`reservas` (
  `idReserva` INT NOT NULL AUTO_INCREMENT,
  `finalizada` TINYINT NULL DEFAULT 0,
  `quantidadeReserva` FLOAT NULL,
  `dataPrevista` DATETIME NULL,
  `ordem` VARCHAR(45) NULL,
  `usuarios_idUsuario` INT NOT NULL,
  `itens_idItem` INT NOT NULL,
  PRIMARY KEY (`idReserva`),
  INDEX `fk_reservas_usuarios1_idx` (`usuarios_idUsuario` ASC) VISIBLE,
  INDEX `fk_reservas_itens1_idx` (`itens_idItem` ASC) VISIBLE,
  CONSTRAINT `fk_reservas_usuarios1`
    FOREIGN KEY (`usuarios_idUsuario`)
    REFERENCES `estoque_api`.`usuarios` (`idUsuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_reservas_itens1`
    FOREIGN KEY (`itens_idItem`)
    REFERENCES `estoque_api`.`itens` (`idItem`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `estoque_api`.`previsoes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `estoque_api`.`previsoes` (
  `idPrevisao` INT NOT NULL AUTO_INCREMENT,
  `dataPrevista` DATETIME NULL,
  `quantidadePrevista` FLOAT NULL,
  `ordem` VARCHAR(45) NULL,
  `finalizada` TINYINT NULL DEFAULT 0,
  `usuarios_idUsuario` INT NOT NULL,
  `itens_idItem` INT NOT NULL,
  PRIMARY KEY (`idPrevisao`),
  INDEX `fk_previsoes_usuarios1_idx` (`usuarios_idUsuario` ASC) VISIBLE,
  INDEX `fk_previsoes_itens1_idx` (`itens_idItem` ASC) VISIBLE,
  CONSTRAINT `fk_previsoes_usuarios1`
    FOREIGN KEY (`usuarios_idUsuario`)
    REFERENCES `estoque_api`.`usuarios` (`idUsuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_previsoes_itens1`
    FOREIGN KEY (`itens_idItem`)
    REFERENCES `estoque_api`.`itens` (`idItem`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

INSERT INTO `itens` VALUES (1400,'PARAF SEXT RI 3/16 X 1/2 BSW24 C5/16 A2,8 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',131)
							,(1410,'PARAF SEXT RI 3/16 X 5/8 BSW24 C5/16 A2,8 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',58)
                            ,(1420,'PARAF SEXT RI 3/16 X 3/4 BSW24 C5/16 A2,8 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',141)
                            ,(1430,'PARAF SEXT RI 3/16 X 7/8 BSW24 C5/16 A2,8 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',91)
                            ,(1440,'PARAF SEXT RI 3/16 X 1 BSW24 C5/16 A2,8 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',51)
                            ,(1450,'PARAF SEXT RI 3/16 X 1-1/4 BSW24 C5/16 A2,8 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',66)
                            ,(1460,'PARAF SEXT RI 3/16 X 1-1/2 BSW24 C5/16 A2,8 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',83)
                            ,(1470,'PARAF SEXT RI 3/16 X 1-3/4 BSW24 C5/16 A2,8 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',147)
                            ,(1480,'PARAF SEXT RI 3/16 X 2 BSW24 C5/16 A2,8 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',74)
                            ,(1490,'PARAF SEXT RI 3/16 X 2-1/4 BSW24 C5/16 A2,8 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',78)
                            ,(1500,'PARAF SEXT RI 3/16 X 2-1/2 BSW24 C5/16 A2,8 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',140)
                            ,(1510,'PARAF SEXT RI 3/16 X 2-3/4 BSW24 C5/16 A2,8 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',124)
                            ,(1520,'PARAF SEXT RI 3/16 X 3 BSW24 C5/16 A2,8 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',95)
                            ,(1530,'PARAF SEXT RI 1/4 X 1/2 UNC20 C7/16 A3,8 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',70)
                            ,(1540,'PARAF SEXT RI 1/4 X 5/8 UNC20 C7/16 A3,8 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',135)
                            ,(1550,'PARAF SEXT RI 1/4 X 3/4 UNC20 C7/16 A3,8 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',133)
                            ,(1560,'PARAF SEXT RI 1/4 X 7/8 UNC20 C7/16 A3,8 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',51)
                            ,(1570,'PARAF SEXT RI 1/4 X 1 UNC20 C7/16 A3,8 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',125)
                            ,(1580,'PARAF SEXT RI 1/4 X 1-1/4 UNC20 C7/16 A3,8 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',84)
                            ,(1590,'PARAF SEXT RI 1/4 X 1-1/2 UNC20 C7/16 A3,8 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',65)
                            ,(1600,'PARAF SEXT RI 1/4 X 1-3/4 UNC20 C7/16 A3,8 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',58)
                            ,(1610,'PARAF SEXT RI 1/4 X 2 UNC20 C7/16 A3,8 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',96)
                            ,(1620,'PARAF SEXT RI 1/4 X 2-1/4 UNC20 C7/16 A3,8 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',67)
                            ,(1630,'PARAF SEXT RI 1/4 X 2-1/2 UNC20 C7/16 A3,8 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',83)
                            ,(1640,'PARAF SEXT RI 1/4 X 2-3/4 UNC20 C7/16 A3,8 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',104)
                            ,(1650,'PARAF SEXT RI 1/4 X 3 UNC20 C7/16 A3,8 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',56)
                            ,(1660,'PARAF SEXT RI 1/4 X 3-1/2 UNC20 C7/16 A3,8 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',117)
                            ,(1670,'PARAF SEXT RI 1/4 X 4 UNC20 C7/16 A3,8 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',92)
                            ,(1680,'PARAF SEXT RI 1/4 X 4-1/2 UNC20 C7/16 A3,8 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',62)
                            ,(1690,'PARAF SEXT RI 1/4 X 5 UNC20 C7/16 A3,8 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',101)
                            ,(1700,'PARAF SEXT RI 1/4 X 5-1/2 UNC20 C7/16 A3,8 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',140)
                            ,(1710,'PARAF SEXT RI 1/4 X 6 UNC20 C7/16 A3,8 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',94)
                            ,(1720,'PARAF SEXT RI 5/16 X 1/2 UNC18 C1/2 A4,9 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',132)
                            ,(1730,'PARAF SEXT RI 5/16 X 5/8 UNC18 C1/2 A4,9 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',141)
                            ,(1740,'PARAF SEXT RI 5/16 X 3/4 UNC18 C1/2 A4,9 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',112)
                            ,(1750,'PARAF SEXT RI 5/16 X 7/8 UNC18 C1/2 A4,9 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',82)
                            ,(1760,'PARAF SEXT RI 5/16 X 1 UNC18 C1/2 A4,9 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',115)
                            ,(1770,'PARAF SEXT RI 5/16 X 1-1/4 UNC18 C1/2 A4,9 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',141)
                            ,(1780,'PARAF SEXT RI 5/16 X 1-1/2 UNC18 C1/2 A4,9 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',107)
                            ,(1790,'PARAF SEXT RI 5/16 X 1-3/4 UNC18 C1/2 A4,9 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',80)
                            ,(1800,'PARAF SEXT RI 5/16 X 2 UNC18 C1/2 A4,9 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',75)
                            ,(1810,'PARAF SEXT RI 5/16 X 2-1/4 UNC18 C1/2 A4,9 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',62)
                            ,(1820,'PARAF SEXT RI 5/16 X 2-1/2 UNC18 C1/2 A4,9 POLIDO','INDUSTRIALIZAÇÃO','PARAFUSOS','PC',78);
                            
                            
INSERT INTO `estoque` VALUES (1,1400,'A123',161),(2,1410,'A124',88),(3,1420,'A125',171),(4,1430,'A126',121),(5,1440,'A127',81),(6,1450,'A128',96)
							,(7,1460,'A129',113),(8,1470,'A130',177),(9,1480,'A131',104),(10,1490,'A132',108),(11,1500,'A133',170),(12,1510,'A134',154)
                            ,(13,1520,'A135',125),(14,1530,'A136',100),(15,1540,'A137',165),(16,1550,'A138',163),(17,1560,'A139',81),(18,1570,'A140',155)
                            ,(19,1580,'A141',114),(20,1590,'A142',95),(21,1600,'A143',88),(22,1610,'A144',126),(23,1620,'A145',97),(24,1630,'A146',113)
                            ,(25,1640,'A147',134),(26,1650,'A148',86),(27,1660,'A149',147),(28,1670,'A150',122),(29,1680,'A151',92),(30,1690,'A152',131)
                            ,(31,1700,'A153',170),(32,1710,'A154',124),(33,1720,'A155',162),(34,1730,'A156',171),(35,1740,'A157',142),(36,1750,'A158',112)
                            ,(37,1760,'A159',145),(38,1770,'A160',171),(39,1780,'A161',137),(40,1790,'A162',110),(41,1800,'A163',105),(42,1810,'A164',92),(43,1820,'A165',108);

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

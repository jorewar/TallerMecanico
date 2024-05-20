DROP SCHEMA IF EXISTS tallerMecanico;

CREATE SCHEMA IF NOT EXISTS tallerMecanico DEFAULT CHARACTER SET utf8 COLLATE utf8_spanish_ci;
USE tallerMecanico;

DROP TABLE IF EXISTS clientes;
CREATE TABLE IF NOT EXISTS clientes (
  nombre VARCHAR(45) NOT NULL,
  dni VARCHAR(9) NOT NULL,
  telefono VARCHAR(9) NULL,
  PRIMARY KEY (dni),
  UNIQUE INDEX `dni_idx` (dni ASC) VISIBLE
);

DROP TABLE IF EXISTS vehiculos;
CREATE TABLE IF NOT EXISTS vehiculos (
  marca VARCHAR(45) NOT NULL,
  modelo VARCHAR(45) NOT NULL,
  matricula VARCHAR(7) NOT NULL,
  PRIMARY KEY (matricula),
  UNIQUE INDEX `matricula_idx` (matricula ASC) VISIBLE
);

DROP TABLE IF EXISTS trabajos;
CREATE TABLE IF NOT EXISTS `trabajos` (
  `cliente` VARCHAR(9) NOT NULL,
  `vehiculo` VARCHAR(7) NOT NULL,
  `fechaInicio` DATE NOT NULL,
  `fechaFin` DATE,
  `tipo` ENUM("Revisión", "Mecánico") NOT NULL,
  `horas` INT,
  `precioMaterial` FLOAT,
  INDEX `fk_clientes_idx` (`cliente` ASC) VISIBLE,
  INDEX `fk_vehiculos_idx` (`vehiculo` ASC) VISIBLE,
  INDEX `fechaInicio_idx` (`fechaInicio` ASC) VISIBLE,
  PRIMARY KEY (`cliente`, `vehiculo`, `fechaInicio`),
  CONSTRAINT `fk_clientes`
    FOREIGN KEY (`cliente`)
    REFERENCES `clientes` (`dni`),
  CONSTRAINT `fk_vehiculos`
    FOREIGN KEY (`vehiculo`)
    REFERENCES `vehiculos` (`matricula`)
 );

INSERT INTO tallerMecanico.clientes (nombre, dni, telefono) VALUES('Patricio Estrella', '11111111H', '950111111');
INSERT INTO tallerMecanico.clientes (nombre, dni, telefono) VALUES('Bob Esponja', '11223344B', '950112233');

INSERT INTO tallerMecanico.vehiculos (marca, modelo, matricula) VALUES('Seat', 'León', '1111BBB');
INSERT INTO tallerMecanico.vehiculos (marca, modelo, matricula) VALUES('Scania', 'Citywide', '1234BCD');
INSERT INTO tallerMecanico.vehiculos (marca, modelo, matricula) VALUES('Renault', 'Megane', '2222CCC');
INSERT INTO tallerMecanico.vehiculos (marca, modelo, matricula) VALUES('Mercedes-Benz', 'eSprinter', '3333DDD');

INSERT INTO tallerMecanico.trabajos (cliente, vehiculo, fechaInicio, fechaFin, tipo, horas, precioMaterial) VALUES('11111111H', '1111BBB', '2024-03-10', '2024-03-14', 'Revisión', NULL, NULL);
INSERT INTO tallerMecanico.trabajos (cliente, vehiculo, fechaInicio, fechaFin, tipo, horas, precioMaterial) VALUES('11111111H', '2222CCC', '2024-03-15', NULL, 'Mecánico', NULL, 125.0);
INSERT INTO tallerMecanico.trabajos (cliente, vehiculo, fechaInicio, fechaFin, tipo, horas, precioMaterial) VALUES('11223344B', '1234BCD', '2024-03-10', '2024-03-16', 'Mecánico', 5, NULL);
INSERT INTO tallerMecanico.trabajos (cliente, vehiculo, fechaInicio, fechaFin, tipo, horas, precioMaterial) VALUES('11223344B', '3333DDD', '2024-03-01', '2024-03-07', 'Revisión', 10, NULL);

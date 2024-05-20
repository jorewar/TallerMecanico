package org.iesalandalus.programacion.tallermecanico.modelo.negocio.mariadb;

import org.mariadb.jdbc.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;


public class MariaDB {

    private static final String HOST = "localhost";
    private static final String ESQUEMA = "tallerMecanico";
    private static final String USUARIO = "taller";
    private static final String CONTRASENA = "taller";

    private static Connection conexion = null;

    private MariaDB() {
        //Evitamos que se creen instancias
    }

    public static Connection getConexion() {
        if (conexion == null) {
            try {
                String urlConexion = String.format("jdbc:mariadb://%s/%s?user=%s&password=%s", HOST, ESQUEMA, USUARIO, CONTRASENA);
                conexion = (Connection) DriverManager.getConnection(urlConexion);
                System.out.println("Conexión a MariaDB realizada correctamente.");
            } catch (SQLException e) {
                System.out.printf("ERROR de conexión a MariaDB:  %s%n", e);
                System.exit(-1);
            }
        }
        return conexion;
    }

    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
                conexion = null;
                System.out.println("Conexión a MariaDB cerrada correctamente.");
            } catch (SQLException e) {
                System.out.printf("ERROR de MariaDB: %s%n", e);
            }
        }
    }
}

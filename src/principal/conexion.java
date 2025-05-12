/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *Clase encargada de establecer la conexión con la base de datos Oracle.
 * @author NeyBg
 */
public class conexion {
    private static final String URL = "jdbc:oracle:thin:@192.168.254.215:1521:orcl"; // Cambia a tu puerto y SID
    private static final String USER = "pruebados"; // Cambia según tu usuario Oracle
    private static final String PASSWORD = "pruebados"; // Cambia según tu clave
    
   /**
     * Establece y retorna una conexión con la base de datos Oracle.
     */
    public static Connection getConnection() {
        Connection conn = null;

        try {
            // Cargar el driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Intentar la conexión
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a Oracle");

        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se encontró el driver de Oracle JDBC.");
            e.printStackTrace();

        } catch (SQLException e) {
            System.err.println("Error al establecer la conexión con Oracle.");
            e.printStackTrace();
        }

        return conn;
    }
}
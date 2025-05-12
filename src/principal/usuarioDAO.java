/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal;
//importamos libreria
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import principal.Usuario;
import javax.swing.JOptionPane;
import principal.conexion;

public class usuarioDAO {
    private String url = "jdbc:oracle:thin:@192.168.254.215:1521:orcl"; 
    private String usuario = "usuario";
    private String contrasena = "contrasena";
    private Connection conexion;
    
    // Constructor por defecto
    public usuarioDAO() {
    }

    
    // Constructor con parámetros
    public usuarioDAO(String url, String usuario, String contrasena) {
        this.url = url;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    // Método para conectar
public void conectar() {
    try {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        System.out.println("=== Intentando conexión ===");
        System.out.println("URL: " + url);
        System.out.println("Usuario: " + usuario);
        System.out.println("Contraseña: " + contrasena.replaceAll(".", "*")); // Oculta contraseña real
        
        this.conexion = DriverManager.getConnection(url, usuario, contrasena);
        System.out.println("¡CONEXIÓN EXITOSA!");
    } catch (ClassNotFoundException e) {
        System.err.println("Error: Driver Oracle no encontrado. ¿Tienes el jar JDBC?");
    } catch (SQLException e) {
        System.err.println("Error SQL Detallado:");
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, 
            "Error de conexión:\n" + e.getMessage(), 
            "Error Oracle", JOptionPane.ERROR_MESSAGE);
    }
}

    // Método para cerrar conexión
    public void cerrarConexion() {
        try {
            if (this.conexion != null && !this.conexion.isClosed()) {
                this.conexion.close();
                System.out.println("Conexión cerrada.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   
    public Usuario autenticar(String correo, String contraseña) {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            if(this.conexion == null || this.conexion.isClosed()) {
                this.conectar(); // Asegura que hay conexión
            }

            String sql = "SELECT NOMBRE, CORREO, ROL FROM USUARIO WHERE CORREO = ? AND CONTRASENA = ?";
            stmt = this.conexion.prepareStatement(sql);
            stmt.setString(1, correo);
            stmt.setString(2, contraseña);

            rs = stmt.executeQuery();

            if (rs.next()) {
                return new Usuario(
                    rs.getString("NOMBRE"),
                    "", // apellido (si lo necesitas)
                    rs.getString("CORREO"),
                    "", // contraseña (no es necesario devolverla)
                    rs.getString("ROL")
                );
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
}
    // Getters y Setters
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     *
     * @return
     */
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContraseña() {
        return contrasena;
    }

    public void setContraseña(String contraseña) {
        this.contrasena = contraseña;
    }

    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }
}

package principal;

// Importamos las librerías necesarias para la conexión a la base de datos y manejo de errores
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import principal.Usuario;
import javax.swing.JOptionPane;

public class usuarioDAO {
    // Dirección para conectarse a la base de datos Oracle local
    private String url = "jdbc:oracle:thin:@localhost:1521:XE";
    // Usuario y contraseña para la base de datos
    private String usuario = "prueba2";
    private String contrasena = "prueba2";

    // Variable para mantener la conexión activa
    private Connection conexion;

    // Constructor vacío por si se necesita crear el objeto sin parámetros
    public usuarioDAO() {
    }

    // Constructor para crear el objeto con datos de conexión personalizados
    public usuarioDAO(String url, String usuario, String contrasena) {
        this.url = url;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    // Este método abre la conexión con la base de datos Oracle
    public void conectar() {
        try {
            // Cargamos el driver de Oracle para que Java sepa cómo conectarse
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Imprime datos de conexión en consola (contraseña oculta)
            System.out.println("=== Intentando conexión ===");
            System.out.println("URL: " + url);
            System.out.println("Usuario: " + usuario);
            System.out.println("Contraseña: " + contrasena.replaceAll(".", "*"));

            // Aquí se realiza la conexión real
            this.conexion = DriverManager.getConnection(url, usuario, contrasena);
            System.out.println("¡CONEXIÓN EXITOSA!");
        } catch (ClassNotFoundException e) {
            // Esto sale si falta el driver de Oracle en el proyecto
            System.err.println("Error: Driver Oracle no encontrado. ¿Tienes el jar JDBC?");
        } catch (SQLException e) {
            // Aquí muestra detalles si la conexión falla por otro motivo
            System.err.println("Error SQL Detallado:");
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                "Error de conexión:\n" + e.getMessage(),
                "Error Oracle", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Este método cierra la conexión si está abierta, para liberar recursos
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

    // Este método busca un usuario en la base que coincida con correo y contraseña para login
    public Usuario autenticar(String correo, String contraseña) {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Si no hay conexión activa, la abre
            if (this.conexion == null || this.conexion.isClosed()) {
                this.conectar();
            }

            // Consulta SQL con parámetros para evitar inyección SQL
            String sql = "SELECT ID_USUARIO, NOMBRE, APELLIDO, CORREO, ROL FROM USUARIO WHERE CORREO = ? AND CONTRASENA = ?";
            stmt = this.conexion.prepareStatement(sql);
            stmt.setString(1, correo);
            stmt.setString(2, contraseña);

            rs = stmt.executeQuery();

            // Si encontró un usuario, devuelve un objeto Usuario con sus datos
            if (rs.next()) {
                System.out.println("Usuario encontrado: " + rs.getString("NOMBRE") + " " + rs.getString("APELLIDO"));
                return new Usuario(
                    rs.getInt("ID_USUARIO"),
                    rs.getString("NOMBRE"),
                    rs.getString("APELLIDO"),
                    rs.getString("CORREO"),
                    "", // No devolvemos la contraseña por seguridad
                    rs.getString("ROL")
                );
            }
            // Si no encontró, devuelve null
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            // Siempre cerramos ResultSet y PreparedStatement para no dejar abiertos
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para solicitar cambio de contraseña (marca la solicitud y guarda la nueva)
    public boolean solicitarCambioClave(int idUsuario, String nuevaContrasena) {
        PreparedStatement stmt = null;
        try {
            if (this.conexion == null || this.conexion.isClosed()) {
                this.conectar();
            }

            String sql = "UPDATE usuario SET solicitud_cambio_contra = 1, nueva_contrasena = ? WHERE id_usuario = ?";
            stmt = this.conexion.prepareStatement(sql);
            stmt.setString(1, nuevaContrasena);
            stmt.setInt(2, idUsuario);

            int filas = stmt.executeUpdate();
            return filas > 0;  // True si actualizó por lo menos una fila
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para crear un usuario nuevo en la base de datos
    public boolean crearUsuario(Usuario usuario) {
        PreparedStatement stmt = null;
        try {
            if (this.conexion == null || this.conexion.isClosed()) {
                this.conectar();
            }
            String sql = "INSERT INTO usuario (id_usuario, nombre, apellido, correo, rol, contrasena) VALUES (usuario_seq.NEXTVAL, ?, ?, ?, ?, ?)";
            stmt = this.conexion.prepareStatement(sql);
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getApellido());
            stmt.setString(3, usuario.getCorreo());
            stmt.setString(4, usuario.getRol());
            stmt.setString(5, usuario.getContrasena());
            int filas = stmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para actualizar datos de un usuario existente
    public boolean actualizarUsuario(Usuario usuario) {
        PreparedStatement stmt = null;
        try {
            if (this.conexion == null || this.conexion.isClosed()) {
                this.conectar();
            }
            String sql = "UPDATE usuario SET nombre = ?, apellido = ?, correo = ?, contrasena = ?, rol = ? WHERE id_usuario = ?";
            stmt = this.conexion.prepareStatement(sql);
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getApellido());
            stmt.setString(3, usuario.getCorreo());
            stmt.setString(4, usuario.getContrasena());
            stmt.setString(5, usuario.getRol());
            stmt.setInt(6, usuario.getId());

            int filas = stmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para eliminar un usuario de la base de datos según su ID
    public boolean eliminarUsuario(int idUsuario) {
        try {
            conectar();  // Abrimos conexión por si no está abierta
            String sql = "DELETE FROM USUARIO WHERE ID_USUARIO = ?";
            try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                ps.setInt(1, idUsuario);
                int filasAfectadas = ps.executeUpdate();
                return filasAfectadas > 0; // True si borró al menos un registro
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para cambiar el estado del usuario (por ejemplo, habilitado o deshabilitado)
    public boolean actualizarEstadoUsuario(int idUsuario, int nuevoEstado) {
        PreparedStatement stmt = null;
        try {
            if (this.conexion == null || this.conexion.isClosed()) {
                this.conectar();
            }
            String sql = "UPDATE USUARIO SET ESTADO = ? WHERE ID_USUARIO = ?";
            stmt = this.conexion.prepareStatement(sql);
            stmt.setInt(1, nuevoEstado);
            stmt.setInt(2, idUsuario);

            int filas = stmt.executeUpdate();
            return filas > 0;  // True si actualizó al menos una fila
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void insertarRetroalimentacion(Retroalimentacion r) throws SQLException {
        String sql = "INSERT INTO retroalimentacion (ID_ENCUESTA, FECHA, TIPO, COMENTARIO, CALIFICACION, ID_USUARIO, REFERENCIA_ID) " +
                     "VALUES (retro_seq.NEXTVAL, ?, ?, ?, ?, ?, ?)";
        if (this.conexion == null || this.conexion.isClosed()){
            this.conectar();
            
        }
        try (PreparedStatement ps = this.conexion.prepareStatement(sql)) {
         
            ps.setDate(1, java.sql.Date.valueOf(r.getFecha()));
            ps.setString(2, r.getTipo());
            ps.setString(3, r.getComentario());
            ps.setInt(4, r.getCalificacion());
            ps.setInt(5, r.getUsuarioId());
            ps.setInt(6, r.getReferenciaId());

            ps.executeUpdate();
        }
    }


    // Métodos para obtener y cambiar los datos de conexión (no tan usados, pero para configurar)
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

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





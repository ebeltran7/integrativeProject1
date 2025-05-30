package principal;

// Importamos las librerías necesarias para la conexión a la base de datos y manejo de errores
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import principal.Usuario;
import javax.swing.JOptionPane;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;



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

    public List<Usuario> obtenerTodosUsuarios() {
    List<Usuario> usuarios = new ArrayList<>();
    String sql = "SELECT ID_USUARIO, NOMBRE, CORREO, ROL, ESTADO FROM USUARIO ORDER BY ID_USUARIO";

    try {
        if (getConexion() == null || getConexion().isClosed()) {
            conectar();
        }
        PreparedStatement ps = getConexion().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Usuario usuario = new Usuario();
            usuario.setId(rs.getInt("ID_USUARIO"));
            usuario.setNombre(rs.getString("NOMBRE"));
            usuario.setCorreo(rs.getString("CORREO"));
            usuario.setRol(rs.getString("ROL"));
            usuario.setEstado(rs.getInt("ESTADO"));
            usuarios.add(usuario);
        }

        rs.close();
        ps.close();

    } catch (Exception e) {
        e.printStackTrace();
    }
    return usuarios;
}

    
    // Este método abre la conexión con la base de datos Oracle
    public void conectar() {
        try {
            // Verifica si la conexión ya está abierta, si está cerrada o no existe, entonces la abre.
            if (this.conexion != null && !this.conexion.isClosed()) {
                return;  // Si ya está abierta, no hacemos nada
            }

            // Cargamos el driver de Oracle para que Java sepa cómo conectarse
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Establece la conexión real con la base de datos
            this.conexion = DriverManager.getConnection(url, usuario, contrasena);
            System.out.println("¡CONEXIÓN EXITOSA!");

        } catch (ClassNotFoundException e) {
            // Si no se encuentra el driver de Oracle, muestra un mensaje de error
            System.err.println("Error: Driver Oracle no encontrado. ¿Tienes el jar JDBC?");
        } catch (SQLException e) {
            // Si ocurre un error de SQL, muestra un mensaje detallado
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


    
    public int insertarSolicitud(int idUsuario, String tipoSolicitud, java.util.Date fechaSolicitud, String estado) throws SQLException {
        String sql = "INSERT INTO solicitud (id_solicitud, fecha_solicitud, estado, tipo, id_usuario) "
                   + "VALUES (solicitud_seq.NEXTVAL, ?, ?, ?, ?)";

        String[] generatedColumns = {"ID_SOLICITUD"};
        int idGenerado = -1;

        if (this.conexion == null || this.conexion.isClosed()) {
            this.conectar();
        }

        try (PreparedStatement ps = this.conexion.prepareStatement(sql, generatedColumns)) {
            java.sql.Date sqlDate = new java.sql.Date(fechaSolicitud.getTime());
            ps.setDate(1, sqlDate);
            ps.setString(2, estado);
            ps.setString(3, tipoSolicitud);
            ps.setInt(4, idUsuario);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    idGenerado = rs.getInt(1);
                }
            }
        }
        return idGenerado;
    }
   
    public int insertarSolicitudConEquipos(int idUsuario, String tipoSolicitud, java.util.Date fechaSolicitud,
                                       String estado, List<Integer> listaIdEquipos) throws SQLException {
        int idSolicitud = -1;
        // Obtener la conexión activa usando getConexion()
        usuarioDAO dao = new usuarioDAO();
        Connection conexion = dao.getConexion(); // Aquí obtenemos la conexión activa
        if (this.conexion == null || this.conexion.isClosed()) {
            this.conectar();
        }

        try {
            // Iniciar transacción
            conexion.setAutoCommit(false);

            // Insertar solicitud y obtener id generado
            String sqlSolicitud = "INSERT INTO solicitud (id_solicitud, fecha_solicitud, estado, tipo, id_usuario) "
                    + "VALUES (solicitud_seq.NEXTVAL, ?, ?, ?, ?)";

            String[] generatedColumns = {"ID_SOLICITUD"};
            try (PreparedStatement psSolicitud = conexion.prepareStatement(sqlSolicitud, generatedColumns)) {
                java.sql.Date sqlDate = new java.sql.Date(fechaSolicitud.getTime());
                psSolicitud.setDate(1, sqlDate);
                psSolicitud.setString(2, estado);
                psSolicitud.setString(3, tipoSolicitud);
                psSolicitud.setInt(4, idUsuario);
                psSolicitud.executeUpdate();

                try (ResultSet rs = psSolicitud.getGeneratedKeys()) {
                    if (rs.next()) {
                        idSolicitud = rs.getInt(1);
                    } else {
                        throw new SQLException("No se pudo obtener el ID de la solicitud.");
                    }
                }
            }

            // Insertar cada equipo asociado
            String sqlEquipo = "INSERT INTO solicitud_equipo (id_solicitud_equipo, id_solicitud, id_equipo, cantidad) "
                    + "VALUES (solicitud_equipo_seq.NEXTVAL, ?, ?, 1)";

            try (PreparedStatement psEquipo = conexion.prepareStatement(sqlEquipo)) {
                for (Integer idEquipo : listaIdEquipos) {
                    psEquipo.setInt(1, idSolicitud);
                    psEquipo.setInt(2, idEquipo);
                    psEquipo.addBatch();
                }
                psEquipo.executeBatch();
            }

            // Confirmar transacción
            conexion.commit();

        } catch (SQLException ex) {
            // Si ocurre error, hacer rollback y mostrar error
            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            throw ex;
        } finally {
            if (conexion != null) {
                try {
                    conexion.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return idSolicitud;
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
        if (conexion == null){
            conectar();
        }
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }
}





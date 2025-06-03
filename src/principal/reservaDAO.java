package principal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date; // usamos solo java.util.Date aquí

public class reservaDAO {
    private Connection conn;

    public reservaDAO() {
        conn = conexion.getConnection();
    }

    // Verifica que la solicitud exista y pertenezca al usuario dado
    public boolean existeSolicitudParaUsuario(int idSolicitud, int idUsuario) {
        String sql = "SELECT COUNT(*) FROM SOLICITUD WHERE ID_SOLICITUD = ? AND ID_USUARIO = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSolicitud);
            ps.setInt(2, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Valida que la sala esté disponible en la fecha y rango horario
    public boolean validarDisponibilidad(int idSala, Date fecha, Date horaInicio, Date horaFin) {
        String sql = "SELECT COUNT(*) FROM RESERVA WHERE ID_SALA = ? AND FECHA = ? " +
                     "AND (HORA_INICIO < ? AND HORA_FIN > ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSala);
            ps.setDate(2, new java.sql.Date(fecha.getTime())); // se aclara que es java.sql.Date
            ps.setTimestamp(3, new Timestamp(horaFin.getTime()));
            ps.setTimestamp(4, new Timestamp(horaInicio.getTime()));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;  // true si no hay conflicto
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // en caso de error
    }

    // Valida que el usuario no tenga otra reserva en cualquier sala en el mismo horario y fecha
    public boolean validarDisponibilidadUsuario(int idUsuario, Date fecha, Date horaInicio, Date horaFin) {
        String sql = "SELECT COUNT(*) FROM RESERVA r JOIN SOLICITUD s ON r.ID_SOLICITUD = s.ID_SOLICITUD " +
                     "WHERE s.ID_USUARIO = ? AND r.FECHA = ? AND (r.HORA_INICIO < ? AND r.HORA_FIN > ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setDate(2, new java.sql.Date(fecha.getTime()));
            ps.setTimestamp(3, new Timestamp(horaFin.getTime()));
            ps.setTimestamp(4, new Timestamp(horaInicio.getTime()));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0; // true si no hay superposición
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // en caso de error
    }

    // Guarda la reserva con todas las validaciones
    public boolean guardarReserva(int idSala, int idSolicitud, int idUsuario, Date fecha, Date horaInicio, Date horaFin) {
        if (!existeSolicitudParaUsuario(idSolicitud, idUsuario)) {
            System.err.println("Error: La solicitud con ID " + idSolicitud + " no existe o no pertenece al usuario con ID " + idUsuario);
            return false;
        }

        if (!validarDisponibilidad(idSala, fecha, horaInicio, horaFin)) {
            System.err.println("Error: La sala " + idSala + " no está disponible en ese horario.");
            return false;
        }

        if (!validarDisponibilidadUsuario(idUsuario, fecha, horaInicio, horaFin)) {
            System.err.println("Error: El usuario " + idUsuario + " ya tiene una reserva en ese horario.");
            return false;
        }

        String sql = "INSERT INTO RESERVA (ID_RESERVA, ID_SALA, ID_SOLICITUD, FECHA, HORA_INICIO, HORA_FIN) " +
                     "VALUES (reserva_seq.NEXTVAL, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSala);
            ps.setInt(2, idSolicitud);
            ps.setDate(3, new java.sql.Date(fecha.getTime()));
            ps.setTimestamp(4, new Timestamp(horaInicio.getTime()));
            ps.setTimestamp(5, new Timestamp(horaFin.getTime()));

            int filas = ps.executeUpdate();
            if (filas > 0) {
                System.out.println("Reserva guardada exitosamente.");
                return true;
            } else {
                System.err.println("No se pudo guardar la reserva.");
            }
        } catch (Exception e) {
            System.err.println("Error al guardar reserva: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}


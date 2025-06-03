package principal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Calendar;


public class dlgReservaSala extends JDialog {

    private int idSala;
    private String nombreSala;
    private int idUsuario;

    private JComboBox<Integer> comboIdSolicitud;
    private JSpinner spinnerFecha;
    private JSpinner spinnerHoraInicio;
    private JSpinner spinnerHoraFin;
    private JButton btnGuardar;

    public dlgReservaSala(Frame parent, int idSala, String nombreSala, int idUsuario) {
        super(parent, "Reservar Sala: " + nombreSala, true);
        this.idSala = idSala;
        this.nombreSala = nombreSala;
        this.idUsuario = idUsuario;

        initComponents();
        cargarSolicitudesUsuario();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setSize(400, 300);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Etiquetas y componentes
        panel.add(new JLabel("Sala:"));
        panel.add(new JLabel(nombreSala));

        panel.add(new JLabel("Selecciona solicitud:"));
        comboIdSolicitud = new JComboBox<>();
        panel.add(comboIdSolicitud);

        panel.add(new JLabel("Fecha:"));
        spinnerFecha = new JSpinner(new SpinnerDateModel());
        spinnerFecha.setEditor(new JSpinner.DateEditor(spinnerFecha, "yyyy-MM-dd"));
        panel.add(spinnerFecha);

        panel.add(new JLabel("Hora Inicio:"));
        spinnerHoraInicio = new JSpinner(new SpinnerDateModel());
        spinnerHoraInicio.setEditor(new JSpinner.DateEditor(spinnerHoraInicio, "HH:mm"));
        panel.add(spinnerHoraInicio);

        panel.add(new JLabel("Hora Fin:"));
        spinnerHoraFin = new JSpinner(new SpinnerDateModel());
        spinnerHoraFin.setEditor(new JSpinner.DateEditor(spinnerHoraFin, "HH:mm"));
        panel.add(spinnerHoraFin);

        add(panel, BorderLayout.CENTER);

        btnGuardar = new JButton("Guardar Reserva");
        add(btnGuardar, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> guardarReserva());
    }

    private void cargarSolicitudesUsuario() {
        List<Integer> solicitudes = obtenerSolicitudesPorUsuario(idUsuario);
        if (solicitudes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tienes solicitudes disponibles para reservar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            btnGuardar.setEnabled(false);
        } else {
            for (Integer id : solicitudes) {
                comboIdSolicitud.addItem(id);
            }
        }
    }

    private List<Integer> obtenerSolicitudesPorUsuario(int idUsuario) {
        List<Integer> lista = new ArrayList<>();
        String sql = "SELECT ID_SOLICITUD FROM SOLICITUD WHERE ID_USUARIO = ?";
        try (Connection conn = conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(rs.getInt("ID_SOLICITUD"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar solicitudes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return lista;
    }

    private void guardarReserva() {
        try {
            int idSolicitud = (Integer) comboIdSolicitud.getSelectedItem();

            Date fecha = (Date) spinnerFecha.getValue();

            // Para hora inicio y fin, combinamos la fecha con la hora seleccionada:
            Date horaInicio = getDateTimeFromSpinner(spinnerFecha, spinnerHoraInicio);
            Date horaFin = getDateTimeFromSpinner(spinnerFecha, spinnerHoraFin);

            if (horaFin.before(horaInicio) || horaFin.equals(horaInicio)) {
                JOptionPane.showMessageDialog(this, "La hora fin debe ser después de la hora inicio.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            reservaDAO dao = new reservaDAO();
            boolean ok = dao.guardarReserva(idSala, idSolicitud, idUsuario, fecha, horaInicio, horaFin);

            if (ok) {
                JOptionPane.showMessageDialog(this, "Reserva guardada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();  // cerrar diálogo
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo guardar la reserva.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Combina la fecha del spinnerFecha y la hora del spinnerHora en un solo Date
    private Date getDateTimeFromSpinner(JSpinner spinnerFecha, JSpinner spinnerHora) {
        Date fecha = (Date) spinnerFecha.getValue();
        Date hora = (Date) spinnerHora.getValue();

        Calendar calFecha = Calendar.getInstance();
        calFecha.setTime(fecha);

        Calendar calHora = Calendar.getInstance();
        calHora.setTime(hora);

        calFecha.set(Calendar.HOUR_OF_DAY, calHora.get(Calendar.HOUR_OF_DAY));
        calFecha.set(Calendar.MINUTE, calHora.get(Calendar.MINUTE));
        calFecha.set(Calendar.SECOND, 0);
        calFecha.set(Calendar.MILLISECOND, 0);

        return calFecha.getTime();
    }
}
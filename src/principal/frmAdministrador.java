/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package principal;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import javax.swing.table.DefaultTableModel;
import principal.frmCrearUsuario;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import java.util.Date;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.text.SimpleDateFormat;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author NeyBg
 */
public class frmAdministrador extends javax.swing.JFrame {
    DefaultTableModel modeloUsuarios;
    private Usuario usuarioLogueado;
    private int idReservaSeleccionada = -1;



    /**
     * Creates new form frmAdministrador
     * 
     */
    public frmAdministrador(Usuario usuario) {
        
        this.usuarioLogueado = usuario;
        initComponents();
        cargarSolicitudesReserva();  // Nueva función para cargar reservasar
        inicializarModeloTabla();
        cargarUsuarios();
        cargarSolicitudesPendientes();
        cargarReservasPendientesParaGestionar();
        cargarReservasSala();
        inicializarListenerTablaGestionarReservas();
        this.setLocationRelativeTo(null);


        // Aquí puedes agregar código adicional para inicializar con el usuario
    
        JTable jTableReservaSala = new JTable();
        JScrollPane jScrollBar1 = new JScrollPane(jTableReservaSala);
   
        


        this.setLocationRelativeTo(null);  // Centrar ventana
        // NUEVO: cargar solicitudes y configurar listener para selección en tabla solicitudes
        cargarSolicitudesPendientes();
        inicializarListenerTablaSolicitudes();
        // Agregar listeners para botones aprobar/rechazar
        btnAprobar.addActionListener(evt -> btnAprobarActionPerformed(evt));
        btnRechazar.addActionListener(evt -> btnRechazarActionPerformed(evt));
        
        botonDeshabilitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonDeshabilitarActionPerformed(evt);
            }
        });
        
        botonHabilitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonHabilitarActionPerformed(evt);
            }
        });
        
        botonEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarActionPerformed(evt);
            }
        });

    }
    
    
    // Método para cargar solicitudes de reserva de salas
    private void cargarSolicitudesReserva() {
        String[] columnas = {"ID Solicitud", "Usuario", "Tipo", "Fecha Solicitud", "Estado"};
        DefaultTableModel modeloSolicitudes = new DefaultTableModel(null, columnas);
        jTableGestionarReservas.setModel(modeloSolicitudes);

        String sql = "SELECT s.ID_SOLICITUD, u.NOMBRE, s.TIPO, s.FECHA_SOLICITUD, s.ESTADO " +
                     "FROM SOLICITUD s " +
                     "JOIN USUARIO u ON s.ID_USUARIO = u.ID_USUARIO " +
                     "WHERE s.ESTADO = 'PENDIENTE' AND s.TIPO LIKE 'RESERVA_%'";

        try {
            usuarioDAO dao = new usuarioDAO();
            PreparedStatement ps = dao.getConexion().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            SimpleDateFormat fechaFormat = new SimpleDateFormat("dd/MM/yyyy");

            while (rs.next()) {
                Object[] fila = new Object[5];
                fila[0] = rs.getInt("ID_SOLICITUD");
                fila[1] = rs.getString("NOMBRE");
                fila[2] = rs.getString("TIPO");

                Date fecha = rs.getDate("FECHA_SOLICITUD");
                fila[3] = (fecha != null) ? fechaFormat.format(fecha) : "";

                fila[4] = rs.getString("ESTADO");
                modeloSolicitudes.addRow(fila);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar solicitudes: " + e.getMessage());
        }
    }
    public void cambiarEstadoReserva(int idReserva, String nuevoEstado) {
        String sql = "UPDATE RESERVA SET ESTADO = ? WHERE ID_RESERVA = ?";
        try {
            usuarioDAO dao = new usuarioDAO();
            if (dao.getConexion() == null || dao.getConexion().isClosed()) {
                dao.conectar();
            }
            PreparedStatement ps = dao.getConexion().prepareStatement(sql);
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idReserva);
            ps.executeUpdate();
            ps.close();
            JOptionPane.showMessageDialog(this, "Estado actualizado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cambiar estado de reserva: " + e.getMessage());
        }
    }

    public void cargarReservasSala() {
        System.out.println("Ejecutando cargarReservasSala...");
        String[] columnas = {"ID", "Nombre Sala", "Profesor", "Fecha", "Hora Inicio", "Hora Fin", "Estado"};
        DefaultTableModel modeloSala = new DefaultTableModel(null, columnas);
        tableReservaSala.setModel(modeloSala);

        String sql = "SELECT r.ID_RESERVA, " +
                     "s.NOMBRE AS SALA, " +
                     "u.NOMBRE || ' ' || u.APELLIDO AS PROFESOR, " +
                     "r.FECHA AS FECHA, " +
                     "r.HORA_INICIO, " +
                     "r.HORA_FIN, " +
                     "so.ESTADO " +
                     "FROM RESERVA r " +
                     "JOIN SALA s ON r.ID_SALA = s.ID_SALA " +
                     "JOIN SOLICITUD so ON r.ID_SOLICITUD = so.ID_SOLICITUD " +
                     "JOIN USUARIO u ON so.ID_USUARIO = u.ID_USUARIO " +
                     "WHERE UPPER(so.ESTADO) = 'PENDIENTE' " +
                     "ORDER BY r.FECHA, r.HORA_INICIO";

        try {
            usuarioDAO dao = new usuarioDAO();
            if (dao.getConexion() == null || dao.getConexion().isClosed()) {
                dao.conectar();
            }
            PreparedStatement ps = dao.getConexion().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");

            while (rs.next()) {
                Object[] fila = new Object[7];
                fila[0] = rs.getInt("ID_RESERVA");
                fila[1] = rs.getString("SALA");
                fila[2] = rs.getString("PROFESOR");

                Timestamp fecha = rs.getTimestamp("FECHA");
                fila[3] = (fecha != null) ? formatoFecha.format(fecha) : "";

                Timestamp horaInicio = rs.getTimestamp("HORA_INICIO");
                fila[4] = (horaInicio != null) ? formatoHora.format(horaInicio) : "";

                Timestamp horaFin = rs.getTimestamp("HORA_FIN");
                fila[5] = (horaFin != null) ? formatoHora.format(horaFin) : "";

                fila[6] = rs.getString("ESTADO");

                modeloSala.addRow(fila);
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar reservas de salas: " + e.getMessage());
        }
    }

    
    private void inicializarListenerTablaGestionarReservas() {
        jTableGestionarReservas.getSelectionModel().addListSelectionListener(event -> {
            int filaSeleccionada = jTableGestionarReservas.getSelectedRow();
            if (filaSeleccionada >= 0) {
                idReservaSeleccionada = (int) jTableGestionarReservas.getValueAt(filaSeleccionada, 0);
                String profesor = jTableGestionarReservas.getValueAt(filaSeleccionada, 2).toString();
                String sala = jTableGestionarReservas.getValueAt(filaSeleccionada, 1).toString();
                String fecha = jTableGestionarReservas.getValueAt(filaSeleccionada, 3).toString();

                // Mostrar un resumen en el txtDetalleSolicitud
                txtDetalleSolicitud.setText("Reserva de " + profesor + " en la sala " + sala + " el día " + fecha + ".");
            }
        });
    }

    private void actualizarEstadoReservaSala(int idReserva, String nuevoEstado, String mensaje) {
        String sql = "UPDATE RESERVAS_SALA SET ESTADO = ? WHERE ID_RESERVA = ?";
        try {
            usuarioDAO dao = new usuarioDAO();
            PreparedStatement ps = dao.getConexion().prepareStatement(sql);
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idReserva);
            int filas = ps.executeUpdate();
            ps.close();

            if (filas > 0) {
                txtDetalleSolicitud.setText("Reserva " + nuevoEstado + ": " + mensaje);
                cargarReservasPendientesParaGestionar();  // Recargar tabla
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró la reserva.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al actualizar reserva: " + e.getMessage());
        }
    }

    
        
    private void inicializarModeloTabla() {
        String[] columnas = {"ID Usuario", "Nombre", "Correo", "Rol", "Estado" };
        modeloUsuarios = new DefaultTableModel(null, columnas);
        tblUsuarios.setModel(modeloUsuarios);
    }
    
 /**
 * Carga los usuarios desde la base de datos y los muestra en la tabla.
 */
    private void cargarUsuarios() {
        usuarioDAO dao = new usuarioDAO();
        modeloUsuarios.setRowCount(0); // limpia la tabla

        try {
            if (dao.getConexion() == null || dao.getConexion().isClosed()) {
                dao.conectar();
            }
            // Consulta con la columna ESTADO
            String sql = "SELECT ID_USUARIO, NOMBRE, CORREO, ROL, ESTADO FROM USUARIO ORDER BY ID_USUARIO";
;

            java.sql.PreparedStatement ps = dao.getConexion().prepareStatement(sql);
            java.sql.ResultSet rs = ps.executeQuery();
            //El array tiene tamaño 4, llenas las posiciones 0 a 3.
            while (rs.next()) {
                Object[] fila = new Object[5];
                fila[0] = rs.getInt("ID_USUARIO");
                fila[1] = rs.getString("NOMBRE");
                fila[2] = rs.getString("CORREO");
                fila[3] = rs.getString("ROL");
                fila[4] = rs.getInt("ESTADO") == 1 ? "Habilitado" : "Deshabilitado";
                modeloUsuarios.addRow(fila);
            }
            rs.close();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Error al cargar usuarios: " + e.getMessage());
        }
    }
    
    
    private Usuario obtenerUsuarioSeleccionado() {
        int fila = tblUsuarios.getSelectedRow();
        if (fila == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Seleccione un usuario para modificar.", "Advertencia", javax.swing.JOptionPane.WARNING_MESSAGE);
            return null;
        }

        Usuario usuario = new Usuario();
        usuario.setId((Integer) modeloUsuarios.getValueAt(fila, 0));
        usuario.setNombre((String) modeloUsuarios.getValueAt(fila, 1));
        usuario.setCorreo((String) modeloUsuarios.getValueAt(fila, 2));
        usuario.setRol((String) modeloUsuarios.getValueAt(fila, 3));
  

    return usuario;
}

    private void cargarReservasPendientesParaGestionar() {
        String[] columnas = {"ID Reserva", "Sala", "Profesor", "Fecha", "Hora Inicio", "Hora Fin", "Estado"};
        DefaultTableModel modeloGestion = new DefaultTableModel(null, columnas);
        jTableGestionarReservas.setModel(modeloGestion);

        String sql = "SELECT r.ID_RESERVA, " +
                     "s.NOMBRE AS SALA, " +
                     "u.NOMBRE || ' ' || u.APELLIDO AS PROFESOR, " +
                     "r.FECHA AS FECHA_RESERVA, " +
                     "r.HORA_INICIO, " +
                     "r.HORA_FIN, " +
                     "so.ESTADO " +
                     "FROM RESERVA r " +
                     "JOIN SALA s ON r.ID_SALA = s.ID_SALA " +
                     "JOIN SOLICITUD so ON r.ID_SOLICITUD = so.ID_SOLICITUD " +
                     "JOIN USUARIO u ON so.ID_USUARIO = u.ID_USUARIO " +
                     "WHERE UPPER(so.ESTADO) = 'PENDIENTE' " +
                     "ORDER BY r.FECHA, r.HORA_INICIO";

        try {
            usuarioDAO dao = new usuarioDAO();
            PreparedStatement ps = dao.getConexion().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm");

            while (rs.next()) {
                Object[] fila = new Object[7];
                fila[0] = rs.getInt("ID_RESERVA");
                fila[1] = rs.getString("SALA");
                fila[2] = rs.getString("PROFESOR");

                // FECHA_RESERVA es Date
                java.sql.Date fechaReserva = rs.getDate("FECHA_RESERVA");
                fila[3] = (fechaReserva != null) ? formatoFecha.format(fechaReserva) : "";

                // HORA_INICIO y HORA_FIN son Time o Timestamp, se formatean solo la hora
                java.sql.Time horaInicio = rs.getTime("HORA_INICIO");
                fila[4] = (horaInicio != null) ? formatoHora.format(horaInicio) : "";

                java.sql.Time horaFin = rs.getTime("HORA_FIN");
                fila[5] = (horaFin != null) ? formatoHora.format(horaFin) : "";

                fila[6] = rs.getString("ESTADO");

                modeloGestion.addRow(fila);
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar reservas: " + e.getMessage());
        }
    }





    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        botonCrear1 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUsuarios = new javax.swing.JTable();
        botonCrear = new javax.swing.JButton();
        botonEliminar = new javax.swing.JButton();
        botonDeshabilitar = new javax.swing.JButton();
        botonHabilitar = new javax.swing.JButton();
        botonModificar = new javax.swing.JButton();
        panelSolicitudes = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableGestionarReservas = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtDetalleSolicitud = new javax.swing.JTextArea();
        btnSalir = new javax.swing.JButton();
        btnAprobar = new javax.swing.JButton();
        btnRechazar = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        botonCrearSala = new javax.swing.JButton();
        botonCrearEquipo = new javax.swing.JButton();
        botonModificarReservaSala = new javax.swing.JButton();
        botonModificarReservaEquipo = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableReservaEquipo = new javax.swing.JTable();
        jScrollPane7 = new javax.swing.JScrollPane();
        jScrollPane5 = new javax.swing.JScrollPane();
        tableReservaSala = new javax.swing.JTable();

        botonCrear1.setBackground(new java.awt.Color(255, 153, 51));
        botonCrear1.setForeground(new java.awt.Color(0, 102, 153));
        botonCrear1.setText("Crear");
        botonCrear1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane1.setBackground(new java.awt.Color(0, 102, 153));
        jTabbedPane1.setForeground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(0, 102, 153));

        tblUsuarios.setBackground(new java.awt.Color(0, 102, 153));
        tblUsuarios.setForeground(new java.awt.Color(255, 255, 255));
        tblUsuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID Usuario", "Nombre", "Apellido", "Correo", "Rol", "Estado"
            }
        ));
        jScrollPane1.setViewportView(tblUsuarios);

        botonCrear.setBackground(new java.awt.Color(255, 153, 51));
        botonCrear.setForeground(new java.awt.Color(0, 102, 153));
        botonCrear.setText("Crear");
        botonCrear.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        botonCrear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCrearActionPerformed(evt);
            }
        });

        botonEliminar.setBackground(new java.awt.Color(255, 153, 51));
        botonEliminar.setForeground(new java.awt.Color(0, 102, 153));
        botonEliminar.setText("Eliminar");
        botonEliminar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        botonEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonEliminarActionPerformed(evt);
            }
        });

        botonDeshabilitar.setBackground(new java.awt.Color(255, 153, 51));
        botonDeshabilitar.setForeground(new java.awt.Color(0, 102, 153));
        botonDeshabilitar.setText("Deshabilitar");
        botonDeshabilitar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        botonDeshabilitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonDeshabilitarActionPerformed(evt);
            }
        });

        botonHabilitar.setBackground(new java.awt.Color(255, 153, 51));
        botonHabilitar.setForeground(new java.awt.Color(0, 102, 153));
        botonHabilitar.setText("Habilitar");
        botonHabilitar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        botonHabilitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonHabilitarActionPerformed(evt);
            }
        });

        botonModificar.setBackground(new java.awt.Color(255, 153, 51));
        botonModificar.setForeground(new java.awt.Color(0, 102, 153));
        botonModificar.setText("Modificar");
        botonModificar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        botonModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonModificarActionPerformed(evt);
            }
        });

        panelSolicitudes.setBackground(new java.awt.Color(0, 102, 153));
        panelSolicitudes.setLayout(null);

        jTableGestionarReservas.setBackground(new java.awt.Color(0, 102, 153));
        jTableGestionarReservas.setForeground(new java.awt.Color(255, 255, 255));
        jTableGestionarReservas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Usuario", "Fecha Solicitud", "Tipo", "Estado"
            }
        ));
        jScrollPane2.setViewportView(jTableGestionarReservas);

        panelSolicitudes.add(jScrollPane2);
        jScrollPane2.setBounds(0, 0, 510, 160);

        txtDetalleSolicitud.setBackground(new java.awt.Color(0, 102, 153));
        txtDetalleSolicitud.setColumns(20);
        txtDetalleSolicitud.setForeground(new java.awt.Color(255, 255, 255));
        txtDetalleSolicitud.setRows(5);
        jScrollPane3.setViewportView(txtDetalleSolicitud);

        panelSolicitudes.add(jScrollPane3);
        jScrollPane3.setBounds(520, 10, 238, 130);

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        btnAprobar.setBackground(new java.awt.Color(255, 153, 0));
        btnAprobar.setForeground(new java.awt.Color(0, 102, 153));
        btnAprobar.setText("Aceptar");
        btnAprobar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnAprobar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAprobarActionPerformed(evt);
            }
        });

        btnRechazar.setBackground(new java.awt.Color(255, 153, 0));
        btnRechazar.setForeground(new java.awt.Color(0, 102, 153));
        btnRechazar.setText("Rechazar");
        btnRechazar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnRechazar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRechazarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(botonCrear, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(botonEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(botonDeshabilitar, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(botonHabilitar, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(botonModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 159, Short.MAX_VALUE))
                    .addComponent(panelSolicitudes, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnAprobar, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(35, 35, 35)
                                .addComponent(btnRechazar, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnSalir)))
                        .addGap(13, 13, 13)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(botonCrear, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(botonModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(botonHabilitar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17)
                        .addComponent(botonDeshabilitar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(botonEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelSolicitudes, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAprobar)
                    .addComponent(btnRechazar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                .addComponent(btnSalir)
                .addGap(43, 43, 43))
        );

        jTabbedPane1.addTab("Gestion de Usuarios", jPanel1);

        jPanel2.setBackground(new java.awt.Color(0, 102, 153));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Reserva Equipos");

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Reserva Salas");

        botonCrearSala.setText("Crear Sala");
        botonCrearSala.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCrearSalaActionPerformed(evt);
            }
        });

        botonCrearEquipo.setText("Crear Equipo");
        botonCrearEquipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonCrearEquipoActionPerformed(evt);
            }
        });

        botonModificarReservaSala.setText("Modificar");
        botonModificarReservaSala.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonModificarReservaSalaActionPerformed(evt);
            }
        });

        botonModificarReservaEquipo.setText("Modificar ");
        botonModificarReservaEquipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonModificarReservaEquipoActionPerformed(evt);
            }
        });

        tableReservaEquipo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Nombre ", "Usuario", "Fecha inicial", "Fecha Final"
            }
        ));
        jScrollPane4.setViewportView(tableReservaEquipo);

        jScrollPane6.setViewportView(jScrollPane4);

        tableReservaSala.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID_Reserva", "ID_Sala", "Nomre", "ID_Usuario", "Fecha Inicial", "fecha Final"
            }
        ));
        jScrollPane5.setViewportView(tableReservaSala);

        jScrollPane7.setViewportView(jScrollPane5);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 471, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(14, 14, 14)
                                .addComponent(botonModificarReservaEquipo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(botonCrearEquipo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 471, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(botonCrearSala)
                                    .addComponent(botonModificarReservaSala)))
                            .addComponent(jLabel1))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(botonCrearSala)
                        .addGap(18, 18, 18)
                        .addComponent(botonModificarReservaSala)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(botonCrearEquipo)
                                .addGap(18, 18, 18)
                                .addComponent(botonModificarReservaEquipo)
                                .addGap(101, 101, 101))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(79, Short.MAX_VALUE))))))
        );

        jTabbedPane1.addTab("Gestion de Reservas", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 595, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonModificarActionPerformed
        // TODO add your handling code here:

        Usuario usuarioSeleccionado = obtenerUsuarioSeleccionado();
        if (usuarioSeleccionado == null) {
            return; // No hay selección, salir
        }

        frmCrearUsuario dialog = new frmCrearUsuario(this, true);
        dialog.cargarUsuario(usuarioSeleccionado);  // Método que debes crear en frmCrearUsuario
        dialog.setModoEdicion(true);                 // Método para activar modo edición
        dialog.setVisible(true);

        if (dialog.isGuardado()) {
            cargarUsuarios();  // Recarga la tabla para mostrar cambios
        }

    }//GEN-LAST:event_botonModificarActionPerformed

    
    
    // Evento para deshabilitar usuario seleccionado
    private void botonHabilitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonHabilitarActionPerformed
        Usuario usuarioSeleccionado = obtenerUsuarioSeleccionado();
        if (usuarioSeleccionado == null) {
            return; // No hay usuario seleccionado
        }

        usuarioDAO dao = new usuarioDAO();
        boolean exito = dao.actualizarEstadoUsuario(usuarioSeleccionado.getId(), 1); // 1 = habilitado

        if (exito) {
            JOptionPane.showMessageDialog(this, "Usuario habilitado correctamente.");
            cargarUsuarios();  // Refresca tabla
        } else {
            JOptionPane.showMessageDialog(this, "Error al habilitar usuario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_botonHabilitarActionPerformed

    private void botonDeshabilitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonDeshabilitarActionPerformed
        Usuario usuarioSeleccionado = obtenerUsuarioSeleccionado();
        if (usuarioSeleccionado == null) {
            return; // No hay usuario seleccionado
        }

        usuarioDAO dao = new usuarioDAO();
        boolean exito = dao.actualizarEstadoUsuario(usuarioSeleccionado.getId(), 0); // 0 = deshabilitado

        if (exito) {
            JOptionPane.showMessageDialog(this, "Usuario deshabilitado correctamente.");
            cargarUsuarios();  // Refresca tabla
        } else {
            JOptionPane.showMessageDialog(this, "Error al deshabilitar usuario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_botonDeshabilitarActionPerformed

    private void botonEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonEliminarActionPerformed
        // agregamo metodo para eliminar
        Usuario usuarioSeleccionado = obtenerUsuarioSeleccionado();
        if (usuarioSeleccionado == null) {
            return; // No hay usuario seleccionado
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea eliminar al usuario " + usuarioSeleccionado.getNombre() + "?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            usuarioDAO dao = new usuarioDAO();
            boolean exito = dao.eliminarUsuario(usuarioSeleccionado.getId());

            if (exito) {
                JOptionPane.showMessageDialog(this, "Usuario eliminado correctamente.");
                cargarUsuarios(); // refresca tabla
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }//GEN-LAST:event_botonEliminarActionPerformed

    private void botonCrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCrearActionPerformed
        // TODO add your handling code here:
        frmCrearUsuario dialog = new frmCrearUsuario(this, true);
        dialog.setVisible(true);
        if (dialog.isGuardado()) {
            cargarUsuarios(); // Refresca la tabla con usuarios actualizados
        }

    }//GEN-LAST:event_botonCrearActionPerformed

    private void btnAprobarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAprobarActionPerformed
        if (idReservaSeleccionada > 0) {
            actualizarEstadoReservaSala(idReservaSeleccionada, "APROBADA", "Reserva aprobada con éxito.");
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una reserva primero.");
        }
    }//GEN-LAST:event_btnAprobarActionPerformed
    
    private void btnRechazarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRechazarActionPerformed
        // TODO add your handling code here:
        if (idReservaSeleccionada > 0) {
            String motivo = JOptionPane.showInputDialog(this, "¿Por qué se rechaza la reserva?");
            if (motivo != null && !motivo.trim().isEmpty()) {
                actualizarEstadoReservaSala(idReservaSeleccionada, "RECHAZADA", motivo);
            } else {
                JOptionPane.showMessageDialog(this, "Debe ingresar un motivo para rechazar.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecciona una reserva primero.");
        }
    }//GEN-LAST:event_btnRechazarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();              // Cierra el formulario actual
        new frmlogin().setVisible(true); // Abre el formulario de login

    }//GEN-LAST:event_btnSalirActionPerformed

    private void botonCrearSalaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCrearSalaActionPerformed
        // Asumiendo que tenemos la variable usuarioLogueado
        String rolActual = usuarioLogueado.getRol();
        // Esto te abrirá o generará automáticamente el método manejador del evento
        frmCrearReservaSala dialog = new frmCrearReservaSala(this, true, rolActual);
        dialog.setLocationRelativeTo(this); // Centra el formulario en relación al padre
        dialog.setVisible(true); // Muestra el formulario modal
    }//GEN-LAST:event_botonCrearSalaActionPerformed
 
    private void botonCrearEquipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCrearEquipoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botonCrearEquipoActionPerformed

    private void botonModificarReservaSalaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonModificarReservaSalaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botonModificarReservaSalaActionPerformed

    private void botonModificarReservaEquipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonModificarReservaEquipoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_botonModificarReservaEquipoActionPerformed

    private void cambiarEstadoSolicitud(int idSolicitud, String estado) {
    try {
            usuarioDAO dao = new usuarioDAO();
            String sql = "UPDATE SOLICITUD SET ESTADO = ? WHERE ID_SOLICITUD  = ?)";

            PreparedStatement ps = dao.getConexion().prepareStatement(sql);
            ps.setString(1, estado);
            ps.setInt(2, idSolicitud);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Solicitud " + estado.toLowerCase());
            cargarSolicitudesReserva();  // Refrescar tabla
            txtDetalleSolicitud.setText("");  // Limpiar detalles

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + e.getMessage());
        } 
    }
    
    private void cargarSolicitudesPendientes() {
        String sql = "SELECT s.id_solicitud, u.nombre || ' ' || u.apellido AS usuario, s.fecha_solicitud, s.tipo, s.estado " +
                     "FROM solicitud s JOIN usuario u ON s.id_usuario = u.id_usuario " +
                     "WHERE s.estado = 'PENDIENTE'";

        try {
            usuarioDAO dao = new usuarioDAO();
            if (dao.getConexion() == null || dao.getConexion().isClosed()) {
                dao.conectar();
            }
            java.sql.PreparedStatement ps = dao.getConexion().prepareStatement(sql);
            java.sql.ResultSet rs = ps.executeQuery();

            DefaultTableModel model = (DefaultTableModel) jTableGestionarReservas.getModel();
            model.setRowCount(0); // Limpiar tabla

            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("id_solicitud"),
                    rs.getString("usuario"),
                    rs.getDate("fecha_solicitud"),
                    rs.getString("tipo"),
                    rs.getString("estado")
                };
                model.addRow(fila);
            }
            rs.close();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar solicitudes: " + e.getMessage());
        }
}

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmAdministrador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmAdministrador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmAdministrador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmAdministrador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new frmlogin().setVisible(true);
        });
    }
// En el método inicializarListenerTablaSolicitudes (modificar)
private void inicializarListenerTablaSolicitudes() {
    jTableGestionarReservas.getSelectionModel().addListSelectionListener(event -> {
        if (!event.getValueIsAdjusting()) {
            int fila = jTableGestionarReservas.getSelectedRow();
            if (fila >= 0) {
                int idReserva = (int) jTableGestionarReservas.getValueAt(fila, 0);
                mostrarDetallesSolicitud(idReserva);
            }
        }
    });
}


// Método para mostrar detalles de la reserva
private void mostrarDetallesSolicitud(int idSolicitud) {
    try {
        usuarioDAO dao = new usuarioDAO();
        StringBuilder detalle = new StringBuilder();
        
        // 1. Obtener info básica de la solicitud
        String sqlSolicitud = "SELECT s.*, u.NOMBRE FROM SOLICITUD s " +
                             "JOIN USUARIO u ON s.ID_USUARIO = u.ID_USUARIO " +
                             "WHERE s.ID_SOLICITUD = ?";
        
        PreparedStatement ps = dao.getConexion().prepareStatement(sqlSolicitud);
        ps.setInt(1, idSolicitud);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            detalle.append("=== SOLICITUD ===\n");
            detalle.append("Usuario: ").append(rs.getString("NOMBRE")).append("\n");
            detalle.append("Tipo: ").append(rs.getString("TIPO")).append("\n");
            detalle.append("Fecha Solicitud: ").append(rs.getDate("FECHA_SOLICITUD")).append("\n");
            detalle.append("Estado: ").append(rs.getString("ESTADO")).append("\n\n");
        }
        rs.close();

        // 2. Obtener detalles específicos de la reserva
        String sqlReserva = "SELECT r.*, sa.NOMBRE AS SALA, eq.NOMBRE AS EQUIPO " +
                           "FROM RESERVA r " +
                           "LEFT JOIN SALA sa ON r.ID_SALA = sa.ID_SALA " +
                           "LEFT JOIN EQUIPO eq ON r.ID_EQUIPO = eq.ID_EQUIPO " +
                           "WHERE r.ID_SOLICITUD = ?";
        
        ps = dao.getConexion().prepareStatement(sqlReserva);
        ps.setInt(1, idSolicitud);
        rs = ps.executeQuery();

        if (rs.next()) {
            detalle.append("=== DETALLES RESERVA ===\n");
            if ("SALA".equals(rs.getString("TIPO_RESERVA"))) {
                detalle.append("Sala: ").append(rs.getString("SALA")).append("\n");
            } else {
                detalle.append("Equipo: ").append(rs.getString("EQUIPO")).append("\n");
            }
            detalle.append("Fecha: ").append(rs.getDate("FECHA")).append("\n");
            detalle.append("Hora Inicio: ").append(rs.getTime("HORA_INICIO")).append("\n");
            detalle.append("Hora Fin: ").append(rs.getTime("HORA_FIN")).append("\n");
        }
        
        txtDetalleSolicitud.setText(detalle.toString());
        rs.close();
        ps.close();
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al cargar detalles: " + e.getMessage());
    }
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonCrear;
    private javax.swing.JButton botonCrear1;
    private javax.swing.JButton botonCrearEquipo;
    private javax.swing.JButton botonCrearSala;
    private javax.swing.JButton botonDeshabilitar;
    private javax.swing.JButton botonEliminar;
    private javax.swing.JButton botonHabilitar;
    private javax.swing.JButton botonModificar;
    private javax.swing.JButton botonModificarReservaEquipo;
    private javax.swing.JButton botonModificarReservaSala;
    private javax.swing.JButton btnAprobar;
    private javax.swing.JButton btnRechazar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTableGestionarReservas;
    private javax.swing.JPanel panelSolicitudes;
    private javax.swing.JTable tableReservaEquipo;
    private javax.swing.JTable tableReservaSala;
    private javax.swing.JTable tblUsuarios;
    private javax.swing.JTextArea txtDetalleSolicitud;
    // End of variables declaration//GEN-END:variables
}














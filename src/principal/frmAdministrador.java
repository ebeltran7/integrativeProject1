/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package principal;

import javax.swing.table.DefaultTableModel;
import principal.frmCrearUsuario;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import java.util.Date;


/**
 *
 * @author NeyBg
 */
public class frmAdministrador extends javax.swing.JFrame {
    DefaultTableModel modeloUsuarios;
    private Usuario usuarioLogueado;

    /**
     * Creates new form frmAdministrador
     * 
     */
    public frmAdministrador(Usuario usuario) {
        this.usuarioLogueado = usuario;
        initComponents();
        this.setLocationRelativeTo(null);

        // Aquí puedes agregar código adicional para inicializar con el usuario
    }
    public frmAdministrador() {
    ;
        initComponents();
        cargarReservasSala();
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

        SpinnerDateModel dateModelSala = new SpinnerDateModel();
        jSpinnerFechaSala.setModel(dateModelSala);
        jSpinnerFechaSala.setEditor(new JSpinner.DateEditor(jSpinnerFechaSala, "dd/MM/yyyy"));

        SpinnerDateModel horaInicioModel = new SpinnerDateModel();
        jSpinnerHoraInicioSala.setModel(horaInicioModel);
        jSpinnerHoraInicioSala.setEditor(new JSpinner.DateEditor(jSpinnerHoraInicioSala, "HH:mm"));

        SpinnerDateModel horaFinModel = new SpinnerDateModel();
        jSpinnerHoraFinSala.setModel(horaFinModel);
        jSpinnerHoraFinSala.setEditor(new JSpinner.DateEditor(jSpinnerHoraFinSala, "HH:mm"));

        // Configurar spinner fecha equipo
        SpinnerDateModel dateModelEquipo = new SpinnerDateModel();
        jSpinnerFechaEquipo.setModel(dateModelEquipo);
        jSpinnerFechaEquipo.setEditor(new JSpinner.DateEditor(jSpinnerFechaEquipo, "dd/MM/yyyy"));

        // Configurar spinner hora inicio equipo
        SpinnerDateModel horaInicioModelEquipo = new SpinnerDateModel();
        jSpinnerHoraInicioEquipo.setModel(horaInicioModelEquipo);
        jSpinnerHoraInicioEquipo.setEditor(new JSpinner.DateEditor(jSpinnerHoraInicioEquipo, "HH:mm"));

        // Configurar spinner hora fin equipo
        SpinnerDateModel horaFinModelEquipo = new SpinnerDateModel();
        jSpinnerHoraFinEquipo.setModel(horaFinModelEquipo);
        jSpinnerHoraFinEquipo.setEditor(new JSpinner.DateEditor(jSpinnerHoraFinEquipo, "HH:mm"));

        inicializarModeloTabla();
        cargarUsuarios();
    }
    
    private void cargarReservasSala() {
        String[] columnas = {"ID", "Nombre Sala", "Usuario", "Fecha Inicial", "Fecha Final"};
        DefaultTableModel modeloSala = new DefaultTableModel(null, columnas);
        tableReservaSala.setModel(modeloSala);

        String sql = "SELECT r.ID_RESERVA, s.NOMBRE AS NOMBRE_SALA, u.NOMBRE AS NOMBRE_USUARIO, r.FECHA, r.HORA_INICIO, r.HORA_FIN\n" +
                     "FROM RESERVA r\n" +
                     "JOIN SALA s ON r.ID_SALA = s.ID_SALA\n" +
                     "JOIN USUARIO u ON r.ID_SOLICITUD = u.ID_USUARIO\n" +
                     "WHERE r.TIPO_RESERVA = 'SALA'\n" +
                     "ORDER BY r.FECHA, r.HORA_INICIO";

        try {
            usuarioDAO dao = new usuarioDAO();
            if (dao.getConexion() == null || dao.getConexion().isClosed()) {
                dao.conectar();
            }
            java.sql.PreparedStatement ps = dao.getConexion().prepareStatement(sql);
            java.sql.ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] fila = new Object[5];
                fila[0] = rs.getInt("id_reserva");
                fila[1] = rs.getString("nombre_sala");
                fila[2] = rs.getString("nombre_Usuario");
                fila[3] = rs.getTimestamp("fecha_inicio");
                fila[4] = rs.getTimestamp("fecha_fin");
                modeloSala.addRow(fila);
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar reservas de salas: " + e.getMessage());
        }
    }
    
    private void cargarReservasEquipo() {
        String[] columnas = {"ID", "Nombre Equipo", "Usuario", "Fecha", "Hora Inicio", "Hora Fin"};
        DefaultTableModel modeloEquipo = new DefaultTableModel(null, columnas);
        tableReservaEquipo.setModel(modeloEquipo);

        String sql = "SELECT r.ID_RESERVA, e.NOMBRE AS NOMBRE_EQUIPO, u.NOMBRE AS NOMBRE_USUARIO, r.FECHA, r.HORA_INICIO, r.HORA_FIN " +
                     "FROM RESERVA r " +
                     "JOIN EQUIPO e ON r.ID_EQUIPO = e.ID_EQUIPO " +
                     "JOIN SOLICITUD sol ON r.ID_SOLICITUD = sol.ID_SOLICITUD " +
                     "JOIN USUARIO u ON sol.ID_USUARIO = u.ID_USUARIO " +
                     "WHERE r.TIPO_RESERVA = 'EQUIPO' " +
                     "ORDER BY r.FECHA, r.HORA_INICIO";

        try {
            usuarioDAO dao = new usuarioDAO();
            if (dao.getConexion() == null || dao.getConexion().isClosed()) {
                dao.conectar();
            }
            java.sql.PreparedStatement ps = dao.getConexion().prepareStatement(sql);
            java.sql.ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Object[] fila = new Object[6];
                fila[0] = rs.getInt("ID_RESERVA");
                fila[1] = rs.getString("NOMBRE_EQUIPO");
                fila[2] = rs.getString("NOMBRE_USUARIO");
                fila[3] = rs.getDate("FECHA");
                fila[4] = rs.getTimestamp("HORA_INICIO");
                fila[5] = rs.getTimestamp("HORA_FIN");
                modeloEquipo.addRow(fila);
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar reservas de equipos: " + e.getMessage());
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

    private void crearReservaSala() {
        try {
            Date fecha = (Date) jSpinnerFechaSala.getValue();
            Date horaInicio = (Date) jSpinnerHoraInicioSala.getValue();
            Date horaFin = (Date) jSpinnerHoraFinSala.getValue();

            // Validaciones básicas
            if (horaFin.before(horaInicio) || horaFin.equals(horaInicio)) {
                JOptionPane.showMessageDialog(this, "La hora fin debe ser posterior a la hora inicio.");
                return;
            }

            // Aquí deberías agregar validación de horario permitido (6:00 am - 9:40 pm)
            // y validación de solapamiento (pendiente implementar)

            // Construir objeto reserva o preparar la consulta SQL para insertar
            String sqlInsert = "INSERT INTO RESERVA (FECHA, HORA_INICIO, HORA_FIN, TIPO_RESERVA, ID_SALA, ID_SOLICITUD) VALUES (?, ?, ?, ?, ?, ?)";

            usuarioDAO dao = new usuarioDAO();
            if (dao.getConexion() == null || dao.getConexion().isClosed()) {
                dao.conectar();
            }

            java.sql.PreparedStatement ps = dao.getConexion().prepareStatement(sqlInsert);
            ps.setDate(1, new java.sql.Date(fecha.getTime()));
            ps.setTimestamp(2, new java.sql.Timestamp(horaInicio.getTime()));
            ps.setTimestamp(3, new java.sql.Timestamp(horaFin.getTime()));
            ps.setString(4, "SALA");
            // Por ahora pon un ID_SALA fijo o selecciónalo desde UI (pendiente)
            ps.setInt(5, 1); // Cambiar por el ID de sala seleccionado
            // El ID_SOLICITUD también se debe obtener de la lógica, o dejar null si no aplica
            ps.setNull(6, java.sql.Types.INTEGER);

            int filas = ps.executeUpdate();
            if (filas > 0) {
                JOptionPane.showMessageDialog(this, "Reserva de sala creada exitosamente.");
                cargarReservasSala(); // Refrescar tabla
            } else {
                JOptionPane.showMessageDialog(this, "Error al crear la reserva.");
            }

            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al crear reserva: " + e.getMessage());
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
        jTable1 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtDetalleSolicitud = new javax.swing.JTextArea();
        btnAprobar = new javax.swing.JButton();
        btnRechazar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tableReservaEquipo = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        tableReservaSala = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        botonCrearSala = new javax.swing.JButton();
        botonCrearEquipo = new javax.swing.JButton();
        botonModificarReservaSala = new javax.swing.JButton();
        botonModificarReservaEquipo = new javax.swing.JButton();
        jSpinnerFechaSala = new javax.swing.JSpinner();
        jSpinnerHoraInicioSala = new javax.swing.JSpinner();
        jSpinnerHoraFinSala = new javax.swing.JSpinner();
        jSpinnerFechaEquipo = new javax.swing.JSpinner();
        jSpinnerHoraInicioEquipo = new javax.swing.JSpinner();
        jSpinnerHoraFinEquipo = new javax.swing.JSpinner();

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

        jTable1.setBackground(new java.awt.Color(0, 102, 153));
        jTable1.setForeground(new java.awt.Color(255, 255, 255));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(jTable1);

        panelSolicitudes.add(jScrollPane2);
        jScrollPane2.setBounds(0, 0, 370, 150);

        txtDetalleSolicitud.setBackground(new java.awt.Color(0, 102, 153));
        txtDetalleSolicitud.setColumns(20);
        txtDetalleSolicitud.setForeground(new java.awt.Color(255, 255, 255));
        txtDetalleSolicitud.setRows(5);
        jScrollPane3.setViewportView(txtDetalleSolicitud);

        panelSolicitudes.add(jScrollPane3);
        jScrollPane3.setBounds(370, 0, 238, 90);

        btnAprobar.setBackground(new java.awt.Color(255, 153, 0));
        btnAprobar.setForeground(new java.awt.Color(0, 102, 153));
        btnAprobar.setText("Aceptar");
        btnAprobar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnAprobar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAprobarActionPerformed(evt);
            }
        });
        panelSolicitudes.add(btnAprobar);
        btnAprobar.setBounds(390, 100, 45, 20);

        btnRechazar.setBackground(new java.awt.Color(255, 153, 0));
        btnRechazar.setForeground(new java.awt.Color(0, 102, 153));
        btnRechazar.setText("Rechazar");
        btnRechazar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnRechazar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRechazarActionPerformed(evt);
            }
        });
        panelSolicitudes.add(btnRechazar);
        btnRechazar.setBounds(490, 100, 80, 20);

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
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
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(panelSolicitudes, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnSalir)))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                .addComponent(btnSalir)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Gestion de Usuarios", jPanel1);

        jPanel2.setBackground(new java.awt.Color(0, 102, 153));

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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSpinnerFechaSala, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinnerFechaEquipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinnerHoraInicioEquipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinnerHoraFinEquipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinnerHoraFinSala, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinnerHoraInicioSala, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(botonCrearEquipo)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(botonModificarReservaSala)
                                .addComponent(botonCrearSala))
                            .addComponent(botonModificarReservaEquipo))
                        .addGap(24, 24, 24))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(botonCrearSala)
                            .addComponent(jSpinnerFechaSala, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(botonModificarReservaSala)
                            .addComponent(jSpinnerHoraInicioSala, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jSpinnerHoraFinSala, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(botonCrearEquipo)
                        .addGap(18, 18, 18)
                        .addComponent(botonModificarReservaEquipo))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jSpinnerFechaEquipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jSpinnerHoraInicioEquipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jSpinnerHoraFinEquipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(106, 106, 106))
        );

        jTabbedPane1.addTab("Gestion de Reservas", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 542, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        // TODO add your handling code here:
        int fila = jTable1.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una solicitud para aprobar.");
            return;
        }
        int idSolicitud = (int) jTable1.getValueAt(fila, 0);
        cambiarEstadoSolicitud(idSolicitud, "APROBADO");
    }//GEN-LAST:event_btnAprobarActionPerformed

    private void btnRechazarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRechazarActionPerformed
        // TODO add your handling code here:
        int fila = jTable1.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una solicitud para rechazar.");
            return;
        }
        int idSolicitud = (int) jTable1.getValueAt(fila, 0);
        cambiarEstadoSolicitud(idSolicitud, "RECHAZADO");
    }//GEN-LAST:event_btnRechazarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();              // Cierra el formulario actual
        new frmlogin().setVisible(true); // Abre el formulario de login

    }//GEN-LAST:event_btnSalirActionPerformed

    private void botonCrearSalaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonCrearSalaActionPerformed
        // Asumiendo que tenemos la variable usuarioLogueado
        String rolActual = usuarioLogueado.getRol();
        // Esto te abrirá o generará automáticamente el método manejador del evento
        frmCrearReservaSala formulario = new frmCrearReservaSala(this, true, rolActual);
        formulario.setLocationRelativeTo(this); // Centra el formulario en relación al padre
        formulario.setVisible(true); // Muestra el formulario modal
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

    private void cambiarEstadoSolicitud(int idSolicitud, String nuevoEstado) {
        try {
            usuarioDAO dao = new usuarioDAO();
            if (dao.getConexion() == null || dao.getConexion().isClosed()) {
                dao.conectar();
            }
            String sql = "UPDATE solicitud SET estado = ? WHERE id_solicitud = ?";
            java.sql.PreparedStatement ps = dao.getConexion().prepareStatement(sql);
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idSolicitud);
            int filas = ps.executeUpdate();
            ps.close();

            if (filas > 0) {
                JOptionPane.showMessageDialog(this, "Solicitud " + nuevoEstado.toLowerCase() + " correctamente.");
                cargarSolicitudesPendientes();
                txtDetalleSolicitud.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar la solicitud.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al actualizar estado: " + e.getMessage());
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

            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
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
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmAdministrador().setVisible(true);
            }
        });
    }
private void inicializarListenerTablaSolicitudes() {
    jTable1.getSelectionModel().addListSelectionListener(event -> {
        if (!event.getValueIsAdjusting()) {
            int fila = jTable1.getSelectedRow();
            if (fila >= 0) {
                int idSolicitud = (int) jTable1.getValueAt(fila, 0);
                mostrarDetallesSolicitud(idSolicitud);
            }
        }
    });
}

private void mostrarDetallesSolicitud(int idSolicitud) {
    try {
        usuarioDAO dao = new usuarioDAO();
        if (dao.getConexion() == null || dao.getConexion().isClosed()) {
            dao.conectar();
        }

        // Consulta ejemplo para obtener comentarios (ajusta según estructura BD)
        String sql = "SELECT comentario FROM solicitud WHERE id_solicitud = ?";
        java.sql.PreparedStatement ps = dao.getConexion().prepareStatement(sql);
        ps.setInt(1, idSolicitud);
        java.sql.ResultSet rs = ps.executeQuery();

        String comentario = "";
        if (rs.next()) {
            comentario = rs.getString("comentario");
        }
        txtDetalleSolicitud.setText("Comentario:\n" + comentario);

        rs.close();
        ps.close();
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error al obtener detalles: " + e.getMessage());
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
    private javax.swing.JSpinner jSpinnerFechaEquipo;
    private javax.swing.JSpinner jSpinnerFechaSala;
    private javax.swing.JSpinner jSpinnerHoraFinEquipo;
    private javax.swing.JSpinner jSpinnerHoraFinSala;
    private javax.swing.JSpinner jSpinnerHoraInicioEquipo;
    private javax.swing.JSpinner jSpinnerHoraInicioSala;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel panelSolicitudes;
    private javax.swing.JTable tableReservaEquipo;
    private javax.swing.JTable tableReservaSala;
    private javax.swing.JTable tblUsuarios;
    private javax.swing.JTextArea txtDetalleSolicitud;
    // End of variables declaration//GEN-END:variables
}



























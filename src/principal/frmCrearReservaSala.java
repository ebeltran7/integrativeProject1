package principal;

import javax.swing.*;
import javax.swing.SpinnerDateModel;
import java.util.*;
import java.sql.PreparedStatement;
import principal.Usuario;
import principal.usuarioDAO;
import principal.Sala;
import principal.salaDAO;

public class frmCrearReservaSala extends javax.swing.JDialog {

    // Mapas para mantener nombre -> id
    private Map<String, Integer> usuariosMap = new HashMap<>();
    private Map<String, Integer> salasMap = new HashMap<>();
    private String rolUsuario;


    public frmCrearReservaSala(java.awt.Frame parent, boolean modal, String rolUsuario) {
        super(parent, modal);
        this.rolUsuario = rolUsuario;
        initComponents();

        cargarSalasEnCombo();
        cargarUsuariosEnCombo();
        configurarSpinners();

        btnCrearReserva.addActionListener(evt -> crearReservaSala());
        
        ajustarPermisosPorRol();
    }

    private void ajustarPermisosPorRol() {
        if ("estudiante".equalsIgnoreCase(rolUsuario)) {
            comboUsuario.setEnabled(false);
            btnCrearReserva.setEnabled(false);
            JOptionPane.showMessageDialog(this, "Los estudiantes no pueden reservar salas.", "Acceso denegado", JOptionPane.WARNING_MESSAGE);
        } else {
            comboUsuario.setEnabled(true);
            btnCrearReserva.setEnabled(true);
        }
    }


    
    private void cargarSalasEnCombo() {
        comboSala.removeAllItems();
        salasMap.clear();
        salaDAO dao = new salaDAO();
        try {
            List<Sala> salas = dao.obtenerTodasSalas();
            for (Sala s : salas) {
                comboSala.addItem(s.getNombre());
                salasMap.put(s.getNombre(), s.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error cargando salas: " + e.getMessage());
        }
    }

    private void cargarUsuariosEnCombo() {
        comboUsuario.removeAllItems();
        usuariosMap.clear();
        usuarioDAO dao = new usuarioDAO();
        try {
            List<Usuario> usuarios = dao.obtenerTodosUsuarios();
            for (Usuario u : usuarios) {
                if (!u.getRol().equalsIgnoreCase("estudiante"))
                comboUsuario.addItem(u.getNombre());
                usuariosMap.put(u.getNombre(), u.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error cargando usuarios: " + e.getMessage());
        }
    }

    private void configurarSpinners() {
        spinnerFecha.setModel(new SpinnerDateModel());
        spinnerFecha.setEditor(new JSpinner.DateEditor(spinnerFecha, "dd/MM/yyyy"));

        spinnerHoraInicio.setModel(new SpinnerDateModel());
        spinnerHoraInicio.setEditor(new JSpinner.DateEditor(spinnerHoraInicio, "HH:mm"));

        spinnerHoraFin.setModel(new SpinnerDateModel());
        spinnerHoraFin.setEditor(new JSpinner.DateEditor(spinnerHoraFin, "HH:mm"));
    }

    private void crearReservaSala() {
        try {
            int idSala = getIdSalaSeleccionada();
            int idUsuario = getIdUsuarioSeleccionado();
            Date fecha = getFechaSeleccionada();
            Date horaInicio = getHoraInicioSeleccionada();
            Date horaFin = getHoraFinSeleccionada();

            if (idSala == -1 || idUsuario == -1) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar sala y usuario válidos.");
                return;
            }

            if (horaFin.before(horaInicio) || horaFin.equals(horaInicio)) {
                JOptionPane.showMessageDialog(this, "La hora de fin debe ser posterior a la hora de inicio.");
                return;
            }
           
            if (!horaDentroDeRango(horaInicio) || !horaDentroDeRango(horaFin)) {
                JOptionPane.showMessageDialog(this, "La hora debe estar dentro del rango de 6:00 am a 9:40 pm.");
                return;
            }


            String sqlInsert = "INSERT INTO RESERVA (FECHA, HORA_INICIO, HORA_FIN, TIPO_RESERVA, ID_SALA, ID_SOLICITUD) "
                    + "VALUES (?, ?, ?, 'SALA', ?, ?)";

            usuarioDAO dao = new usuarioDAO();
            if (dao.getConexion() == null || dao.getConexion().isClosed()) {
                dao.conectar();
            }

            PreparedStatement ps = dao.getConexion().prepareStatement(sqlInsert);
            ps.setDate(1, new java.sql.Date(fecha.getTime()));
            ps.setTimestamp(2, new java.sql.Timestamp(horaInicio.getTime()));
            ps.setTimestamp(3, new java.sql.Timestamp(horaFin.getTime()));
            ps.setInt(4, idSala);
            ps.setInt(5, idUsuario);

            int filas = ps.executeUpdate();
            if (filas > 0) {
                JOptionPane.showMessageDialog(this, "Reserva de sala creada exitosamente.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al crear la reserva.");
            }
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al crear la reserva: " + e.getMessage());
        }
    }

    private Date getHoraLimiteInicio() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 6);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    private Date getHoraLimiteFin() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 21);
        c.set(Calendar.MINUTE, 40);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public int getIdUsuarioSeleccionado() {
        String nombre = (String) comboUsuario.getSelectedItem();
        if (nombre != null && usuariosMap.containsKey(nombre)) {
            return usuariosMap.get(nombre);
        }
        return -1;
    }

    public int getIdSalaSeleccionada() {
        String nombre = (String) comboSala.getSelectedItem();
        if (nombre != null && salasMap.containsKey(nombre)) {
            return salasMap.get(nombre);
        }
        return -1;
    }

    public Date getFechaSeleccionada() {
        return (Date) spinnerFecha.getValue();
    }

    public Date getHoraInicioSeleccionada() {
        return (Date) spinnerHoraInicio.getValue();
    }

    public Date getHoraFinSeleccionada() {
        return (Date) spinnerHoraFin.getValue();
    }
    
    private boolean horaDentroDeRango(Date hora) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(hora);
        int minutos = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);

        int limiteInicio = 6 * 60;       // 6:00 am en minutos
        int limiteFin = 21 * 60 + 40;    // 9:40 pm en minutos

        return minutos >= limiteInicio && minutos <= limiteFin;
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        comboSala = new javax.swing.JComboBox<>();
        comboUsuario = new javax.swing.JComboBox<>();
        spinnerFecha = new javax.swing.JSpinner();
        spinnerHoraInicio = new javax.swing.JSpinner();
        spinnerHoraFin = new javax.swing.JSpinner();
        btnCrearReserva = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 102, 153));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        comboSala.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        comboUsuario.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        comboUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboUsuarioActionPerformed(evt);
            }
        });

        btnCrearReserva.setText("crear");
        btnCrearReserva.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCrearReservaActionPerformed(evt);
            }
        });

        btnCancelar.setText("cancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(101, 101, 101)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(comboSala, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnCrearReserva)
                        .addGap(68, 68, 68)
                        .addComponent(btnCancelar))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(spinnerFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spinnerHoraInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spinnerHoraFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(76, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(42, Short.MAX_VALUE)
                .addComponent(comboSala, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(spinnerHoraInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spinnerHoraFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spinnerFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(comboUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCrearReserva)
                    .addComponent(btnCancelar))
                .addGap(90, 90, 90))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    private void btnCrearReservaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearReservaActionPerformed
        // TODO add your handling code here:
        
        crearReservaSala();  // Llamamos al método para guardar la reserva
    
    }//GEN-LAST:event_btnCrearReservaActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // volvemos atras
        this.dispose();
        
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void comboUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboUsuarioActionPerformed

    /**
     * @param args the command line arguments
     */
public static void main(String args[]) {
    /* Set the Nimbus look and feel */
    //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
    try {
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
        java.util.logging.Logger.getLogger(frmCrearReservaSala.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    /* Create and display the dialog */
    java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            // Definir rol para la prueba. En producción debe venir del usuario logueado
            String rolActual = "administrador";  // o "estudiante", según quieras probar

            frmCrearReservaSala dialog = new frmCrearReservaSala(new javax.swing.JFrame(), true, rolActual);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        }
    });
}


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnCrearReserva;
    private javax.swing.JComboBox<String> comboSala;
    private javax.swing.JComboBox<String> comboUsuario;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSpinner spinnerFecha;
    private javax.swing.JSpinner spinnerHoraFin;
    private javax.swing.JSpinner spinnerHoraInicio;
    // End of variables declaration//GEN-END:variables
}
















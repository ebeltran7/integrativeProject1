
package principal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;



/**
 *
 * @author NeyBg
 */
public class frmequipo extends javax.swing.JFrame {
    private String rolUsuario; // "Estudiante", "Profesor", "Administrativo", etc.Rol actual
    private JComboBox<String> comboEquipos;

    /**
    * Constructor donde se recibe rol
     */
    public frmequipo(String rolUsuario) {
        this.rolUsuario = rolUsuario;
        initComponents();
        setLocationRelativeTo(null);  // Centra la ventana en la pantalla
        cargarComponentes();
        cargarEquiposSegunRol();

    }

    

    private void cargarComponentes() {
        comboEquipos = new JComboBox<>();
        // agrega comboEquipos a algún panel o contenedor del formulario
        this.getContentPane().add(comboEquipos);
        // También configura tamaño y posición según tu layout o usa un LayoutManager
        comboEquipos.setBounds(20, 20, 200, 25);
    }

    // método para cargar equipos
    private void cargarEquipos() {
        // aquí puedes llenar comboEquipos con datos
        comboEquipos.removeAllItems();
        comboEquipos.addItem("PC-01");
        comboEquipos.addItem("PC-02");
        // etc.
    }
  
    
    private void cargarEquiposSegunRol() {
        List<Equipo> listaEquipos = new ArrayList<>();
        String sql = "";

        if ("Estudiante".equalsIgnoreCase(rolUsuario)) {
            sql = "SELECT ID_EQUIPO, NOMBRE, TIPO, ESTADO FROM EQUIPO WHERE TIPO = 'PC' AND ESTADO = 'DISPONIBLE'";
        } else {
            sql = "SELECT ID_EQUIPO, NOMBRE, TIPO, ESTADO FROM EQUIPO WHERE ESTADO = 'DISPONIBLE'";
        }
        // Cadena correcta para la conexión Oracle XE, usuario y contraseña reales
        String url = "jdbc:oracle:thin:@localhost:1521:XE";
        String usuario = "prueba2";
        String clave = "prueba2";    
        //esto es para la conexion de la univeridad
        //try (Connection conn = DriverManager.getConnection("jdbc:tu_conexion", "usuario", "clave");
        try {
            // Cargamos el driver Oracle (recomendado)
            Class.forName("oracle.jdbc.driver.OracleDriver");        
        
            try (Connection conn = DriverManager.getConnection(url, usuario, clave);
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

                
                listaEquipos.clear();
                comboEquipos.removeAllItems();
                
                while (rs.next()) {
                // Aquí lee el ID como Long, no UUID
                int idEquipo = rs.getInt("ID_EQUIPO"); 
                    String nombre = rs.getString("NOMBRE");
                    String tipo = rs.getString("TIPO");
                    String estado = rs.getString("ESTADO");
                        
                    Equipo equipo = new Equipo(idEquipo, nombre, tipo, estado);
                    listaEquipos.add(equipo);
                    comboEquipos.addItem(nombre);
                }
            }

        } catch (ClassNotFoundException ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,"Error: No se encontró el driver JDBC de Oracle.\\n" + ex.getMessage());

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al cargar equipos: " + e.getMessage());
        }
    

    // Otros métodos y componentes...

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
        jLabel1 = new javax.swing.JLabel();
        botonPortatil = new javax.swing.JButton();
        botonCamara = new javax.swing.JButton();
        botonHdmi = new javax.swing.JButton();
        botonMicrofono = new javax.swing.JButton();
        botonCabledeRed = new javax.swing.JButton();
        txtequipos = new javax.swing.JLabel();
        jButtonVolver = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("EQUIPOS");

        jPanel1.setBackground(new java.awt.Color(0, 102, 153));
        jPanel1.setForeground(new java.awt.Color(0, 102, 153));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/udi.png"))); // NOI18N

        botonPortatil.setBackground(new java.awt.Color(0, 102, 153));
        botonPortatil.setForeground(new java.awt.Color(0, 102, 153));
        botonPortatil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/ordenador-portatil.png"))); // NOI18N
        botonPortatil.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Portatil", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 255, 255))); // NOI18N
        botonPortatil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonPortatilActionPerformed(evt);
            }
        });

        botonCamara.setBackground(new java.awt.Color(0, 102, 153));
        botonCamara.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/camara-web.png"))); // NOI18N
        botonCamara.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Camara", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        botonHdmi.setBackground(new java.awt.Color(0, 102, 153));
        botonHdmi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/puerto-hdmi.png"))); // NOI18N
        botonHdmi.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "HDMI", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        botonMicrofono.setBackground(new java.awt.Color(0, 102, 153));
        botonMicrofono.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/sonido.png"))); // NOI18N
        botonMicrofono.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Microfo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        botonCabledeRed.setBackground(new java.awt.Color(0, 102, 153));
        botonCabledeRed.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/ethernet.png"))); // NOI18N
        botonCabledeRed.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Ethernet", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        txtequipos.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        txtequipos.setForeground(new java.awt.Color(255, 255, 255));
        txtequipos.setText("EQUIPOS");

        jButtonVolver.setText("atras");
        jButtonVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVolverActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtequipos, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 60, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(94, 94, 94)
                        .addComponent(botonPortatil, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(botonCamara, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(45, 45, 45)
                .addComponent(botonCabledeRed, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(61, 61, 61))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(botonMicrofono, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(48, 48, 48)
                        .addComponent(botonHdmi, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(136, 136, 136))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonVolver)
                        .addGap(25, 25, 25))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtequipos, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botonCamara, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonCabledeRed, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonPortatil, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(82, 82, 82)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(botonHdmi, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(botonMicrofono, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                .addComponent(jButtonVolver)
                .addGap(22, 22, 22))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void botonPortatilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonPortatilActionPerformed

    }//GEN-LAST:event_botonPortatilActionPerformed

    private void jButtonVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVolverActionPerformed
        //Abrir frmroles, pasando el rol actual si es necesario
        frmroles frm = new frmroles(this.rolUsuario);
                frm.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButtonVolverActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmequipo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmequipo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmequipo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmequipo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmequipo("Estudiante").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton botonCabledeRed;
    private javax.swing.JButton botonCamara;
    private javax.swing.JButton botonHdmi;
    private javax.swing.JButton botonMicrofono;
    private javax.swing.JButton botonPortatil;
    private javax.swing.JButton jButtonVolver;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel txtequipos;
    // End of variables declaration//GEN-END:variables
}

















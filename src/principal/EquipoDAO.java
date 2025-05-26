/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author NeyBg
 */
public class EquipoDAO {
    private Connection conexion;

    public EquipoDAO(Connection conexion) {
        this.conexion = conexion;
    }

    public int buscarIdPorNombre(String nombre) throws SQLException {
        int idEquipo = -1;
        String sql = "SELECT ID_EQUIPO FROM EQUIPO WHERE NOMBRE = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    idEquipo = rs.getInt("ID_EQUIPO");
                }
            }
        }
        return idEquipo;
    }

    // Aquí puedes agregar otros métodos para CRUD de equipos
    
}

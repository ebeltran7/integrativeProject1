/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal;


import java.sql.Connection;
import principal.conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import principal.Sala;
public class salaDAO {
    private Connection conn;

    public salaDAO() {
        conn = conexion.getConnection();  // Aquí usas la clase conexion que tienes
    }

    public Connection getConexion() {
        return conn;
    }

    public List<Sala> obtenerTodasSalas() {
        List<Sala> listaSalas = new ArrayList<>();
        String sql = "SELECT ID_SALA, NOMBRE, CAPACIDAD, SOFTWARE FROM SALA ORDER BY NOMBRE";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Sala sala = new Sala(
                    rs.getInt("ID_SALA"),
                    rs.getString("NOMBRE"),
                    rs.getInt("CAPACIDAD"),
                    rs.getString("SOFTWARE")
                );        
                listaSalas.add(sala);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listaSalas;
    }

}


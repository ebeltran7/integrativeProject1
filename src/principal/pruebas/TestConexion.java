/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.pruebas;

import principal.conexion;

/**
 *
 * @author NeyBg
 */
public class TestConexion {
    public static void main(String[] args) {
        if (conexion.getConnection() != null) {
            System.out.println("Conexión exitosa con Oracle local!");
        } else {
            System.out.println("No se pudo conectar.");
        }
    }
}

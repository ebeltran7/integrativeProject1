
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal.pruebas;
//importamos para que reconozca a usuarioDAO
import principal.usuarioDAO;

/**
 *
 * @author NeyBg
 */
public class TestSolicitudCambio {
    public static void main(String[] args) {
        usuarioDAO dao = new usuarioDAO();

        boolean exito = dao.solicitarCambioClave(1, "nuevaClave123"); // Asume idUsuario=1

        if (exito) {
            System.out.println("Solicitud registrada correctamente.");
        } else {
            System.out.println("Error al registrar solicitud.");
        }
    }    
}
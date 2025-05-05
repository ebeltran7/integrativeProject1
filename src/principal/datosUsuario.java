/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author NeyBg
 */
public class datosUsuario {
    

    public static List<Usuario> obtenerUsuario(){
        List<Usuario> lista = new ArrayList<>();
        lista.add(new Usuario("1", "Ferney", "ebeltran7@udi.edu.co", "1234", "estudiante"));
        lista.add(new Usuario("2", "Leo", "lleiva@udi.edu.co", "abcd", "profesor" ));
        lista.add(new Usuario("2", "Ana", "ana@correo.com", "abcd", "profesor"));
        lista.add(new Usuario("3", "Carlos", "carlos@correo.com", "admin", "administrativo"));
        return lista;
    }
        private static ArrayList<Usuario> listaUsuario = new ArrayList<>();
        public static void agregarUsuario(Usuario u) {
            listaUsuario.add(u);
    }

       
    
    
}
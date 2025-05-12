/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal;

/**
 * Modelo de datos para representar un usuario del sistema
 * @author NeyBg
 */
public class Usuario {
    private String id;
    private String nombre;
    private String correo;
    private String contrasena;
    private String rol; // rol le llamamos al estudiante, al profesor o el administrativo.
    
    public Usuario(){
    
    }
    //Metodo constructor
    public Usuario(String id, String nombre, String correo, String contrasena, String rol){
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.rol = rol;
                                
    }
    
    //geters 
    public String getId(){
        return id;
    }
    
    public String getNombre(){
        return nombre;
    }
    
        public String getCorreo(){
        return correo;
    }
        public String getContrasena(){
        return contrasena;
    }
            public String getRol(){
        return rol;
    }
            
    //setters
    public void setId(String id) {
        this.id = id;
    }
        public void setNombre(String nombre) {
        this.nombre = nombre;
    }
        public void setCorreo(String correo) {
        this.correo = correo;
        
    }     
        public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
        public void setRol(String rol) {
        this.rol = rol;
    }
    
    public boolean verificarCrdenciales(String correo, String contrasena){
        return this.correo.equals(correo)&& this.contrasena.equals(contrasena);
    
    }    

    
        
    /**
     * @param args the command line arguments
     */
    
}

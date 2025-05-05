/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal;

/**
 *
 * @author NeyBg
 */
public class Equipo {
    private int id;
    private String nombre;
    private String tipo; //ejemplo portatil, camara, otros.
    private String estado; //Disponible prestado o en mantenimiento.
    
        public Equipo(int id, String nombre, String tipo, String estado){
            this.id = id;
            this.nombre = nombre;
            this.tipo = tipo;
            this.estado = estado;
        }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public String getEstado() {
        return estado;
    }
            
    
}


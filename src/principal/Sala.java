/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal;

/**
 *
 * @author NeyBg
 */
public class Sala {
    
    private int id;
    private String nombre;
    private int capacidad;
    private String softwareRequerido; //tipo de Sala de computo con ciertas softwares o packetes.
    
    public Sala (int id, String nombre, int capacidad, String softwareRequerido) {
        this.id = id;
        this.nombre = nombre;
        this.capacidad = capacidad;
        this.softwareRequerido = softwareRequerido;
    }
    public int getId(){
    return id ;
    }
    
    public String getNombre(){
        return nombre;
    }
    
    public int getCapacidad(){
        return capacidad;
    }
    
    public String getSofwareRequerido(){
        return softwareRequerido;
    }
    
    public void setId( int id){
        this.id = id;
    }
    
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    
    public void setCapacidad (int capacidad){
        this.capacidad = capacidad;
    }
    
    public void setSoftwareRequerido(String softwareRequerido){
        this.softwareRequerido = softwareRequerido;
    }
    
}

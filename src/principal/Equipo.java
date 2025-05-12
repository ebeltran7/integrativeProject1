/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
/**
 *
 * @author NeyBg
 */
public class Equipo {
    private int id;
    private String nombre;
    private String tipo; //ejemplo portatil, camara, otros.
    private String estado; //Disponible prestado o en mantenimiento.
    private String accesorios; //cargador, mouse, estuche,y cables.
    
    public Equipo(int id, String nombre, String tipo, String estado){
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.estado = estado;
        this.accesorios =  accesorios;
    }
         // Método para verificar integridad del equipo
    public boolean verificarAccesorios(String accesoriosPresentes) {
        return this.accesorios.equals(accesoriosPresentes);
    }
    
    public boolean tieneTodosAccesorios(String accesoriosVerificados) {
    if (this.accesorios == null || this.accesorios.isEmpty()) {
        return true; // Si el equipo no requiere accesorios
    }
    
    if (accesoriosVerificados == null || accesoriosVerificados.isEmpty()) {
        return false; // Si faltan accesorios pero debería tener
    }
    
    // Dividir los accesorios en arrays
    String[] requeridos = this.accesorios.split(",");
    String[] verificados = accesoriosVerificados.split(",");
    
    // Verificar que todos los requeridos estén en los verificados
    for (String req : requeridos) {
        boolean encontrado = false;
        String reqTrim = req.trim(); // Eliminar espacios en blanco
        
        for (String ver : verificados) {
            if (ver.trim().equalsIgnoreCase(reqTrim)) {
                encontrado = true;
                break;
            }
        }
        
        if (!encontrado) {
            return false;
        }
    }
    
    return true;
}
    
        // Método para cambiar estado
    public void cambiarEstado(String nuevoEstado) {
        if (nuevoEstado.matches("Disponible|Prestado|En mantenimiento")) {
            this.estado = nuevoEstado;
        } else {
            throw new IllegalArgumentException("Estado no válido");
        }
    }

    public void setAccesorios(String accesorios) {
        this.accesorios = accesorios;
    }

    public String getAccesorios() {
        return accesorios;
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


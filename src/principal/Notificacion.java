/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal;

import java.time.LocalDate;

/**
 *
 * @author NeyBg
 */
public class Notificacion {
    private int id;
    private String mensaje;
    private LocalDate  fecha;
    private String usuarioId;
    private boolean leido;
    
    public Notificacion(int id, String mensaje, LocalDate fecha, String usuarioId, boolean leido){
        this.id = id;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.usuarioId = usuarioId;
        this.leido = leido;
        
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }

    public int getId() {
        return id;
    }

    public String getMensaje() {
        return mensaje;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public boolean isLeido() {
        return leido;
    }
    
}

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
public class Retroalimentacion {
    private String id;
    private String usuarioId;
    private String tipo; // sala o equipo
    private String referenciaId; //ID de la sala o quipo al que se le refiere el dato.
    private String comentario;
    private int calificacion; // 1 al 5
    private LocalDate fecha;
    
        public Retroalimentacion(String id, String usuarioId, String tipo, String referenciaId, String comentario, int calificacion, LocalDate fecha){
            this.id = id;
            this.usuarioId = usuarioId;
            this.tipo = tipo;
            this.referenciaId = referenciaId;
            this.comentario = comentario;
            this.calificacion = calificacion;
            this.fecha = fecha;
        }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setReferenciaId(String referenciaId) {
        this.referenciaId = referenciaId;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getId() {
        return id;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public String getTipo() {
        return tipo;
    }

    public String getReferenciaId() {
        return referenciaId;
    }

    public String getComentario() {
        return comentario;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public LocalDate getFecha() {
        return fecha;
    }
    
    
}



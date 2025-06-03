/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package principal;

import java.time.LocalDateTime;

/**
 *
 * @author NeyBg
 */
public class reservaSala {
    private int id;
    private int idUsuario;
    private int idSala;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin ;
    private String estado ; 
    
    public reservaSala(int id, int idUsuario, int idSala, LocalDateTime fechaInicio, LocalDateTime fechaFin, String estado){
        this.id = id;
        this.idUsuario = idUsuario;
        this.idSala = idSala;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
        
    }

    public int getId() {
        return id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public int getIdSala() {
        return idSala;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public String getEstado() {
        return estado;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setIdSala(int idSala) {
        this.idSala = idSala;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
}

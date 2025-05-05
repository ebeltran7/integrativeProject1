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
public class prestamoEquipo {
    private int id;
    private int idUsuario;
    private int idEquipo;
    private LocalDateTime fechaPrestamo;
    private LocalDateTime fechaDevolucion;
    private String estado; //prestado, devuelto, retrasado o en emora.
    
    public prestamoEquipo(int id, int idUsuario, int idEquipo, LocalDateTime fechaPrestamo, LocalDateTime fechaDevolucion, String estado){
        this.id = id;
        this.idUsuario = idUsuario;
        this.idEquipo = idEquipo;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public int getIdEquipo() {
        return idEquipo;
    }

    public LocalDateTime getFechaPrestamo() {
        return fechaPrestamo;
    }

    public LocalDateTime getFechaDevolucion() {
        return fechaDevolucion;
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

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    public void setFechaPrestamo(LocalDateTime fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public void setFechaDevolucion(LocalDateTime fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}

/**
 * Clase que representa un préstamo de equipo segun requerimientos, de gestión de activos.
 * Maneja toda la información relacionada con el préstamo, devolución y estado de equipos.
 * incluimos firma.
 * 
 * @author NeyBg
 * @version 1.0
 */
package principal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;



public class prestamoEquipo {
    //Atributos de la clase
    private int id;
    private int idUsuario;
    private int idEquipo;
    private LocalDateTime fechaPrestamo;
    private LocalDateTime fechaDevolucion;
    private String lugarEntrega;
    private String estado; //prestado, devuelto, retrasado o en emora.
    private boolean acuerdoFirmado;
    private String observaciones;
    private String checklistAccesorios;
    private String orientacionRecibida;
    
   /**
     * Constructor principal para crear un nuevo préstamo de equipo.
     * 
     * @param id Identificador único del préstamo
     * @param idUsuario ID del usuario que recibe el equipo
     * @param idEquipo ID del equipo que se presta
     * @param fechaPrestamo Fecha y hora del préstamo (si es null, usa fecha actual)
     * @param lugarEntrega Lugar donde se entrega el equipo
     * @param fechaDevolucion Fecha estimada de devolución
     * @param estado Estado inicial del préstamo (si es null, se establece como "prestado")
     */    
    
    public prestamoEquipo(int id, int idUsuario, int idEquipo, LocalDateTime fechaPrestamo,String lugarEntrega, LocalDateTime fechaDevolucion, String estado){
        this.id = id;
        this.idUsuario = idUsuario;
        this.idEquipo = idEquipo;
        this.fechaPrestamo = LocalDateTime.now();
        this.lugarEntrega = lugarEntrega;
        this.estado = "prestado";
        this.acuerdoFirmado = false;
        this.observaciones = "";
        this.fechaDevolucion = fechaDevolucion;
        
    }
    
     // Métodos de operación del préstamo
    
    /**
     * Registra la orientación recibida por el usuario y añade una observación con fecha.
     * 
     * @param orientacion Descripción de la orientación proporcionada
     */
    //Metodo de instancia para el proceso del prestamo
    public void registrarOrientacion(String orientacion){
        this.orientacionRecibida = orientacion ;
        this.observaciones += "orientacion: " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + "\n";
    }
    
    /**
     * Marca el acuerdo como firmado y registra la fecha del evento.
     */
    public void firmarAcuerdo(){
        this.acuerdoFirmado = true;
        this.observaciones += "Acuerdo firmado: " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + "\n";
    }
    
    public void registrarChecklist(String accesorios) {
        this.checklistAccesorios = accesorios;
        this.observaciones += "Checklist: " + accesorios + "\n";
    }
    
    /**
     * Este es importante ya que procesa la devolucion del equipo, actualizando estado, fecha y observaciones.
     * 
     * @param estadoEquipo condicion cuando se devuelve el equipo
     * @param observaciones nota adicional sobre la devolucion
     */
    public void registrarDevolucion(String estadoEquipo, String observaciones) {
        this.fechaDevolucion = LocalDateTime.now();
        this.estado = "Devuelto";
        this.observaciones += "Devolución - Estado: " + estadoEquipo + "\n" + observaciones;
    }
    //aca se fija el prestamo como retrasado y registra la fecha del evento.
    public void marcarRetraso() {
        this.estado = "Retrasado";
        this.observaciones += "Préstamo marcado como retrasado: " + LocalDateTime.now() + "\n";
    }
   
    public String getLugarEntrega() {
        return lugarEntrega;
    }    

    public boolean isAcuerdoFirmado() {
        return acuerdoFirmado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public String getChecklistAccesorios() {
        return checklistAccesorios;
    }

    /**
     * Tipo de método:
    Público (accesible desde otras clases)
    De instancia (opera con los atributos del objeto)
    Retorna un String
    Qué hace:
    Crea una cadena estructurada con los detalles clave del préstamo
    Usa String.format() para construir el texto de manera limpia
    Incluye conversión condicional de booleanos a texto ("Sí"/"No")
     * @return
     */
    public String getOrientacionRecibida() {
        return orientacionRecibida;
    }

    public void setLugarEntrega(String lugarEntrega) {
        this.lugarEntrega = lugarEntrega;
    }

    public void setAcuerdoFirmado(boolean acuerdoFirmado) {
        this.acuerdoFirmado = acuerdoFirmado;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setChecklistAccesorios(String checklistAccesorios) {
        this.checklistAccesorios = checklistAccesorios;
    }

    public void setOrientacionRecibida(String orientacionRecibida) {
        this.orientacionRecibida = orientacionRecibida;
    }

    public String getDetallesPrestamo() {
        return String.format(
                "Préstamo #%d\nEquipo: %d\nUsuario: %d\nEstado: %s\nLugar: %s\nFirma: %s",
                id, idEquipo, idUsuario, estado, lugarEntrega, acuerdoFirmado ? "Sí" : "No"
        );
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

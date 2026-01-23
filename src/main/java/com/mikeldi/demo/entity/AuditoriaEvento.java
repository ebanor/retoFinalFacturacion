package com.mikeldi.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "auditoriaevento")
public class AuditoriaEvento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String entidad;
    
    @Column(name = "entidadId", nullable = false)
    private Long entidadId;
    
    @Column(nullable = false)
    private String accion;
    
    @Column(name = "usuarioId", nullable = false)
    private Long usuarioId;
    
    @Column(nullable = false)
    private String fecha;
    
    @Column(name = "detalleCambios", columnDefinition = "TEXT")
    private String detalleCambios;
    
    public AuditoriaEvento() {}
    
    public AuditoriaEvento(String entidad, Long entidadId, String accion, Long usuarioId, String fecha, String detalleCambios) {
        this.entidad = entidad;
        this.entidadId = entidadId;
        this.accion = accion;
        this.usuarioId = usuarioId;
        this.fecha = fecha;
        this.detalleCambios = detalleCambios;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getEntidad() { return entidad; }
    public void setEntidad(String entidad) { this.entidad = entidad; }
    
    public Long getEntidadId() { return entidadId; }
    public void setEntidadId(Long entidadId) { this.entidadId = entidadId; }
    
    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }
    
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    
    public String getDetalleCambios() { return detalleCambios; }
    public void setDetalleCambios(String detalleCambios) { this.detalleCambios = detalleCambios; }
}

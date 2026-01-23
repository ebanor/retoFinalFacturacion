package com.mikeldi.demo.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cobropago")
public class CobroPago {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "facturaId", nullable = false)
    private Long facturaId;
    
    @Column(nullable = false)
    private String fecha;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal importe;
    
    @Column(name = "metodoPagoId", nullable = false)
    private Long metodoPagoId;
    
    @Column(name = "referenciaBancaria")
    private String referenciaBancaria;
    
    @Column(name = "estadoCobro", nullable = false)
    private String estadoCobro;
    
    @Column(name = "usuarioRegistroId")
    private Long usuarioRegistroId;
    
    public CobroPago() {}
    
    public CobroPago(Long facturaId, String fecha, BigDecimal importe, Long metodoPagoId,
                    String referenciaBancaria, String estadoCobro, Long usuarioRegistroId) {
        this.facturaId = facturaId;
        this.fecha = fecha;
        this.importe = importe;
        this.metodoPagoId = metodoPagoId;
        this.referenciaBancaria = referenciaBancaria;
        this.estadoCobro = estadoCobro;
        this.usuarioRegistroId = usuarioRegistroId;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getFacturaId() { return facturaId; }
    public void setFacturaId(Long facturaId) { this.facturaId = facturaId; }
    
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    
    public BigDecimal getImporte() { return importe; }
    public void setImporte(BigDecimal importe) { this.importe = importe; }
    
    public Long getMetodoPagoId() { return metodoPagoId; }
    public void setMetodoPagoId(Long metodoPagoId) { this.metodoPagoId = metodoPagoId; }
    
    public String getReferenciaBancaria() { return referenciaBancaria; }
    public void setReferenciaBancaria(String referenciaBancaria) { this.referenciaBancaria = referenciaBancaria; }
    
    public String getEstadoCobro() { return estadoCobro; }
    public void setEstadoCobro(String estadoCobro) { this.estadoCobro = estadoCobro; }
    
    public Long getUsuarioRegistroId() { return usuarioRegistroId; }
    public void setUsuarioRegistroId(Long usuarioRegistroId) { this.usuarioRegistroId = usuarioRegistroId; }
}

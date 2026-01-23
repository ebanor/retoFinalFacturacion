package com.mikeldi.demo.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "facturaventa")
public class FacturaVenta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "numeroFactura", nullable = false, unique = true)
    private String numeroFactura;
    
    @Column(nullable = false)
    private String serie;
    
    @Column(name = "pedidoId")
    private Long pedidoId;
    
    @Column(name = "clienteId", nullable = false)
    private Long clienteId;
    
    @Column(name = "fechaEmision", nullable = false)
    private String fechaEmision;
    
    @Column(name = "fechaVencimiento")
    private String fechaVencimiento;
    
    @Column(name = "estadoPago", nullable = false)
    private String estadoPago;
    
    @Column(name = "baseImponible", nullable = false, precision = 10, scale = 2)
    private BigDecimal baseImponible;
    
    @Column(name = "ivaTotal", nullable = false, precision = 10, scale = 2)
    private BigDecimal ivaTotal;
    
    @Column(name = "totalFactura", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalFactura;
    
    @Column(nullable = false)
    private String moneda;
    
    @Column(name = "usuarioEmisorId")
    private Long usuarioEmisorId;
    
    public FacturaVenta() {}
    
    public FacturaVenta(String numeroFactura, String serie, Long pedidoId, Long clienteId, 
                       String fechaEmision, String fechaVencimiento, String estadoPago,
                       BigDecimal baseImponible, BigDecimal ivaTotal, BigDecimal totalFactura,
                       String moneda, Long usuarioEmisorId) {
        this.numeroFactura = numeroFactura;
        this.serie = serie;
        this.pedidoId = pedidoId;
        this.clienteId = clienteId;
        this.fechaEmision = fechaEmision;
        this.fechaVencimiento = fechaVencimiento;
        this.estadoPago = estadoPago;
        this.baseImponible = baseImponible;
        this.ivaTotal = ivaTotal;
        this.totalFactura = totalFactura;
        this.moneda = moneda;
        this.usuarioEmisorId = usuarioEmisorId;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }
    
    public String getSerie() { return serie; }
    public void setSerie(String serie) { this.serie = serie; }
    
    public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }
    
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    
    public String getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(String fechaEmision) { this.fechaEmision = fechaEmision; }
    
    public String getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(String fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }
    
    public String getEstadoPago() { return estadoPago; }
    public void setEstadoPago(String estadoPago) { this.estadoPago = estadoPago; }
    
    public BigDecimal getBaseImponible() { return baseImponible; }
    public void setBaseImponible(BigDecimal baseImponible) { this.baseImponible = baseImponible; }
    
    public BigDecimal getIvaTotal() { return ivaTotal; }
    public void setIvaTotal(BigDecimal ivaTotal) { this.ivaTotal = ivaTotal; }
    
    public BigDecimal getTotalFactura() { return totalFactura; }
    public void setTotalFactura(BigDecimal totalFactura) { this.totalFactura = totalFactura; }
    
    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }
    
    public Long getUsuarioEmisorId() { return usuarioEmisorId; }
    public void setUsuarioEmisorId(Long usuarioEmisorId) { this.usuarioEmisorId = usuarioEmisorId; }
}

package com.mikeldi.demo.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "lineafactura")
public class LineaFactura {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "facturaId", nullable = false)
    private Long facturaId;
    
    @Column(name = "productoId")
    private Long productoId;
    
    @Column(nullable = false)
    private String descripcion;
    
    @Column(nullable = false)
    private Integer cantidad;
    
    @Column(name = "precioUnitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;
    
    @Column(precision = 5, scale = 2)
    private BigDecimal descuento;
    
    @Column(name = "tipoIva", nullable = false)
    private String tipoIva;
    
    @Column(name = "cuotaIva", nullable = false, precision = 10, scale = 2)
    private BigDecimal cuotaIva;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;
    
    public LineaFactura() {}
    
    public LineaFactura(Long facturaId, Long productoId, String descripcion, Integer cantidad,
                       BigDecimal precioUnitario, BigDecimal descuento, String tipoIva,
                       BigDecimal cuotaIva, BigDecimal subtotal) {
        this.facturaId = facturaId;
        this.productoId = productoId;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.descuento = descuento;
        this.tipoIva = tipoIva;
        this.cuotaIva = cuotaIva;
        this.subtotal = subtotal;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getFacturaId() { return facturaId; }
    public void setFacturaId(Long facturaId) { this.facturaId = facturaId; }
    
    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario; }
    
    public BigDecimal getDescuento() { return descuento; }
    public void setDescuento(BigDecimal descuento) { this.descuento = descuento; }
    
    public String getTipoIva() { return tipoIva; }
    public void setTipoIva(String tipoIva) { this.tipoIva = tipoIva; }
    
    public BigDecimal getCuotaIva() { return cuotaIva; }
    public void setCuotaIva(BigDecimal cuotaIva) { this.cuotaIva = cuotaIva; }
    
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}

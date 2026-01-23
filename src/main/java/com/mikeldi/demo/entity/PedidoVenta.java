package com.mikeldi.demo.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "pedidoventa")
public class PedidoVenta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "numeroPedido", nullable = false, unique = true)
    private String numeroPedido;
    
    @Column(name = "clienteId", nullable = false)
    private Long clienteId;
    
    @Column(name = "fechaCreacion", nullable = false)
    private String fechaCreacion;
    
    @Column(nullable = false)
    private String estado;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;
    
    @Column(nullable = false)
    private String moneda;
    
    public PedidoVenta() {}
    
    public PedidoVenta(String numeroPedido, Long clienteId, String fechaCreacion, String estado, BigDecimal total, String moneda) {
        this.numeroPedido = numeroPedido;
        this.clienteId = clienteId;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
        this.total = total;
        this.moneda = moneda;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNumeroPedido() { return numeroPedido; }
    public void setNumeroPedido(String numeroPedido) { this.numeroPedido = numeroPedido; }
    
    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    
    public String getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    
    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }
}

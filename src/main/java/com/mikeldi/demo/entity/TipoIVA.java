package com.mikeldi.demo.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tipoiva")
public class TipoIVA {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String nombre;
    
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal porcentaje;
    
    @Column(name = "vigenciaDesde")
    private String vigenciaDesde;
    
    @Column(name = "vigenciaHasta")
    private String vigenciaHasta;
    
    public TipoIVA() {}
    
    public TipoIVA(String nombre, BigDecimal porcentaje, String vigenciaDesde, String vigenciaHasta) {
        this.nombre = nombre;
        this.porcentaje = porcentaje;
        this.vigenciaDesde = vigenciaDesde;
        this.vigenciaHasta = vigenciaHasta;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public BigDecimal getPorcentaje() { return porcentaje; }
    public void setPorcentaje(BigDecimal porcentaje) { this.porcentaje = porcentaje; }
    
    public String getVigenciaDesde() { return vigenciaDesde; }
    public void setVigenciaDesde(String vigenciaDesde) { this.vigenciaDesde = vigenciaDesde; }
    
    public String getVigenciaHasta() { return vigenciaHasta; }
    public void setVigenciaHasta(String vigenciaHasta) { this.vigenciaHasta = vigenciaHasta; }
}

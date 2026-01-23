package com.mikeldi.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cliente")
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nombreFiscal", nullable = false)
    private String nombreFiscal;
    
    @Column(name = "cifNif", unique = true)
    private String cifNif;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(name = "direccionFacturacion")
    private String direccionFacturacion;
    
    @Column(name = "formaPago")
    private String formaPago;
    
    @Column(name = "plazosPago")
    private Integer plazosPago;
    
    @Column(nullable = false)
    private Boolean activo = true;
    
    public Cliente() {}
    
    public Cliente(String nombreFiscal, String cifNif, String email, String direccionFacturacion, String formaPago, Integer plazosPago) {
        this.nombreFiscal = nombreFiscal;
        this.cifNif = cifNif;
        this.email = email;
        this.direccionFacturacion = direccionFacturacion;
        this.formaPago = formaPago;
        this.plazosPago = plazosPago;
        this.activo = true;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombreFiscal() { return nombreFiscal; }
    public void setNombreFiscal(String nombreFiscal) { this.nombreFiscal = nombreFiscal; }
    
    public String getCifNif() { return cifNif; }
    public void setCifNif(String cifNif) { this.cifNif = cifNif; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getDireccionFacturacion() { return direccionFacturacion; }
    public void setDireccionFacturacion(String direccionFacturacion) { this.direccionFacturacion = direccionFacturacion; }
    
    public String getFormaPago() { return formaPago; }
    public void setFormaPago(String formaPago) { this.formaPago = formaPago; }
    
    public Integer getPlazosPago() { return plazosPago; }
    public void setPlazosPago(Integer plazosPago) { this.plazosPago = plazosPago; }
    
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}

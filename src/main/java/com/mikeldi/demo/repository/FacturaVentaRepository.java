package com.mikeldi.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mikeldi.demo.entity.FacturaVenta;

@Repository
public interface FacturaVentaRepository extends JpaRepository<FacturaVenta, Long> {
    boolean existsByNumeroFactura(String numeroFactura);
}

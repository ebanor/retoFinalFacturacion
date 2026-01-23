package com.mikeldi.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mikeldi.demo.entity.LineaFactura;

@Repository
public interface LineaFacturaRepository extends JpaRepository<LineaFactura, Long> {
    // No necesita validaciones de duplicados específicas
}

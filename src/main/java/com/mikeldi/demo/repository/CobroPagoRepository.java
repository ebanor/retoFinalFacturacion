package com.mikeldi.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mikeldi.demo.entity.CobroPago;

@Repository
public interface CobroPagoRepository extends JpaRepository<CobroPago, Long> {
    // No necesita validaciones de duplicados específicas
}

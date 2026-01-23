package com.mikeldi.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mikeldi.demo.entity.AuditoriaEvento;

@Repository
public interface AuditoriaEventoRepository extends JpaRepository<AuditoriaEvento, Long> {
    // No necesita validaciones de duplicados
}

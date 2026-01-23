package com.mikeldi.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mikeldi.demo.entity.TipoIVA;

@Repository
public interface TipoIVARepository extends JpaRepository<TipoIVA, Long> {
    boolean existsByNombre(String nombre);
}

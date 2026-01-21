package com.mikeldi.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mikeldi.demo.entity.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    // JpaRepository ya incluye métodos como:
    // - save(cliente)
    // - findAll()
    // - findById(id)
    // - delete(cliente)
    // - count()
    
    // Puedes agregar métodos personalizados si necesitas:
    boolean existsByEmail(String email);
}

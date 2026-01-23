package com.mikeldi.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mikeldi.demo.entity.PedidoVenta;

@Repository
public interface PedidoVentaRepository extends JpaRepository<PedidoVenta, Long> {
    boolean existsByNumeroPedido(String numeroPedido);
}

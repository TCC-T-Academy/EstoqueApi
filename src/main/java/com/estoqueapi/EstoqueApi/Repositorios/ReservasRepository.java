package com.estoqueapi.EstoqueApi.Repositorios;

import com.estoqueapi.EstoqueApi.Entidades.Reservas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservasRepository extends JpaRepository<Reservas, Long> {

}

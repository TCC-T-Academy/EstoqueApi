package com.estoqueapi.EstoqueApi.Repositorios;

import com.estoqueapi.EstoqueApi.Entidades.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    @Query("select r from Reserva r where item.idItem = ?1")
    List<Reserva> ConsultarByIdItem(Long idItem);

    @Query("select r from Reserva r where item.idItem = ?1 and r.finalizada=false")
    List<Reserva> consultarPendentesByIdItem(Long idItem);

    @Query("select r from Reserva r order by r.dataPrevista desc")
    List<Reserva> findAllOrderByDesc();

    List<Reserva> findByOrdem(String ordem);
}
package com.estoqueapi.EstoqueApi.Repositorios;

import com.estoqueapi.EstoqueApi.Entidades.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    @Query("select r from Reserva r where item.idItem = ?1")
    List<Reserva> ConsultarByIdItem(Long idItem);

    @Query("select r from Reserva r where item.idItem = ?1 and r.finalizada=false")
    List<Reserva> consultarPendentesByIdItem(Long idItem);

    @Query(value = "select * from reserva where data_prevista < localtime() and finalizada = ?1 order by data_prevista asc;", nativeQuery = true)
    List<Reserva> findByDataPrevistaVencidos(Boolean finalizada);
    @Query(value = "select * from reserva where finalizada = ?1 order by data_prevista asc;", nativeQuery = true)
    List<Reserva> findByDataPrevistaAVencer(Boolean finalizada);

}
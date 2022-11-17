package com.estoqueapi.EstoqueApi.Repositorios;

import com.estoqueapi.EstoqueApi.Entidades.Reservas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservasRepository extends JpaRepository<Reservas, Long> {
    @Query("select r from Reservas r where item.idItem = ?1")
    List<Reservas> ConsultarByIdItem(Long idItem);

    @Query(value = "SELECT * FROM reservas WHERE DATE_FORMAT(data_prevista, '%Y %m %d') < DATE_FORMAT(now(), '%Y %m %d')", nativeQuery = true)
    List<Reservas> findByDataPrevistaVencidos();
    @Query(value = "SELECT * FROM reservas WHERE DATE_FORMAT(data_prevista, '%Y %m %d') >= DATE_FORMAT(now(), '%Y %m %d')", nativeQuery = true)
    List<Reservas> findByDataPrevistaAVencer();

}
package com.estoqueapi.EstoqueApi.Repositorios;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.estoqueapi.EstoqueApi.Entidades.Previsoes;

import java.sql.Date;
import java.util.List;

@Repository
public interface PrevisoesRepository extends CrudRepository<Previsoes, Long> {

    List<Previsoes> findByFinalizada(boolean finalizada);
    @Query(value = "SELECT * FROM previsoes WHERE DATE_FORMAT(data_prevista, '%Y %m %d') < DATE_FORMAT(now(), '%Y %m %d')", nativeQuery = true)
    List<Previsoes> findByDataPrevistaVencidos();
    @Query(value = "SELECT * FROM previsoes WHERE DATE_FORMAT(data_prevista, '%Y %m %d') >= DATE_FORMAT(now(), '%Y %m %d')", nativeQuery = true)
    List<Previsoes> findByDataPrevistaAVencer();

    @Query(value = "SELECT * FROM previsoes WHERE DATE_FORMAT(data_prevista, '%Y %m %d') < DATE_FORMAT(now(), '%Y %m %d') AND finalizada = :realizada", nativeQuery = true)
    List<Previsoes> findByDataPrevistaMenorFinalizada(boolean realizada);

    @Query(value = "SELECT * FROM previsoes WHERE DATE_FORMAT(data_prevista, '%Y %m %d') >= DATE_FORMAT(now(), '%Y %m %d') AND finalizada = :realizada", nativeQuery = true)
    List<Previsoes> findByDataPrevistaMaiorIgualFinalizada(boolean realizada);
}


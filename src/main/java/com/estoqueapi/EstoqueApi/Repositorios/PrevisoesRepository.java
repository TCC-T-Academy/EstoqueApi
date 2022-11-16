package com.estoqueapi.EstoqueApi.Repositorios;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.estoqueapi.EstoqueApi.Entidades.Previsoes;

import java.util.Date;
import java.util.List;

@Repository
public interface PrevisoesRepository extends CrudRepository<Previsoes, Long> {

    List<Previsoes> findByFinalizada(boolean finalizada);

    List<Previsoes> findBydataPrevista(Date dataPrevista);
}

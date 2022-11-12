package com.estoqueapi.EstoqueApi.Repositorios;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.estoqueapi.EstoqueApi.Entidades.Previsoes;

@Repository
public interface PrevisoesRepository extends CrudRepository<Previsoes, Long> {
}

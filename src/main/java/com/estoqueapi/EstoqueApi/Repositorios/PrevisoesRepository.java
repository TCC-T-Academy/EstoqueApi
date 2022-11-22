package com.estoqueapi.EstoqueApi.Repositorios;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.estoqueapi.EstoqueApi.Entidades.Previsoes;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrevisoesRepository extends CrudRepository<Previsoes, Long> {

    List<Previsoes> findByFinalizada(boolean finalizada);

    // Retorna todas as previsões selecionadas por um item
    @Query(value = "SELECT * from previsoes LEFT JOIN itens ON previsoes.item_id_Item = itens.id_Item WHERE itens.id_Item = :idItem", nativeQuery = true)
    List<Previsoes> ConsultarByIdItem(Long idItem);
    @Query(value = "SELECT * from previsoes LEFT JOIN itens ON previsoes.item_id_Item = itens.id_Item WHERE itens.id_Item = :idItem AND finalizada = 0", nativeQuery = true)
    List<Previsoes> ConsultarPendentesByIdItem(Long idItem);

    // Retorna todas as previsões com data anterior a de hoje, com a opção de escolha se ela estiver finalizada ou não
    @Query(value = "SELECT * FROM previsoes WHERE DATE_FORMAT(data_prevista, '%Y %m %d') < DATE_FORMAT(now(), '%Y %m %d')", nativeQuery = true)
    List<Previsoes> findByDataPrevistaVencidos();

    // Retorna todas as previsões com data a partir de hoje, com a opção de escolha se ela estiver finalizada ou não
    @Query(value = "SELECT * FROM previsoes WHERE DATE_FORMAT(data_prevista, '%Y %m %d') >= DATE_FORMAT(now(), '%Y %m %d')", nativeQuery = true)
    List<Previsoes> findByDataPrevistaAVencer();

    @Query(value = "SELECT * FROM previsoes WHERE DATE_FORMAT(data_prevista, '%Y %m %d') < DATE_FORMAT(now(), '%Y %m %d') AND finalizada = :realizada", nativeQuery = true)
    List<Previsoes> findByDataPrevistaMenorFinalizada(boolean realizada);

    @Query(value = "SELECT * FROM previsoes WHERE DATE_FORMAT(data_prevista, '%Y %m %d') >= DATE_FORMAT(now(), '%Y %m %d') AND finalizada = :realizada", nativeQuery = true)
    List<Previsoes> findByDataPrevistaMaiorIgualFinalizada(boolean realizada);

    Optional<Previsoes> findByOrdem(String ordem);
}


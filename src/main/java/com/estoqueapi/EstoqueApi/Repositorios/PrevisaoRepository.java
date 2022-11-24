package com.estoqueapi.EstoqueApi.Repositorios;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.estoqueapi.EstoqueApi.Entidades.Previsao;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrevisaoRepository extends CrudRepository<Previsao, Long> {

    List<Previsao> findByFinalizada(boolean finalizada);

    // Retorna todas as previsões selecionadas por um item
    @Query(value = "SELECT * from previsao LEFT JOIN item ON previsao.item_id_Item = item.id_Item WHERE item.id_Item = :idItem", nativeQuery = true)
    List<Previsao> ConsultarByIdItem(Long idItem);
    @Query(value = "SELECT * from previsao LEFT JOIN item ON previsao.item_id_Item = item.id_Item WHERE item.id_Item = :idItem AND finalizada = 0", nativeQuery = true)
    List<Previsao> ConsultarPendentesByIdItem(Long idItem);

    // Retorna todas as previsões com data anterior a de hoje, com a opção de escolha se ela estiver finalizada ou não
    @Query(value = "SELECT * FROM previsao WHERE DATE_FORMAT(data_prevista, '%Y %m %d') < DATE_FORMAT(now(), '%Y %m %d')", nativeQuery = true)
    List<Previsao> findByDataPrevistaVencidos();

    // Retorna todas as previsões com data a partir de hoje, com a opção de escolha se ela estiver finalizada ou não
    @Query(value = "SELECT * FROM previsao WHERE DATE_FORMAT(data_prevista, '%Y %m %d') >= DATE_FORMAT(now(), '%Y %m %d')", nativeQuery = true)
    List<Previsao> findByDataPrevistaAVencer();

    @Query(value = "SELECT * FROM previsao WHERE DATE_FORMAT(data_prevista, '%Y %m %d') < DATE_FORMAT(now(), '%Y %m %d') AND finalizada = :realizada", nativeQuery = true)
    List<Previsao> findByDataPrevistaMenorFinalizada(boolean realizada);

    @Query(value = "SELECT * FROM previsao WHERE DATE_FORMAT(data_prevista, '%Y %m %d') >= DATE_FORMAT(now(), '%Y %m %d') AND finalizada = :realizada", nativeQuery = true)
    List<Previsao> findByDataPrevistaMaiorIgualFinalizada(boolean realizada);

    Optional<Previsao> findByOrdem(String ordem);
}


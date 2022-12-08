package com.estoqueapi.EstoqueApi.Repositorios;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.estoqueapi.EstoqueApi.Entidades.Previsao;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrevisaoRepository extends CrudRepository<Previsao, Long> {

    // Retorna todas as previsões selecionadas por um item
    @Query(value = "SELECT * from previsao LEFT JOIN item ON previsao.item_id_Item = item.id_Item WHERE item.id_Item = :idItem", nativeQuery = true)
    List<Previsao> ConsultarByIdItem(Long idItem);

    @Query(value = "SELECT * from previsao LEFT JOIN item ON previsao.item_id_Item = item.id_Item WHERE item.id_Item = :idItem AND finalizada = 0", nativeQuery = true)
    List<Previsao> ConsultarPendentesByIdItem(Long idItem);

    List<Previsao> findByOrdem(String ordem);
}


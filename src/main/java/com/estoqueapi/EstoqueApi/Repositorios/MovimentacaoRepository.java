package com.estoqueapi.EstoqueApi.Repositorios;

import com.estoqueapi.EstoqueApi.Entidades.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao,Long> {
    @Query("Select m from Movimentacao m where item.idItem = ?1 order by m.idMovimentacao desc")
    List<Movimentacao> findAllByIdItem(Long idItem);
}

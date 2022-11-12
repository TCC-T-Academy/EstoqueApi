package com.estoqueapi.EstoqueApi.Repositorios;

import com.estoqueapi.EstoqueApi.Entidades.Movimentacoes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimentacoesRepository extends JpaRepository<Movimentacoes,Long> {
    @Query("Select m from Movimentacoes m where item.idItem = ?1 order by m.idMovimentacao desc")
    List<Movimentacoes> findAllByIdItem(Long idItem);
}

package com.estoqueapi.EstoqueApi.Repositorios;

import com.estoqueapi.EstoqueApi.Entidades.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque,Long> {

    @Query("select e from Estoque e where e.item.idItem = ?1")
    Optional<Estoque> findByIdItem(long idItem);
}

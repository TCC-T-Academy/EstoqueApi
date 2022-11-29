package com.estoqueapi.EstoqueApi.Repositorios;

import com.estoqueapi.EstoqueApi.Entidades.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque,Long> {

    @Query("select e from Estoque e where e.item.idItem = ?1")
    Optional<Estoque> findByIdItem(long idItem);

    @Query(value = "select * from estoque as e inner join item as i " +
                        "on e.item_id_item = i.id_item " +
                    "where e.estoque_real <= i.estoque_seguranca" ,nativeQuery = true)
    List<Estoque> findItensAbaixoEstoque();
}

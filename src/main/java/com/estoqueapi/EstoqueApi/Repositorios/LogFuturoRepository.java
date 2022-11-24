package com.estoqueapi.EstoqueApi.Repositorios;

import com.estoqueapi.EstoqueApi.Entidades.LogFuturo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogFuturoRepository extends JpaRepository<LogFuturo, Long> {
    @Query("SELECT l FROM LogFuturo l WHERE l.idItem = ?1 ORDER BY l.data")
    List<LogFuturo> findLogByIdItem(long idItem);

}

package com.estoqueapi.EstoqueApi.Repositorios;

import com.estoqueapi.EstoqueApi.Entidades.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuarios,Long> {
    Optional<Usuarios> findByEmail(String email);

    @Query(value = "select count(*) from Usuarios where usuarios.email = :email ", nativeQuery = true)
    Long existsByEmail(String email);
}

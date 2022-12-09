package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.Role;
import com.estoqueapi.EstoqueApi.Repositorios.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Role consultarByAuthority(String authority){
        return roleRepository.findByAuthority(authority).orElseThrow(() -> new EntityNotFoundException("Role não encontrada"));
    }
}

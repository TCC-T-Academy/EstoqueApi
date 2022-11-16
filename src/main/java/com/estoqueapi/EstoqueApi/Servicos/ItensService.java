package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.Itens;
import com.estoqueapi.EstoqueApi.Repositorios.ItensRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class ItensService {

    @Autowired
    private ItensRepository itensRepository;

    public List<Itens> listarItens(){
        return itensRepository.findAll();
    }

    public Itens consultarItemById(long idItem) {
        Itens item = itensRepository.findById(idItem).orElseThrow(() -> new EntityNotFoundException("Item nao encontrado"));
        return item;
    }
}

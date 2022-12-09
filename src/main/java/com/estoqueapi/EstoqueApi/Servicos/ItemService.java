package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.Item;
import com.estoqueapi.EstoqueApi.Repositorios.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public List<Item> listarItens(){
        return itemRepository.findAll();
    }

    public Item consultarItemById(long idItem) {
        Item item = itemRepository.findById(idItem).orElseThrow(() -> new EntityNotFoundException("Item nao encontrado"));
        return item;
    }
}

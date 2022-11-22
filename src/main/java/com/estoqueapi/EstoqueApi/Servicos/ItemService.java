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

    public Item salvar(Item item){
        return itemRepository.save(item);
    }

    public Item alterarItem(long idItem, Item item){

        Item itemAlterado = this.consultarItemById(idItem);

        if(!item.getUnidade().isBlank() && !item.getUnidade().isEmpty()){
            itemAlterado.setUnidade(item.getUnidade());
        }

        if(!item.getGrupo().isBlank() && !item.getGrupo().isEmpty()){
            itemAlterado.setGrupo(item.getGrupo());
        }

        if(item.getEstoqueSeguranca() > 0){
            itemAlterado.setEstoqueSeguranca(item.getEstoqueSeguranca());
        }

        if(!item.getFamilia().isBlank() && !item.getFamilia().isEmpty()){
            itemAlterado.setFamilia(item.getFamilia());
        }

        if(!item.getDescricao().isBlank() && !item.getDescricao().isEmpty()){
            itemAlterado.setDescricao(item.getDescricao());
        }

        return this.salvar(itemAlterado);
    }

}

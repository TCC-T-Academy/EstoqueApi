package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.Estoque;
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

    public Itens salvar(Itens item){
        return itensRepository.save(item);
    }

    public Itens alterarItem(long idItem, Itens item){

        Itens itemAlterado = this.consultarItemById(idItem);

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

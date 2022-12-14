package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.Estoque;
import com.estoqueapi.EstoqueApi.Entidades.Item;
import com.estoqueapi.EstoqueApi.Entidades.LogFuturo;
import com.estoqueapi.EstoqueApi.Exceptions.AcaoNaoPermitidaException;
import com.estoqueapi.EstoqueApi.Exceptions.MovimentacaoInvalidaException;
import com.estoqueapi.EstoqueApi.Repositorios.EstoqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

@Service
public class EstoqueService {

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private ItemService itemService;

    public List<Estoque> listarEstoque(){ return estoqueRepository.findAll(); }

    public Estoque buscarEstoqueIdItem(long idItem){
        Estoque estoque = estoqueRepository.findByIdItem(idItem).orElseThrow(()-> new EntityNotFoundException("Item nao encontrado no estoque."));
        return estoque;
    }

    public Estoque adicionarEstoque(long idItem, float qtd) {
        Estoque estoque = this.buscarEstoqueIdItem(idItem);
        if(qtd <= 0){
            throw new MovimentacaoInvalidaException("Quantidade Invalida");
        }
        estoque.setEstoqueReal(estoque.getEstoqueReal() + qtd);
        return estoqueRepository.save(estoque);
    }

    public Estoque subtrairEstoque(long idItem, float qtd) {
        Estoque estoque = this.buscarEstoqueIdItem(idItem);
        if(qtd <= 0){
            throw new MovimentacaoInvalidaException("Quantidade Invalida");
        }else if(qtd > estoque.getEstoqueReal()){
            throw new MovimentacaoInvalidaException("Quantidade Indisponível");
        }
        estoque.setEstoqueReal(estoque.getEstoqueReal() - qtd);
        return estoqueRepository.save(estoque);
    }

    public Estoque buscarEstoqueById(long idEstoque) {
        Estoque estoque = estoqueRepository.findById(idEstoque).orElseThrow(() -> new EntityNotFoundException("Estoque nao encontrado"));
        return estoque;
    }

    public Estoque salvar(Estoque estoque){
        return this.validarEstoque(estoque);
    }

    /*public Estoque alterarEstoque(long idEstoque, Estoque estoque){
        Estoque estoqueAlterado = this.buscarEstoqueById(idEstoque);

        if(estoque.getEstoqueReal() > 0){
            estoqueAlterado.setEstoqueReal(estoque.getEstoqueReal());
        }
        if(!estoque.getLocalizacao().isBlank() && !estoque.getLocalizacao().isEmpty()){
            estoqueAlterado.setLocalizacao(estoque.getLocalizacao());
        }
        if(estoque.getItem().getIdItem() > 0){
            estoqueAlterado.setItem(itemService.consultarItemById(estoque.getItem().getIdItem()));
        }
        return this.salvar(estoqueAlterado);
    }*/

    public Estoque validarEstoque(Estoque e){
        Item item = itemService.consultarItemById(e.getItem().getIdItem());
        e.setItem(item);
        if(e.getEstoqueReal() <= 0){
            throw new AcaoNaoPermitidaException("Estoque menor ou igual a zero");
        }else if(e.getLocalizacao().isEmpty() || e.getLocalizacao().isBlank()){
            throw new AcaoNaoPermitidaException("Localizacao Invalida");
        }
        return e;
    }

    public List<Estoque> consultarEstoqueAbaixoLimite (){
        List<Estoque> list = estoqueRepository.findItensAbaixoEstoque();
        if(list.size() == 0){
            throw new EntityNotFoundException("Lista nao encontrada");
        }
        return list;
    }

    public Estoque atualizarEstoqueFuturo(long idItem, List<LogFuturo> log){
        Float quantidade;
        LocalDate data;
        Estoque e = this.buscarEstoqueIdItem(idItem);

        if(log.size() > 0) {
            quantidade = log.get(log.size() - 1).getEstoqueMomento();
            data = log.get(log.size() - 1).getData();
            e.setDataFutura(data);
            e.setEstoqueFuturo(quantidade);
        }else{
            e.setDataFutura(LocalDate.now());
            e.setEstoqueFuturo(e.getEstoqueReal());
        }

        return estoqueRepository.save(e);
    }
}

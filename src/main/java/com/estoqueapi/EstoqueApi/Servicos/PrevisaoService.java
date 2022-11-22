package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.Previsao;
import com.estoqueapi.EstoqueApi.Repositorios.ItemRepository;
import com.estoqueapi.EstoqueApi.Repositorios.PrevisaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
public class PrevisaoService {

    @Autowired
    private PrevisaoRepository previsaoRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;

    // Listar todas as previsões cadastradas
   public Iterable<Previsao> listarPrevisoes() {
        return previsaoRepository.findAll();
    }

    // Filtrar previsões por id do item
    public List<Previsao> consultarByIdItem(Long idItem) {
        return previsaoRepository.ConsultarByIdItem(idItem);
    }

    //Cadastrar previsões
    @Transactional
    public Previsao cadastrarPrevisoes(Previsao pr){
        return previsaoRepository.save(pr);
    }

    //Filtrar previsão por idPrevisao
    public Previsao filtrarId(long idPrevisao){
        Optional<Previsao> obj = previsaoRepository.findById(idPrevisao);
        Previsao prev = null;
        try{
            prev = obj.get();
        } catch (NoSuchElementException exception) {
            throw new EntityNotFoundException("Previsão não localizada");
        }
        return prev;
    }

    //Filtrar por ordem de compra/produção realizadas ou não
    public List<Previsao> findByFinalizada(boolean finalizada){
        return previsaoRepository.findByFinalizada(finalizada);
    }

    //Filtrar pela data as previsões que venceram e estão a vencer.
    public List<Previsao> findByDataPrevistaVencidos(boolean finalizada) {
        if (finalizada == true) {
            return previsaoRepository.findByDataPrevistaVencidos();
        } else
            return previsaoRepository.findByDataPrevistaAVencer();
    }

        // Lista previsões com opção de escolha tanto com data anterior quanto a partir de hoje e finalizadas ou não
    public List<Previsao> findByDataPrevistaFinalizada(boolean vencimento, boolean finalizada) {
        String venci;
        if (vencimento == true) {
            return previsaoRepository.findByDataPrevistaMenorFinalizada(finalizada);
        } else {
            return previsaoRepository.findByDataPrevistaMaiorIgualFinalizada(finalizada);
        }
    }

    //Alterar previsões - Alterar somente se tiver ativo (não realizado)
    public Previsao alterarPrevisao(Long idPrevisao, Previsao previsao){
        Previsao prev = this.filtrarId(idPrevisao);
        if (!previsao.getFinalizada() == true) {
            prev.setFinalizada(previsao.isFinalizada());
        } else {
            throw new IllegalArgumentException("A previsão não poderá estar ativa");
        }
        if (previsao.getQuantidadePrevista() > 0) {
            prev.setQuantidadePrevista(previsao.getQuantidadePrevista());
        } else {
            throw new IllegalArgumentException("Quantidade inválida");
        }
        if (!previsao.getDataPrevista().equals(null)) {
            prev.setDataPrevista(previsao.getDataPrevista());
        } else {
            throw new IllegalArgumentException("Informe uma data válida");
        }
        if (!previsao.getOrdem().isBlank() && !previsao.getOrdem().isEmpty()) {
            prev.setOrdem(previsao.getOrdem());
        } else {
            throw new IllegalArgumentException("A ordem de compra");
        }
        if (!previsao.getItem().equals(null)) {
            prev.setItem(previsao.getItem());
        } else {
            throw new IllegalArgumentException("Informe o item previsto");
        }
        return this.cadastrarPrevisoes(prev);
    }

    //Excluir previsão (só pode ser excluído quando finalizada = false)
    @Transactional

    public void excluirPrevisao(long idPrevisao){
        Previsao previsao = this.filtrarId(idPrevisao);
        if(previsao.getFinalizada() == false){
            previsaoRepository.delete(previsao);
        }else{
            throw new IllegalArgumentException("Essa ordem já foi realizada, não poderá ser excluída");
        }
    }

    public List<Previsao> consultarPendentesByIdItem(Long idItem) {
        return previsaoRepository.ConsultarPendentesByIdItem(idItem);
    }

    public Previsao validarPrevisoes(Previsao previsao) {
        if (previsao.getFinalizada() == true) {
            throw new IllegalArgumentException("A previsão não poderá estar ativa");
        } else if (previsao.getQuantidadePrevista() <= 0) {
            throw new IllegalArgumentException("Quantidade inválida");
        } else if (previsao.getDataPrevista().equals(null)) {
            throw new IllegalArgumentException("Informe uma data válida");
        } else if (previsao.getOrdem().isBlank() && !previsao.getOrdem().isEmpty()) {
            throw new IllegalArgumentException("A ordem de compra");
        } else if (previsao.getItem().equals(null)) {
            throw new IllegalArgumentException("Informe o item previsto");
        }
        return previsao;
    }

    private boolean previsaoExiste(String ordem) {
        return previsaoRepository.findByOrdem(ordem).orElse(null) != null;
    }

    public Previsao findByOrdem(String ordem){
        return previsaoRepository.findByOrdem(ordem).orElseThrow(() -> new EntityNotFoundException("Ordem não localizada"));
    }

}


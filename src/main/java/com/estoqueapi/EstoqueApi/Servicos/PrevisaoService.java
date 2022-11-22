package com.estoqueapi.EstoqueApi.Servicos;

<<<<<<< HEAD:src/main/java/com/estoqueapi/EstoqueApi/Servicos/PrevisaoService.java
import com.estoqueapi.EstoqueApi.Entidades.Previsao;
import com.estoqueapi.EstoqueApi.Repositorios.PrevisaoRepository;
=======
import com.estoqueapi.EstoqueApi.Entidades.Previsoes;
import com.estoqueapi.EstoqueApi.Repositorios.ItensRepository;
import com.estoqueapi.EstoqueApi.Repositorios.PrevisoesRepository;
>>>>>>> 0fe0bb76251c4aee225eef40d8873c35d58d30e8:src/main/java/com/estoqueapi/EstoqueApi/Servicos/PrevisoesService.java
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
    private ItensRepository itensRepository;

    @Autowired ItensService itensService;

    // Listar todas as previsões cadastradas
<<<<<<< HEAD:src/main/java/com/estoqueapi/EstoqueApi/Servicos/PrevisaoService.java
    public Iterable<Previsao> listarPrevisoes(){
       return previsaoRepository.findAll();
=======
    public Iterable<Previsoes> listarPrevisoes() {
        return previsoesRepository.findAll();
>>>>>>> 0fe0bb76251c4aee225eef40d8873c35d58d30e8:src/main/java/com/estoqueapi/EstoqueApi/Servicos/PrevisoesService.java
    }

    // Filtrar previsões por id do item
<<<<<<< HEAD:src/main/java/com/estoqueapi/EstoqueApi/Servicos/PrevisaoService.java
    public List<Previsao> consultarByIdItem(Long idItem) {
    return previsaoRepository.ConsultarByIdItem(idItem);
=======
    public List<Previsoes> consultarByIdItem(long idItem) {
        return previsoesRepository.ConsultarByIdItem(idItem);
>>>>>>> 0fe0bb76251c4aee225eef40d8873c35d58d30e8:src/main/java/com/estoqueapi/EstoqueApi/Servicos/PrevisoesService.java
    }

    //Cadastrar previsões
    @Transactional
    public Previsao cadastrarPrevisoes(Previsao pr){
        return previsaoRepository.save(pr);
    }

    //Filtrar previsão por idPrevisao
<<<<<<< HEAD:src/main/java/com/estoqueapi/EstoqueApi/Servicos/PrevisaoService.java
    public Previsao filtrarId(long idPrevisao){
        Optional<Previsao> obj = previsaoRepository.findById(idPrevisao);
        Previsao prev = null;
        try{
=======
    public Previsoes filtrarId(long idPrevisao) {
        Optional<Previsoes> obj = previsoesRepository.findById(idPrevisao);
        Previsoes prev = null;
        try {
>>>>>>> 0fe0bb76251c4aee225eef40d8873c35d58d30e8:src/main/java/com/estoqueapi/EstoqueApi/Servicos/PrevisoesService.java
            prev = obj.get();
        } catch (NoSuchElementException exception) {
            throw new EntityNotFoundException("Previsão não localizada");
        }
        return prev;
    }

    //Filtrar por ordem de compra/produção realizadas ou não
<<<<<<< HEAD:src/main/java/com/estoqueapi/EstoqueApi/Servicos/PrevisaoService.java
    public List<Previsao> findByFinalizada(boolean finalizada){
        return previsaoRepository.findByFinalizada(finalizada);
=======
    public List<Previsoes> findByFinalizada(boolean finalizada) {
        return previsoesRepository.findByFinalizada(finalizada);
>>>>>>> 0fe0bb76251c4aee225eef40d8873c35d58d30e8:src/main/java/com/estoqueapi/EstoqueApi/Servicos/PrevisoesService.java
    }

    //Filtrar pela data as previsões que venceram e estão a vencer.
    public List<Previsao> findByDataPrevistaVencidos(boolean finalizada) {
        if (finalizada == true) {
            return previsaoRepository.findByDataPrevistaVencidos();
        } else
            return previsaoRepository.findByDataPrevistaAVencer();
    }

<<<<<<< HEAD:src/main/java/com/estoqueapi/EstoqueApi/Servicos/PrevisaoService.java
        // Lista previsões com opção de escolha tanto com data anterior quanto a partir de hoje e finalizadas ou não
    public List<Previsao> findByDataPrevistaFinalizada(boolean vencimento, boolean finalizada) {
        String venci;
=======
    // Lista previsões com opção de escolha tanto com data anterior quanto a partir de hoje e finalizadas ou não
    public List<Previsoes> findByDataPrevistaFinalizada(boolean vencimento, boolean finalizada) {
>>>>>>> 0fe0bb76251c4aee225eef40d8873c35d58d30e8:src/main/java/com/estoqueapi/EstoqueApi/Servicos/PrevisoesService.java
        if (vencimento == true) {
            return previsaoRepository.findByDataPrevistaMenorFinalizada(finalizada);
        } else {
            return previsaoRepository.findByDataPrevistaMaiorIgualFinalizada(finalizada);
        }
    }

<<<<<<< HEAD:src/main/java/com/estoqueapi/EstoqueApi/Servicos/PrevisaoService.java

    //Alterar previsões - Alterar somente se tiver ativo (não realizado)
    public Previsao alterarPrevisao(Long idPrevisao, Previsao previsao){
        Previsao prev = this.filtrarId(idPrevisao);
        prev.setFinalizada(previsao.isFinalizada());
        prev.setQuantidadePrevista(previsao.getQuantidadePrevista());
        prev.setDataPrevista(previsao.getDataPrevista());
        prev.setOrdem(previsao.getOrdem());
        prev.setItem(prev.getItem());

=======
    //Alterar previsões
    public Previsoes alterarPrevisao(long idPrevisao, Previsoes previsao) {
        Previsoes prev = this.filtrarId(idPrevisao);
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
>>>>>>> 0fe0bb76251c4aee225eef40d8873c35d58d30e8:src/main/java/com/estoqueapi/EstoqueApi/Servicos/PrevisoesService.java
        return this.cadastrarPrevisoes(prev);
    }

    //Excluir previsão (só pode ser excluído quando finalizada = false)
    @Transactional
<<<<<<< HEAD:src/main/java/com/estoqueapi/EstoqueApi/Servicos/PrevisaoService.java
    public void excluirPrevisao(long idPrevisao){
        Previsao previsao = this.filtrarId(idPrevisao);
        if(previsao.getFinalizada() == false){
            previsaoRepository.delete(previsao);
        }else{
                throw new IllegalArgumentException("Essa ordem já foi realizada, não poderá ser excluída");
=======
    public void excluirPrevisao(long idPrevisao) {
        Previsoes previsoes = this.filtrarId(idPrevisao);
        if (previsoes.getFinalizada() == false) {
            previsoesRepository.delete(previsoes);
        } else {
            throw new IllegalArgumentException("Essa ordem já foi realizada, não poderá ser excluída");
>>>>>>> 0fe0bb76251c4aee225eef40d8873c35d58d30e8:src/main/java/com/estoqueapi/EstoqueApi/Servicos/PrevisoesService.java
        }
    }

    public List<Previsao> consultarPendentesByIdItem(Long idItem) {
        return previsaoRepository.ConsultarPendentesByIdItem(idItem);
    }

    public Previsoes validarPrevisoes(Previsoes previsao) {
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
        return previsoesRepository.findByOrdem(ordem).orElse(null) != null;
    }

    public Previsoes findByOrdem(String ordem){
        return previsoesRepository.findByOrdem(ordem).orElseThrow(() -> new EntityNotFoundException("Ordem não localizada"));
    }

}


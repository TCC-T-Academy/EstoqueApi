package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.Previsoes;
import com.estoqueapi.EstoqueApi.Repositorios.ItensRepository;
import com.estoqueapi.EstoqueApi.Repositorios.PrevisoesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Service
public class PrevisoesService {

    @Autowired
    private PrevisoesRepository previsoesRepository;

    @Autowired
    private ItensRepository itensRepository;

    @Autowired ItensService itensService;

    // Listar todas as previsões cadastradas
    public Iterable<Previsoes> listarPrevisoes() {
        return previsoesRepository.findAll();
    }

    // Filtrar previsões por id do item
    public List<Previsoes> consultarByIdItem(long idItem) {
        return previsoesRepository.ConsultarByIdItem(idItem);
    }

    //Cadastrar previsões
    @Transactional
    public Previsoes cadastrarPrevisoes(Previsoes pr){
        return previsoesRepository.save(pr);
    }

    //Filtrar previsão por idPrevisao
    public Previsoes filtrarId(long idPrevisao) {
        Optional<Previsoes> obj = previsoesRepository.findById(idPrevisao);
        Previsoes prev = null;
        try {
            prev = obj.get();
        } catch (NoSuchElementException exception) {
            throw new EntityNotFoundException("Previsão não localizada");
        }
        return prev;
    }

    //Filtrar por ordem de compra/produção realizadas ou não
    public List<Previsoes> findByFinalizada(boolean finalizada) {
        return previsoesRepository.findByFinalizada(finalizada);
    }

    //Filtrar pela data as previsões que venceram e estão a vencer.
    public List<Previsoes> findByDataPrevistaVencidos(boolean finalizada) {
        if (finalizada == true) {
            return previsoesRepository.findByDataPrevistaVencidos();
        } else
            return previsoesRepository.findByDataPrevistaAVencer();
    }

    // Lista previsões com opção de escolha tanto com data anterior quanto a partir de hoje e finalizadas ou não
    public List<Previsoes> findByDataPrevistaFinalizada(boolean vencimento, boolean finalizada) {
        if (vencimento == true) {
            return previsoesRepository.findByDataPrevistaMenorFinalizada(finalizada);
        } else {
            return previsoesRepository.findByDataPrevistaMaiorIgualFinalizada(finalizada);
        }
    }

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
        return this.cadastrarPrevisoes(prev);
    }

    //Excluir previsão (só pode ser excluído quando finalizada = false)
    @Transactional
    public void excluirPrevisao(long idPrevisao) {
        Previsoes previsoes = this.filtrarId(idPrevisao);
        if (previsoes.getFinalizada() == false) {
            previsoesRepository.delete(previsoes);
        } else {
            throw new IllegalArgumentException("Essa ordem já foi realizada, não poderá ser excluída");
        }
    }

    public List<Previsoes> consultarPendentesByIdItem(Long idItem) {
        return previsoesRepository.ConsultarPendentesByIdItem(idItem);
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


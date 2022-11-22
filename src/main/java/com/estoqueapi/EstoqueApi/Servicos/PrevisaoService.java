package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.Previsao;
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

    // Listar todas as previsões cadastradas
    public Iterable<Previsao> listarPrevisoes(){
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
        }
        catch (NoSuchElementException exception){
            throw  new EntityNotFoundException("Previsão não localizada");
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
        prev.setFinalizada(previsao.isFinalizada());
        prev.setQuantidadePrevista(previsao.getQuantidadePrevista());
        prev.setDataPrevista(previsao.getDataPrevista());
        prev.setOrdem(previsao.getOrdem());
        prev.setItem(prev.getItem());

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
}

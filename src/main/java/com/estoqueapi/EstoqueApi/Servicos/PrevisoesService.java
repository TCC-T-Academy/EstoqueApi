package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.Previsoes;
import com.estoqueapi.EstoqueApi.Entidades.Reservas;
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

    // Listar todas as previsões cadastradas
    public Iterable<Previsoes> listarPrevisoes(){
       return previsoesRepository.findAll();
    }
    // Filtrar previsões por id do item
    public List<Previsoes> consultarByIdItem(Long idItem) {
    return previsoesRepository.ConsultarByIdItem(idItem);
    }

    //Cadastrar previsões
    @Transactional
    public Previsoes cadastrarPrevisoes(Previsoes pr){
        return previsoesRepository.save(pr);
    }

    //Filtrar previsão por idPrevisao
    public Previsoes filtrarId(long idPrevisao){
        Optional<Previsoes> obj = previsoesRepository.findById(idPrevisao);
        Previsoes prev = null;
        try{
            prev = obj.get();
        }
        catch (NoSuchElementException exception){
            throw  new EntityNotFoundException("Previsão não localizada");
        }
        return prev;
    }

    //Filtrar por ordem de compra/produção realizadas ou não
    public List<Previsoes> findByFinalizada(boolean finalizada){
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
        String venci;
        if (vencimento == true) {
            return previsoesRepository.findByDataPrevistaMenorFinalizada(finalizada);
        } else {
            return previsoesRepository.findByDataPrevistaMaiorIgualFinalizada(finalizada);
        }

    }


    //Alterar previsões - Alterar somente se tiver ativo (não realizado)
    public Previsoes alterarPrevisao(Long idPrevisao, Previsoes previsao){
        Previsoes prev = this.filtrarId(idPrevisao);
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
        Previsoes previsoes = this.filtrarId(idPrevisao);
        if(previsoes.getFinalizada() == false){
            previsoesRepository.delete(previsoes);
        }else{
                throw new IllegalArgumentException("Essa ordem já foi realizada, não poderá ser excluída");
        }
    }

    public List<Previsoes> consultarPendentesByIdItem(Long idItem) {
        return previsoesRepository.ConsultarPendentesByIdItem(idItem);
    }
}

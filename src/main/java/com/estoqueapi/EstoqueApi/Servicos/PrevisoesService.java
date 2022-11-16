package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.Previsoes;
import com.estoqueapi.EstoqueApi.Repositorios.PrevisoesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.sql.Date;
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
    //Cadastrar previsões
    @Transactional
    public Previsoes cadastrarPrevisoes(Previsoes pr){
        return previsoesRepository.save(pr);
  }

    //Filtrar previsão por idPrevisao
    public Previsoes filtrarId(Long idPrevisao){
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

    //Alterar previsões - Alterar somente se tiver ativo (não realizado)
    public Previsoes alterarPrevisoes(Long idPrevisoes, Previsoes previsoes){
        Previsoes prev = this.filtrarId(idPrevisoes);
        prev.setFinalizada(previsoes.isFinalizada());
        prev.setQuantidadePrevista(previsoes.getQuantidadePrevista());
        prev.setDataPrevista(previsoes.getDataPrevista());
        prev.setOrdem(previsoes.getOrdem());
        prev.setItem(prev.getItem());

        return this.cadastrarPrevisoes(prev);
    }

    //Excluir previsões - VALIDAÇÃO Não pode excluir previsão já realizada
    @Transactional
    public void excluirPrevisao(Long idPrevisao){
        Previsoes previsoes = this.filtrarId(idPrevisao);
        previsoesRepository.delete(previsoes);
    }

}

package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.Estoque;
import com.estoqueapi.EstoqueApi.Entidades.LogFuturo;
import com.estoqueapi.EstoqueApi.Entidades.Previsao;
import com.estoqueapi.EstoqueApi.Entidades.Reserva;
import com.estoqueapi.EstoqueApi.Repositorios.LogFuturoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
public class LogFuturoService {

    @Autowired
    private LogFuturoRepository repository;
    @Autowired
    private EstoqueService estoqueService;


    public List<LogFuturo> buscarLogIdItem(long idItem) {
        List<LogFuturo> log = repository.findLogByIdItem(idItem);
        Float estoqueAtual = estoqueService.buscarEstoqueIdItem(idItem).getEstoqueReal();

        if(log.size() > 0){
            Float estoqueFuturoItem = log.get(0).getQuantidade() + estoqueAtual;
            log.get(0).setEstoqueMomento(estoqueFuturoItem);

            for (int i = 1; i != log.size(); i++){
                estoqueFuturoItem = log.get(i).getQuantidade() + log.get(i-1).getEstoqueMomento();
                log.get(i).setEstoqueMomento(estoqueFuturoItem);
            }
        }

        return log;
    }
}

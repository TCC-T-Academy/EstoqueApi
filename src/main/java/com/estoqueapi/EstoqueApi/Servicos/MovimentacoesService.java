package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.Movimentacoes;
import com.estoqueapi.EstoqueApi.Repositorios.MovimentacoesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovimentacoesService {

    @Autowired
    MovimentacoesRepository movimentacoesRepository;

    public Movimentacoes salvar(Movimentacoes movimentacao){
        return  movimentacoesRepository.save(movimentacao);
    }

    public List<Movimentacoes> consultar(){
        return movimentacoesRepository.findAll();
    }

    public List<Movimentacoes> salvarVarios(List<Movimentacoes> lista){

        List<Movimentacoes> listaSaida = new ArrayList<>();

        lista.forEach(movimentacoes->{
            listaSaida.add(this.entradaItem(movimentacoes));
        });

        return listaSaida;
    }

    public List<Movimentacoes> consultarByIdItem(Long idItem){
        return movimentacoesRepository.findAllByIdItem(idItem);
    }

    public Movimentacoes entradaItem(Movimentacoes m){

        //GERAR ALTERAR QTD NA TABELA DE ESTOQUE

        m.setDataMovimentacao(Instant.now());
        m.setTipo("IN");

        return this.salvar(m);
    }

    public Movimentacoes saidaItem(Movimentacoes m){

        //GERAR ALTERAR QTD NA TABELA DE ESTOQUE

        m.setDataMovimentacao(Instant.now());
        m.setTipo("OUT");

        return this.salvar(m);
    }




}

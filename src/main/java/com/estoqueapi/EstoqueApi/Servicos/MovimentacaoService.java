package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.Movimentacao;
import com.estoqueapi.EstoqueApi.Entidades.Previsao;
import com.estoqueapi.EstoqueApi.Entidades.Reserva;
import com.estoqueapi.EstoqueApi.Repositorios.EstoqueRepository;
import com.estoqueapi.EstoqueApi.Repositorios.ItemRepository;
import com.estoqueapi.EstoqueApi.Repositorios.MovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovimentacaoService {

    @Autowired
    MovimentacaoRepository movimentacaoRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemService itemService;

    @Autowired
    EstoqueRepository estoqueRepository;

    @Autowired
    ValidacaoService validacaoService;

    @Autowired
    EstoqueService estoqueService;

    @Autowired
    PrevisaoService previsaoService;

    @Autowired
    ReservaService reservaService;




    public Movimentacao salvar(Movimentacao movimentacao) {
        return movimentacaoRepository.save(movimentacao);
    }

    public List<Movimentacao> consultar(){
         return movimentacaoRepository.findAll();
    }

    public List<Movimentacao> salvarVarios(List<Movimentacao> lista){

        List<Movimentacao> listaSaida = new ArrayList<>();

        lista.forEach(movimentacoes->{
            listaSaida.add(this.entradaItem(movimentacoes));
        });

        return listaSaida;
    }

    public List<Movimentacao> consultarByIdItem(Long idItem){
        return movimentacaoRepository.findAllByIdItem(idItem);
    }

    public Movimentacao entradaItem(Movimentacao mov){
        mov.setDataMovimentacao(Instant.now());
        mov.setTipo("IN");
        Movimentacao m = validacaoService.validarMovimentacao(mov);
        Previsao p = validacaoService.consultaPrevisoesByMovimentacao(mov);
        if (p.getIdPrevisao() > 0){
            p.setFinalizada(true);
            previsaoService.alterarPrevisao(p.getIdPrevisao(), p);
        }

        estoqueService.adicionarEstoque(mov.getItem().getIdItem(), mov.getQuantidade());

        return this.salvar(m);
    }

    public Movimentacao saidaItem(Movimentacao mov){
        mov.setDataMovimentacao(Instant.now());
        mov.setTipo("OUT");
        Movimentacao m = validacaoService.validarMovimentacao(mov);
        Reserva r = validacaoService.consultaReservasByMovimentacao(mov);

        if (r.getIdReserva() > 0){
            r.setFinalizada(true);
            reservaService.alterar(r.getIdReserva(), r);
        }

        estoqueService.subtrairEstoque(mov.getItem().getIdItem(), mov.getQuantidade());

        return this.salvar(m);
    }




}

package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.*;
import com.estoqueapi.EstoqueApi.Repositorios.EstoqueRepository;
import com.estoqueapi.EstoqueApi.Repositorios.ItensRepository;
import com.estoqueapi.EstoqueApi.Repositorios.MovimentacoesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import javax.management.AttributeNotFoundException;
import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovimentacoesService {

    @Autowired
    MovimentacoesRepository movimentacoesRepository;

    @Autowired
    ItensRepository itensRepository;

    @Autowired
    ItensService itensService;

    @Autowired
    EstoqueRepository estoqueRepository;

    @Autowired
    ValidacoesService validacoesService;

    @Autowired
    EstoqueService estoqueService;

    @Autowired
    PrevisoesService previsoesService;

    @Autowired
    ReservasService reservasService;




    public Movimentacoes salvar(Movimentacoes movimentacao) {
        return movimentacoesRepository.save(movimentacao);
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

    public Movimentacoes entradaItem(Movimentacoes mov){
        mov.setDataMovimentacao(Instant.now());
        mov.setTipo("IN");
        Movimentacoes m = validacoesService.validarMovimentacao(mov);
        Previsoes p = validacoesService.consultaPrevisoesByMovimentacao(mov);
        if (p.getIdPrevisao() > 0){
            p.setFinalizada(true);
            previsoesService.alterarPrevisao(p.getIdPrevisao(), p);
        }

        estoqueService.adicionarEstoque(mov.getItem().getIdItem(), mov.getQuantidade());

        return this.salvar(m);
    }

    public Movimentacoes saidaItem(Movimentacoes mov){
        mov.setDataMovimentacao(Instant.now());
        mov.setTipo("OUT");
        Movimentacoes m = validacoesService.validarMovimentacao(mov);
        Reservas r = validacoesService.consultaReservasByMovimentacao(mov);

        if (r.getIdReserva() > 0){
            r.setFinalizada(true);
            reservasService.alterar(r.getIdReserva(), r);
        }

        estoqueService.subtrairEstoque(mov.getItem().getIdItem(), mov.getQuantidade());

        return this.salvar(m);
    }




}

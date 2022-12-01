package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Dtos.MovimentacaoDTO;
import com.estoqueapi.EstoqueApi.Entidades.Movimentacao;
import com.estoqueapi.EstoqueApi.Entidades.Previsao;
import com.estoqueapi.EstoqueApi.Entidades.Reserva;
import com.estoqueapi.EstoqueApi.Mapper.Mapper;
import com.estoqueapi.EstoqueApi.Repositorios.LogFuturoRepository;
import com.estoqueapi.EstoqueApi.Repositorios.MovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class MovimentacaoService {

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Autowired
    private ValidacaoService validacaoService;

    @Autowired
    private EstoqueService estoqueService;

    @Autowired
    private PrevisaoService previsaoService;

    @Autowired
    private ReservaService reservaService;
    @Autowired
    private LogFuturoService logFuturoService;




    public Movimentacao salvar(Movimentacao movimentacao) {
        return movimentacaoRepository.save(movimentacao);
    }

    /** Metodo para consultar todas as movimentacoes
     * @return List<Movimentacao> - retorna lista ordenada de movimentacoes
     * */
    public List<Movimentacao> consultar(){
         return movimentacaoRepository.findAllOrderByDesc();
    }

    public Page<MovimentacaoDTO> consultaPaginada(Pageable pageable) {
        Mapper mapper = new Mapper();
        Page<Movimentacao> pageMov = movimentacaoRepository.findAll(pageable);
        Page<MovimentacaoDTO> pageMovDTO = pageMov.map(mov -> mapper.toMovimentacaoDto(mov));
        return pageMovDTO;
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

    /** Metodo para efetuar uma movimentacao de entrada.
     * @param mov Movimentacao - objeto movimentacao referente a entrada
     * @return Movimentacao - retorna movimentacao efetuada
     * */
    public Movimentacao entradaItem(Movimentacao mov){
        mov.setDataMovimentacao(Instant.now());
        mov.setTipo("IN");
        Movimentacao m = validacaoService.validarMovimentacao(mov);
        Previsao p = validacaoService.consultaPrevisoesByMovimentacao(mov);

        if (p.getIdPrevisao() > 0){
            Previsao p2 = previsaoService.clonar(p);
            p2.setFinalizada(true);
            previsaoService.alterarPrevisao(p.getIdPrevisao(), p2);
        }

        estoqueService.adicionarEstoque(mov.getItem().getIdItem(), mov.getQuantidade());

        //Atualiza estoque futuro
        atualizaEstoqueFuturo(m);

        return this.salvar(m);
    }

    /** Metodo para efetuar uma movimentacao de saida.
     * @param mov Movimentacao - objeto movimentacao referente a saida
     * @return Movimentacao - retorna movimentacao efetuada
     * */
    public Movimentacao saidaItem(Movimentacao mov){
        mov.setDataMovimentacao(Instant.now());
        mov.setTipo("OUT");
        Movimentacao m = validacaoService.validarMovimentacao(mov);
        Reserva r = validacaoService.consultaReservasByMovimentacao(mov);

        if (r.getIdReserva() > 0){
            Reserva r2 = reservaService.clonar(r);
            r2.setFinalizada(true);
            reservaService.alterar(r.getIdReserva(), r2);
        }

        estoqueService.subtrairEstoque(mov.getItem().getIdItem(), mov.getQuantidade());

        //Atualiza estoque futuro
        atualizaEstoqueFuturo(m);

        return this.salvar(m);
    }

    private void atualizaEstoqueFuturo(Movimentacao m){
        estoqueService.atualizarEstoqueFuturo(logFuturoService.buscarLogIdItem(m.getItem().getIdItem()));
    }

}

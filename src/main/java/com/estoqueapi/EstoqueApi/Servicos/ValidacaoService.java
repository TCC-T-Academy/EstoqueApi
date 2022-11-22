package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.*;
import com.estoqueapi.EstoqueApi.Enums.Origem;
import com.estoqueapi.EstoqueApi.Exceptions.MovimentacaoInvalidaException;
import com.estoqueapi.EstoqueApi.Repositorios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class ValidacaoService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private EstoqueService estoqueService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private ReservaService reservaService;
    @Autowired
    private PrevisaoService previsaoService;

    public boolean usuarioExiste(Long idUsuario){
        return usuarioRepository.existsById(idUsuario);
    }

    public Movimentacao validarMovimentacao(Movimentacao m){
        Usuario usuario = usuarioService.buscarUsuarioById(m.getUsuario().getIdUsuario());
        Estoque estoque = estoqueService.buscarEstoqueIdItem(m.getItem().getIdItem());
        Item item = itemService.consultarItemById(m.getItem().getIdItem());

        if(m.getQuantidade() <= 0){
            throw new MovimentacaoInvalidaException("Quantidade Inválida!");
        }

        m.setUsuario(usuario);
        m.setEstoque(estoque);
        m.setItem(item);

        /*
        * Validacoes de usuário, quantidade, item ou outras
        * */

        return m;

    }

    public Previsao consultaPrevisoesByMovimentacao(Movimentacao m){

        List<Previsao> list = previsaoService.consultarPendentesByIdItem(m.getItem().getIdItem());
        Stream<Previsao> stream =  list.stream().filter(previsoes -> {
            boolean ret = previsoes.getOrdem().equalsIgnoreCase(m.getOrigemDestino())
                    && previsoes.getQuantidadePrevista() == m.getQuantidade();
            return ret;
        });

        if (m.getOrigemDestino().equalsIgnoreCase(Origem.AVULSO.toString())
                || m.getOrigemDestino().equalsIgnoreCase(Origem.DEVOLUCAO.toString())){
            Previsao prev = new Previsao();
            prev.setIdPrevisao(0);
            return prev;
        }
        return stream.findFirst().orElseThrow(()-> new MovimentacaoInvalidaException("Previsão não encontrada"));


    }

    public Reserva consultaReservasByMovimentacao(Movimentacao m){

        List<Reserva> list = reservaService.consultarPendentesByIdItem(m.getItem().getIdItem());
        Stream<Reserva> stream =  list.stream().filter(reservas -> {
            boolean ret = reservas.getOrdem().equalsIgnoreCase(m.getOrigemDestino())
                    && reservas.getQuantidadeReserva() == m.getQuantidade();
            return ret;
        });

        if (m.getOrigemDestino().equalsIgnoreCase(Origem.AVULSO.toString())){
            Reserva r = new Reserva();
            r.setIdReserva(0);
            return r;
        }
        return stream.findFirst().orElseThrow(()-> new MovimentacaoInvalidaException("Reserva não encontrada"));
    }

}

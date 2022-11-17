package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.*;
import com.estoqueapi.EstoqueApi.Enums.Origens;
import com.estoqueapi.EstoqueApi.Exceptions.MovimentacaoInvalidaException;
import com.estoqueapi.EstoqueApi.Repositorios.EstoqueRepository;
import com.estoqueapi.EstoqueApi.Repositorios.ItensRepository;
import com.estoqueapi.EstoqueApi.Repositorios.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ValidacoesService {

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private ItensService itensService;

    @Autowired
    private EstoqueService estoqueService;
    @Autowired
    private UsuariosService usuariosService;
    @Autowired
    private ReservasService reservasService;
    @Autowired
    private PrevisoesService previsoesService;

    public boolean usuarioExiste(Long idUsuario){
        return usuariosRepository.existsById(idUsuario);
    }

    public Movimentacoes validarMovimentacao(Movimentacoes m){
        Usuarios usuario = usuariosService.buscarUsuarioById(m.getUsuario().getIdUsuario());
        Estoque estoque = estoqueService.buscarEstoqueIdItem(m.getItem().getIdItem());
        Itens item = itensService.consultarItemById(m.getItem().getIdItem());

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

    public Previsoes consultaPrevisoesByMovimentacao(Movimentacoes m){

        List<Previsoes> list = previsoesService.consultarPendentesByIdItem(m.getItem().getIdItem());
        Stream<Previsoes> stream =  list.stream().filter(previsoes -> {
            boolean ret = previsoes.getOrdem().equalsIgnoreCase(m.getOrigemDestino())
                    && previsoes.getQuantidadePrevista() == m.getQuantidade();
            return ret;
        });

        if (m.getOrigemDestino().equalsIgnoreCase(Origens.AVULSO.toString())
                || m.getOrigemDestino().equalsIgnoreCase(Origens.DEVOLUCAO.toString())){
            Previsoes prev = new Previsoes();
            prev.setIdPrevisao(0);
            return prev;
        }
        return stream.findFirst().orElseThrow(()-> new MovimentacaoInvalidaException("Previsão não encontrada"));


    }

    public Reservas consultaReservasByMovimentacao(Movimentacoes m){

        List<Reservas> list = reservasService.consultarPendentesByIdItem(m.getItem().getIdItem());
        Stream<Reservas> stream =  list.stream().filter(reservas -> {
            boolean ret = reservas.getOrdem().equalsIgnoreCase(m.getOrigemDestino())
                    && reservas.getQuantidadeReserva() == m.getQuantidade();
            return ret;
        });

        if (m.getOrigemDestino().equalsIgnoreCase(Origens.AVULSO.toString())){
            Reservas r = new Reservas();
            r.setIdReserva(0);
            return r;
        }
        return stream.findFirst().orElseThrow(()-> new MovimentacaoInvalidaException("Reserva não encontrada"));
    }


}

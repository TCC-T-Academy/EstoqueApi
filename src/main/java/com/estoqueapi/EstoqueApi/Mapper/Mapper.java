package com.estoqueapi.EstoqueApi.Mapper;

import com.estoqueapi.EstoqueApi.Dtos.*;
import com.estoqueapi.EstoqueApi.Entidades.*;
import com.estoqueapi.EstoqueApi.Servicos.RoleService;
import com.estoqueapi.EstoqueApi.Utils.ConversorData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;

@Component
public class Mapper {

    @Autowired
    private RoleService roleService;

    public MovimentacaoDTO toMovimentacaoDto (Movimentacao movimentacao) {

        long idMovimentacao = movimentacao.getIdMovimentacao();
        Estoque estoque = movimentacao.getEstoque();
        Usuario usuario = movimentacao.getUsuario();
        String tipo = movimentacao.getTipo();
        String origemDestino = movimentacao.getOrigemDestino();
        float quantidade = movimentacao.getQuantidade();
        LocalDateTime dataMovimentacao = ConversorData.toLocalDateTime(movimentacao.getDataMovimentacao());

        return new MovimentacaoDTO(idMovimentacao,dataMovimentacao,tipo,origemDestino,quantidade,this.toUsuarioPublicoDTO(usuario),estoque);
    }

    public Movimentacao toMovimentacao(MovimentacaoDTO movimentacaoDTO) {
        return new Movimentacao(
                ConversorData.toInstant(movimentacaoDTO.getDataMovimentacao()),
                movimentacaoDTO.getTipo(),
                movimentacaoDTO.getOrigemDestino(),
                movimentacaoDTO.getQuantidade(),
                movimentacaoDTO.getEstoque(),
                movimentacaoDTO.getEstoque().getItem(),
                this.toUsuario(movimentacaoDTO.getUsuario()));
    }

    public UsuarioPublicoDTO toUsuarioPublicoDTO(Usuario usuario){
        long idUsuario = usuario.getIdUsuario();
        String nome = usuario.getNome();
        String email = usuario.getEmail();
        String role = usuario.getRoles().stream().findFirst().orElseThrow(() -> new EntityNotFoundException("Role não encontrada")).getAuthority();


        return new UsuarioPublicoDTO( idUsuario, nome, email, role);
    }

    public UsuarioNovoDTO toUsuarioNovoDTO(Usuario usuario){
        long idUsuario = usuario.getIdUsuario();

        String nome = usuario.getNome();
        String email = usuario.getEmail();
        String role = usuario.getRoles().stream().findFirst().orElseThrow(() -> new EntityNotFoundException("Role não encontrada")).getAuthority();
        String senha = usuario.getSenha();

        return new UsuarioNovoDTO( idUsuario, nome, senha, email, role);
    }
    public Usuario toUsuario(UsuarioPublicoDTO usuarioPublicoDTO){
        return new Usuario(usuarioPublicoDTO.getNome(),"","");
    }

    public Usuario toUsuario(UsuarioNovoDTO usuarioNovoDTO){
        Usuario usuario = new Usuario();
        Role role = roleService.consultarByAuthority(usuarioNovoDTO.getRole());
        HashSet<Role> roles = new HashSet<Role>();
        roles.add(role);
        usuario.setIdUsuario(0l);
        usuario.setNome(usuarioNovoDTO.getNome());
        usuario.setEmail(usuarioNovoDTO.getEmail());
        usuario.setSenha(usuarioNovoDTO.getSenha());
        usuario.setRoles(roles);
        return usuario;
    }

    public Movimentacao toMovimentacao(MovimentacaoNovaDTO movimentacaoNovaDTO){
        Usuario u = new Usuario();
        u.setIdUsuario(movimentacaoNovaDTO.getIdUsuario());

        Item i = new Item();
        i.setIdItem(movimentacaoNovaDTO.getIdItem());

        Movimentacao m = new Movimentacao();
        m.setQuantidade(movimentacaoNovaDTO.getQuantidade());
        m.setOrigemDestino(movimentacaoNovaDTO.getOrigemDestino());
        m.setUsuario(u);
        m.setItem(i);

        return m;
    }

    public MovimentacaoNovaDTO toMovimentacaoNovaDTO(Movimentacao movimentacao){
        return new MovimentacaoNovaDTO(movimentacao.getOrigemDestino()
                ,movimentacao.getQuantidade()
                ,movimentacao.getItem().getIdItem()
                , movimentacao.getUsuario().getIdUsuario());
    }

    public PrevisaoDTO toPrevisaoDTO (Previsao previsao) {

        long idPrevisao = previsao.getIdPrevisao();
        String ordem = previsao.getOrdem();
        Usuario usuario = previsao.getUsuario();
        Item item = previsao.getItem();
        float quantidadePrevista = previsao.getQuantidadePrevista();
        LocalDate dataPrevista = previsao.getDataPrevista();
        boolean finalizada = previsao.getFinalizada();

        return new PrevisaoDTO(idPrevisao,item,this.toUsuarioPublicoDTO(usuario),quantidadePrevista,dataPrevista,ordem,finalizada);
    }

    public Previsao toPrevisao(PrevisaoDTO previsaoDTO) {
        return new Previsao(
                previsaoDTO.getIdPrevisao(),
                previsaoDTO.getItem(),
                this.toUsuario(previsaoDTO.getUsuario()),
                previsaoDTO.getQuantidadePrevista(),
                previsaoDTO.getDataPrevista(),
                previsaoDTO.getOrdem(),
                previsaoDTO.isFinalizada());
    }

    public Previsao toPrevisao(PrevisaoNovaDTO previsaoNovaDTO){
        Usuario u = new Usuario();
        u.setIdUsuario(previsaoNovaDTO.getIdUsuario());

        Item i = new Item();
        i.setIdItem(previsaoNovaDTO.getIdItem());

        Previsao p = new Previsao();
        p.setQuantidadePrevista(previsaoNovaDTO.getQuantidadePrevista());
        p.setDataPrevista(previsaoNovaDTO.getDataPrevista());
        p.setOrdem(previsaoNovaDTO.getOrdem());
        p.setFinalizada(previsaoNovaDTO.isFinalizada());
        p.setUsuario(u);
        p.setItem(i);

        return p;
    }

    public PrevisaoNovaDTO previsaoNovaDTO(Previsao previsao){
        return new PrevisaoNovaDTO(previsao.getOrdem(),
                previsao.getQuantidadePrevista(),
                previsao.getItem().getIdItem(),
                previsao.getUsuario().getIdUsuario(),
                previsao.getDataPrevista(),
                previsao.getFinalizada());
    }


    public ReservaDTO toReservaDTO(Reserva reserva) {

        long idReserva = reserva.getIdReserva();
        boolean finalizada = reserva.isFinalizada();
        float quantidadeReserva = reserva.getQuantidadeReserva();
        LocalDate dataPrevista = reserva.getDataPrevista();
        String ordem = reserva.getOrdem();
        Usuario usuario = reserva.getUsuario();
        Item item = reserva.getItem();

        return new ReservaDTO(idReserva,finalizada,quantidadeReserva,dataPrevista, ordem,this.toUsuarioPublicoDTO(usuario),item);
    }

    public Reserva toReserva(ReservaDTO reservaDTO) {
        return new Reserva(
                reservaDTO.getIdReserva(),
                reservaDTO.isFinalizada(),
                reservaDTO.getQuantidadeReserva(),
                reservaDTO.getDataPrevista(),
                reservaDTO.getOrdem(),
                this.toUsuario(reservaDTO.getUsuario()),
                reservaDTO.getItem());
    }

    public Reserva toReserva(ReservaNovaDTO reservaNovaDTO){
        Usuario u = new Usuario();
        u.setIdUsuario(reservaNovaDTO.getIdUsuario());

        Item i = new Item();
        i.setIdItem(reservaNovaDTO.getIdItem());

        Reserva r = new Reserva();
        r.setQuantidadeReserva(reservaNovaDTO.getQuantidadeReserva());
        r.setDataPrevista(reservaNovaDTO.getDataPrevista());
        r.setOrdem(reservaNovaDTO.getOrdem());
        r.setFinalizada(reservaNovaDTO.isFinalizada());
        r.setUsuario(u);
        r.setItem(i);

        return r;
    }

    public ReservaNovaDTO reservaNovaDTO(Reserva reserva){
        return new ReservaNovaDTO(reserva.isFinalizada(),
                reserva.getQuantidadeReserva(),
                reserva.getDataPrevista(),
                reserva.getOrdem(),
                reserva.getUsuario().getIdUsuario(),
                reserva.getItem().getIdItem());
    }
}

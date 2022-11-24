package com.estoqueapi.EstoqueApi.Mapper;

import com.estoqueapi.EstoqueApi.Dtos.MovimentacaoDTO;
import com.estoqueapi.EstoqueApi.Dtos.MovimentacaoNovaDTO;
import com.estoqueapi.EstoqueApi.Dtos.UsuarioPublicoDTO;
import com.estoqueapi.EstoqueApi.Entidades.*;
import com.estoqueapi.EstoqueApi.Enums.PerfilUsuario;
import com.estoqueapi.EstoqueApi.Utils.ConversorData;
import org.aspectj.apache.bcel.generic.RET;
import org.hibernate.loader.plan.spi.Return;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class Mapper {
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
        PerfilUsuario perfil = usuario.getPerfil();

        return new UsuarioPublicoDTO( idUsuario, nome, perfil);
    }
    public Usuario toUsuario(UsuarioPublicoDTO usuarioPublicoDTO){
        return new Usuario(usuarioPublicoDTO.getNome(),"",usuarioPublicoDTO.getPerfilUsuario(),"");
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

}

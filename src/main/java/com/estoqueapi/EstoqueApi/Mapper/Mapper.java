package com.estoqueapi.EstoqueApi.Mapper;

import com.estoqueapi.EstoqueApi.Dtos.*;
import com.estoqueapi.EstoqueApi.Entidades.*;
import com.estoqueapi.EstoqueApi.Enums.PerfilUsuario;
import com.estoqueapi.EstoqueApi.Utils.ConversorData;
import org.aspectj.apache.bcel.generic.RET;
import org.hibernate.loader.plan.spi.Return;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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
        p.setQuantidadePrevista(previsaoNovaDTO.getQuantidadePrevisao());
        p.setDataPrevista(previsaoNovaDTO.getDataPrevisao());
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



}

package com.estoqueapi.EstoqueApi.Dtos;

import com.estoqueapi.EstoqueApi.Entidades.Estoque;
import com.estoqueapi.EstoqueApi.Entidades.Movimentacao;
import com.estoqueapi.EstoqueApi.Entidades.Usuario;
import com.estoqueapi.EstoqueApi.Servicos.MovimentacaoService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import java.time.Instant;
import java.time.LocalDateTime;


public class MovimentacaoDTO {

    private long idMovimentacao = 0;
    private LocalDateTime dataMovimentacao;
    private String tipo; //IN OUT
    private String origemDestino;
    private float quantidade;
    private UsuarioPublicoDTO usuario;
    private Estoque estoque;


    public MovimentacaoDTO() {
    }

    public MovimentacaoDTO(long idMovimentacao, LocalDateTime dataMovimentacao, String tipo, String origemDestino, float quantidade, UsuarioPublicoDTO usuario, Estoque estoque) {
        this.idMovimentacao = idMovimentacao;
        this.dataMovimentacao = dataMovimentacao;
        this.tipo = tipo;
        this.origemDestino = origemDestino;
        this.quantidade = quantidade;
        this.usuario = usuario;
        this.estoque = estoque;
    }

    public long getIdMovimentacao() {
        return idMovimentacao;
    }

    public void setIdMovimentacao(long idMovimentacao) {
        this.idMovimentacao = idMovimentacao;
    }

    public LocalDateTime getDataMovimentacao() {
        return dataMovimentacao;
    }

    public void setDataMovimentacao(LocalDateTime dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getOrigemDestino() {
        return origemDestino;
    }

    public void setOrigemDestino(String origemDestino) {
        this.origemDestino = origemDestino;
    }

    public float getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(float quantidade) {
        this.quantidade = quantidade;
    }

    public UsuarioPublicoDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioPublicoDTO usuario) {
        this.usuario = usuario;
    }

    public Estoque getEstoque() {
        return estoque;
    }

    public void setEstoque(Estoque estoque) {
        this.estoque = estoque;
    }
}

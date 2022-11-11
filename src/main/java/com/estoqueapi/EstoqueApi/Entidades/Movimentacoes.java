package com.estoqueapi.EstoqueApi.Entidades;

import javax.persistence.*;
import java.time.Instant;

@Entity
public class Movimentacoes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idMovimentacao;
    @Column(columnDefinition = "DATETIME")
    private Instant dataMovimentacao;
    private String tipo; //IN OUT
    private String origemDestino;
    private float quantidade;

    private long idItem;
    private long idUsuario;

    /*
    private Estoque estoque;
    private Itens item;
    private Usuarios usuario;
    */

    public Movimentacoes() {
    }

    public Movimentacoes(Instant dataMovimentacao, String tipo, String origemDestino, float quantidade, long idItem, long idUsuario) {
        this.dataMovimentacao = dataMovimentacao;
        this.tipo = tipo;
        this.origemDestino = origemDestino;
        this.quantidade = quantidade;
        this.idItem = idItem;
        this.idUsuario = idUsuario;
    }

    public long getIdItem() {
        return idItem;
    }

    public void setIdItem(long idItem) {
        this.idItem = idItem;
    }

    public long getIdMovimentacao() {
        return idMovimentacao;
    }

    public void setIdMovimentacao(long idMovimentacao) {
        this.idMovimentacao = idMovimentacao;
    }

    public Instant getDataMovimentacao() {
        return dataMovimentacao;
    }

    public void setDataMovimentacao(Instant dataMovimentacao) {
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

    public long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setQuantidade(float quantidade) {
        this.quantidade = quantidade;
    }
}

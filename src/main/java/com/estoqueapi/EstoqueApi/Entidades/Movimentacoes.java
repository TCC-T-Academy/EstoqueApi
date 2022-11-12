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

    @ManyToOne
    private Estoque estoque;

    @ManyToOne
    private Itens item;

    @ManyToOne
    private Usuarios usuario;


    public Movimentacoes() {
    }

    public Movimentacoes(Instant dataMovimentacao, String tipo, String origemDestino, float quantidade, Estoque estoque, Itens item, Usuarios usuario) {
        this.dataMovimentacao = dataMovimentacao;
        this.tipo = tipo;
        this.origemDestino = origemDestino;
        this.quantidade = quantidade;
        this.estoque = estoque;
        this.item = item;
        this.usuario = usuario;
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

    public void setQuantidade(float quantidade) {
        this.quantidade = quantidade;
    }

    public Estoque getEstoque() {
        return estoque;
    }

    public void setEstoque(Estoque estoque) {
        this.estoque = estoque;
    }

    public Itens getItem() {
        return item;
    }

    public void setItem(Itens item) {
        this.item = item;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }
}

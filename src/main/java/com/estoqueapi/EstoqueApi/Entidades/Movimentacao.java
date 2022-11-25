package com.estoqueapi.EstoqueApi.Entidades;

import javax.persistence.*;
import java.time.Instant;

@Entity
public class Movimentacao {

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
    private Item item;

    @ManyToOne
    private Usuario usuario;


    public Movimentacao() {
    }

    public Movimentacao(Instant dataMovimentacao, String tipo, String origemDestino, float quantidade, Estoque estoque
            , Item item, Usuario usuario) {
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

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}

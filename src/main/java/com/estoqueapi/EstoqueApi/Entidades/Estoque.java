package com.estoqueapi.EstoqueApi.Entidades;

import javax.persistence.*;

@Entity
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idEstoque;
    private String localizacao;
    private Float estoqueReal;

    @ManyToOne
    private Item item;

    public Estoque() {
    }

    public Estoque(String localizacao, Float estoqueReal, Item item) {
        this.localizacao = localizacao;
        this.estoqueReal = estoqueReal;
        this.item = item;
    }

    public long getIdEstoque() {
        return idEstoque;
    }

    public void setIdEstoque(long idEstoque) {
        this.idEstoque = idEstoque;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public Float getEstoqueReal() {
        return estoqueReal;
    }

    public void setEstoqueReal(Float estoqueReal) {
        this.estoqueReal = estoqueReal;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}

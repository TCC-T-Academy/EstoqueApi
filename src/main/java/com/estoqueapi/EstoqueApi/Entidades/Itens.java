package com.estoqueapi.EstoqueApi.Entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Itens {

    @Id
    private long idItem;
    private String descricao = "";
    private String grupo = "";
    private String familia = "";
    private String unidade = "";
    @Column(columnDefinition = "float default 0")
    private float estoqueSeguranca;

    public Itens() {
    }

    public Itens(long idItem, String descricao, String grupo, String familia, String unidade, float estoqueSeguranca) {
        this.idItem = idItem;
        this.descricao = descricao;
        this.grupo = grupo;
        this.familia = familia;
        this.unidade = unidade;
        this.estoqueSeguranca = estoqueSeguranca;
    }

    public long getIdItem() {
        return idItem;
    }

    public void setIdItem(long idItem) {
        this.idItem = idItem;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getFamilia() {
        return familia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public float getEstoqueSeguranca() {
        return estoqueSeguranca;
    }

    public void setEstoqueSeguranca(float estoqueSeguranca) {
        this.estoqueSeguranca = estoqueSeguranca;
    }
}

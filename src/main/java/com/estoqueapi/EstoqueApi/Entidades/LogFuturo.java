package com.estoqueapi.EstoqueApi.Entidades;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@Entity
@Immutable
@Subselect("SELECT UUID() AS ID, LOG_FUTURO.* FROM LOG_FUTURO")
public class LogFuturo {

    @Id
    private String id;
    private long idItem;
    private String tipoMovimentacao;
    private String origemDestino;
    private Instant data;
    private Float quantidade;
   private Float estoqueMomento;


    public LogFuturo(String tipoMovimentacao, String origemDestino, Instant data, Float quantidade) {
        this.tipoMovimentacao = tipoMovimentacao;
        this.origemDestino = origemDestino;
        this.data = data;
        this.quantidade = quantidade;
        this.estoqueMomento = estoqueMomento;
    }

    public LogFuturo(Instant data, String origemDestino, Float quantidade) {
        this.origemDestino = origemDestino;
        this.data = data;
        this.quantidade = quantidade;
    }

    public LogFuturo() {
    }

    public String getTipoMovimentacao() {
        return tipoMovimentacao;
    }

    public void setTipoMovimentacao(String tipoMovimentacao) {
        this.tipoMovimentacao = tipoMovimentacao;
    }

    public String getOrigemDestino() {
        return origemDestino;
    }

    public void setOrigemDestino(String origemDestino) {
        this.origemDestino = origemDestino;
    }

    public Instant getData() {
        return data;
    }

    public void setData(Instant data) {
        this.data = data;
    }

    public Float getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Float quantidade) {
        this.quantidade = quantidade;
    }

    public long getIdItem() {
        return idItem;
    }

    public void setIdItem(long idItem) {
        this.idItem = idItem;
    }
    public Float getEstoqueMomento() {
        return estoqueMomento;
    }

    public void setEstoqueMomento(Float estoqueMomento) {
        this.estoqueMomento = estoqueMomento;
    }



}

package com.estoqueapi.EstoqueApi.Entidades;

import java.sql.Date;

import javax.persistence.*;

@Entity
@Table(name = "previsoes")
public class Previsoes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idPrevisao;

    @ManyToOne
    private Itens item;

    @ManyToOne
    private Usuarios usuario;

    private float quantidadePrevista;
    private Date dataPrevista;
    private String ordem;
    private boolean finalizada = false;



    public long getIdPrevisao() {
        return this.idPrevisao;
    }

    public void setIdPrevisao(long idPrevisao) {
        this.idPrevisao = idPrevisao;
    }

    public float getQuantidadePrevista() {
        return this.quantidadePrevista;
    }

    public void setQuantidadePrevista(float quantidadePrevista) {
        this.quantidadePrevista = quantidadePrevista;
    }

    public Date getDataPrevista() {
        return this.dataPrevista;
    }

    public void setDataPrevista(Date dataPrevista) {
        this.dataPrevista = dataPrevista;
    }

    public String getOrdem() {
        return this.ordem;
    }

    public void setOrdem(String ordem) {
        this.ordem = ordem;
    }

    public boolean isFinalizada() {
        return this.finalizada;
    }

    public boolean getFinalizada() {
        return this.finalizada;
    }

    public void setFinalizada(boolean finalizada) {
        this.finalizada = finalizada;
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
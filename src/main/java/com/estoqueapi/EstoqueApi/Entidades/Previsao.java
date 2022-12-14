package com.estoqueapi.EstoqueApi.Entidades;

import java.time.LocalDate;

import javax.persistence.*;

@Entity
public class Previsao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idPrevisao;

    @ManyToOne
    private Item item;

    @ManyToOne
    private Usuario usuario;

    private float quantidadePrevista;

    @Column(columnDefinition = "date")
    private LocalDate dataPrevista;
    private String ordem;
    private boolean finalizada = false;

    public Previsao() {
    }

    public Previsao(long idPrevisao, Item item, Usuario usuario, float quantidadePrevista, LocalDate dataPrevista, String ordem, boolean finalizada) {
        this.item = item;
        this.usuario = usuario;
        this.quantidadePrevista = quantidadePrevista;
        this.dataPrevista = dataPrevista;
        this.ordem = ordem;
        this.finalizada = finalizada;
    }

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

    public LocalDate getDataPrevista() {
        return dataPrevista;
    }

    public void setDataPrevista(LocalDate dataPrevista) {
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
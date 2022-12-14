package com.estoqueapi.EstoqueApi.Entidades;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

@Entity
public class Reserva {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private long idReserva;
    private boolean finalizada = false;
    private float quantidadeReserva;
    @Column(columnDefinition = "date")
    private LocalDate dataPrevista;
    private String ordem;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Item item;

    public Reserva() {
    }

    public Reserva(long idReserva, boolean finalizada, float quantidadeReserva, LocalDate dataPrevista, String ordem, Usuario usuario, Item item) {
        this.idReserva = idReserva;
        this.finalizada = finalizada;
        this.quantidadeReserva = quantidadeReserva;
        this.dataPrevista = dataPrevista;
        this.ordem = ordem;
        this.usuario = usuario;
        this.item = item;
    }

    public long getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(long idReserva) {
        this.idReserva = idReserva;
    }

    public boolean isFinalizada() {
        return finalizada;
    }

    public void setFinalizada(boolean finalizada) {
        this.finalizada = finalizada;
    }

    public float getQuantidadeReserva() {
        return quantidadeReserva;
    }

    public void setQuantidadeReserva(float quantidadeReserva) {
        this.quantidadeReserva = quantidadeReserva;
    }

    public LocalDate getDataPrevista() {
        return dataPrevista;
    }

    public void setDataPrevista(LocalDate dataPrevista) {
        this.dataPrevista = dataPrevista;
    }

    public String getOrdem() {
        return ordem;
    }

    public void setOrdem(String ordem) {
        this.ordem = ordem;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

}

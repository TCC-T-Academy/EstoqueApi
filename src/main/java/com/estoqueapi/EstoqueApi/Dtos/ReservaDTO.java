package com.estoqueapi.EstoqueApi.Dtos;

import com.estoqueapi.EstoqueApi.Entidades.Item;
import com.estoqueapi.EstoqueApi.Entidades.Usuario;

import java.time.LocalDate;

public class ReservaDTO {

    private long idReserva;
    private boolean finalizada = false;
    private float quantidadeReserva;
    private LocalDate dataPrevista;
    private String ordem;
    private UsuarioPublicoDTO usuario;
    private Item item;

    public ReservaDTO() {
    }

    public ReservaDTO(long idReserva, boolean finalizada, float quantidadeReserva, LocalDate dataPrevista, String ordem, UsuarioPublicoDTO usuario, Item item) {
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

    public UsuarioPublicoDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioPublicoDTO usuario) {
        this.usuario = usuario;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}

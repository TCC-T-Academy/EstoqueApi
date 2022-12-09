package com.estoqueapi.EstoqueApi.Dtos;

import com.estoqueapi.EstoqueApi.Entidades.Item;
import com.estoqueapi.EstoqueApi.Entidades.Usuario;

import java.time.LocalDate;

public class ReservaNovaDTO {

    private boolean finalizada = false;
    private float quantidadeReserva;
    private LocalDate dataPrevista;
    private String ordem;
    private long idUsuario;
    private long idItem;

    // Construtor padrão
    public ReservaNovaDTO() {
    }

    // Construtor completo
    public ReservaNovaDTO(boolean finalizada, float quantidadeReserva, LocalDate dataPrevista, String ordem, long idUsuario, long idItem) {
        this.finalizada = finalizada;
        this.quantidadeReserva = quantidadeReserva;
        this.dataPrevista = dataPrevista;
        this.ordem = ordem;
        this.idUsuario = idUsuario;
        this.idItem = idItem;
    }

    // Getter e setters
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

    public long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public long getIdItem() {
        return idItem;
    }

    public void setIdItem(long idItem) {
        this.idItem = idItem;
    }
}

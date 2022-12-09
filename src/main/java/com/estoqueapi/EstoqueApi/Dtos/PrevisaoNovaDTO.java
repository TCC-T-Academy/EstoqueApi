package com.estoqueapi.EstoqueApi.Dtos;

import java.time.LocalDate;

public class PrevisaoNovaDTO {

    private String ordem;
    private float quantidadePrevista;
    private long idItem;
    private long idUsuario;
    private LocalDate dataPrevista;
    private boolean finalizada = false;

    public PrevisaoNovaDTO(String ordem, float quantidade, long idItem, long idUsuario, LocalDate dataPrevista, boolean finalizada) {
        this.ordem = ordem;
        this.quantidadePrevista = quantidade;
        this.idItem = idItem;
        this.idUsuario = idUsuario;
        this.dataPrevista = dataPrevista;
        this.finalizada = finalizada;
    }

    public PrevisaoNovaDTO() {
    }

    public String getOrdem() {
        return ordem;
    }

    public void setOrdem(String ordem) {
        this.ordem = ordem;
    }

    public float getQuantidadePrevista() {
        return quantidadePrevista;
    }

    public void setQuantidadePrevista(float quantidade) {
        this.quantidadePrevista = quantidade;
    }

    public long getIdItem() {
        return idItem;
    }

    public void setIdItem(long idItem) {
        this.idItem = idItem;
    }

    public long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public LocalDate getDataPrevista() { return dataPrevista; }

    public void setDataPrevista(LocalDate dataPrevista) { this.dataPrevista = dataPrevista; }

    public boolean isFinalizada() {
        return finalizada;
    }

    public void setFinalizada(boolean finalizada) {
        this.finalizada = finalizada;
    }
}

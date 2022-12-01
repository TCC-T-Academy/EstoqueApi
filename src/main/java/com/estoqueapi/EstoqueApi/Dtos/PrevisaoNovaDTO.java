package com.estoqueapi.EstoqueApi.Dtos;

import java.time.LocalDate;

public class PrevisaoNovaDTO {

    private String ordem;
    private float quantidadePrevisao;
    private long idItem;
    private long idUsuario;
    private LocalDate dataPrevisao;
    private boolean finalizada = false;

    public PrevisaoNovaDTO(String ordem, float quantidade, long idItem, long idUsuario, LocalDate dataPrevisao, boolean finalizada) {
        this.ordem = ordem;
        this.quantidadePrevisao = quantidade;
        this.idItem = idItem;
        this.idUsuario = idUsuario;
        this.dataPrevisao = dataPrevisao;
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

    public float getQuantidadePrevisao() {
        return quantidadePrevisao;
    }

    public void setQuantidadePrevisao(float quantidade) {
        this.quantidadePrevisao = quantidade;
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

    public LocalDate getDataPrevisao() {
        return dataPrevisao;
    }

    public void setDataPrevisao(LocalDate dataPrevisao) {
        this.dataPrevisao = dataPrevisao;
    }

    public boolean isFinalizada() {
        return finalizada;
    }

    public void setFinalizada(boolean finalizada) {
        this.finalizada = finalizada;
    }
}

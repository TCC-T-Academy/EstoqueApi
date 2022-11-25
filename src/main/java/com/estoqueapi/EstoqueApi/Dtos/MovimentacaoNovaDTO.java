package com.estoqueapi.EstoqueApi.Dtos;

public class MovimentacaoNovaDTO {
    private String origemDestino;
    private float quantidade;
    private long idItem;
    private long idUsuario;

    public MovimentacaoNovaDTO(String origemDestino, float quantidade, long idItem, long idUsuario) {
        this.origemDestino = origemDestino;
        this.quantidade = quantidade;
        this.idItem = idItem;
        this.idUsuario = idUsuario;
    }

    public MovimentacaoNovaDTO() {
    }

    public String getOrigemDestino() {
        return origemDestino;
    }

    public void setOrigemDestino(String origemDestino) {
        this.origemDestino = origemDestino;
    }

    public float getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(float quantidade) {
        this.quantidade = quantidade;
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
}

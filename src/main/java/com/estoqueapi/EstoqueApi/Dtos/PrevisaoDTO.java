package com.estoqueapi.EstoqueApi.Dtos;

import com.estoqueapi.EstoqueApi.Entidades.Item;
import com.estoqueapi.EstoqueApi.Entidades.Usuario;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

public class PrevisaoDTO {

    private long idPrevisao;
    private Item item;
    private UsuarioPublicoDTO usuario;
    private float quantidadePrevista;
    private LocalDate dataPrevista;
    private String ordem;
    private boolean finalizada = false;

    // Construtor padrão
    public PrevisaoDTO() {
    }

    // Construtor
    public PrevisaoDTO(long idPrevisao, Item item, UsuarioPublicoDTO usuario, float quantidadePrevista, LocalDate dataPrevista, String ordem, boolean finalizada) {
        this.idPrevisao = idPrevisao;
        this.item = item;
        this.usuario = usuario;
        this.quantidadePrevista = quantidadePrevista;
        this.dataPrevista = dataPrevista;
        this.ordem = ordem;
        this.finalizada = finalizada;
    }

    // Getter e setters
    public long getIdPrevisao() {
        return idPrevisao;
    }

    public void setIdPrevisao(long idPrevisao) {
        this.idPrevisao = idPrevisao;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public UsuarioPublicoDTO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioPublicoDTO usuario) {
        this.usuario = usuario;
    }

    public float getQuantidadePrevista() {
        return quantidadePrevista;
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
        return ordem;
    }

    public void setOrdem(String ordem) {
        this.ordem = ordem;
    }

    public boolean isFinalizada() {
        return finalizada;
    }

    public void setFinalizada(boolean finalizada) {
        this.finalizada = finalizada;
    }
}

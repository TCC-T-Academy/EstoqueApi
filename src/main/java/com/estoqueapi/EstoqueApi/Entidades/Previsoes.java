package com.estoqueapi.EstoqueApi.Entidades;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "previsoes")
public class Previsoes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPrevisao;
    //private String itens_idItem;
    //private String usuarios_idUsuario;
    private float quantidadePrevista;
    private Date dataPrevista;
    private String ordem;
    private boolean finalizada;



    public int getIdPrevisao() {
        return this.idPrevisao;
    }

    public void setIdPrevisao(int idPrevisao) {
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

    


}

package com.estoqueapi.EstoqueApi.Dtos;

import com.estoqueapi.EstoqueApi.Enums.PerfilUsuario;

public class UsuarioPublicoDTO {

    private long idUsuario;
    private String nome;
    private PerfilUsuario perfilUsuario;

    public UsuarioPublicoDTO(long idUsuario, String nome, PerfilUsuario perfilUsuario) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.perfilUsuario = perfilUsuario;
    }

    public UsuarioPublicoDTO() {
    }

    public long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public PerfilUsuario getPerfilUsuario() {
        return perfilUsuario;
    }

    public void setPerfilUsuario(PerfilUsuario perfilUsuario) {
        this.perfilUsuario = perfilUsuario;
    }
}

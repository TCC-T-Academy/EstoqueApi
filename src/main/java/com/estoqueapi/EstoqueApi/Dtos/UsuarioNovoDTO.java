package com.estoqueapi.EstoqueApi.Dtos;

import javax.persistence.Column;

public class UsuarioNovoDTO {
    private Long idUsuario;
    private String nome = "";
    private String senha = "";
    private String email = "";
    private String role = "";


    public UsuarioNovoDTO(Long idUsuario, String nome, String senha, String email, String role) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.senha = senha;
        this.email = email;
        this.role = role;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

package com.estoqueapi.EstoqueApi.Entidades;

import com.estoqueapi.EstoqueApi.Enums.PerfilUsuario;

import javax.persistence.*;

@Entity
public class Usuarios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario = 0l;
    private String nome = "";
    private String senha = "";

    @Column(columnDefinition = "integer default 1")
    private PerfilUsuario perfil = PerfilUsuario.COMUM;
    @Column(unique = true)
    private String email = "";

    public Usuarios() {
    }

    public Usuarios(String nome, String senha, PerfilUsuario perfil, String email) {
        this.nome = nome;
        this.senha = senha;
        this.perfil = perfil;
        this.email = email;
    }

    public long getIdUsuario() {
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

    public PerfilUsuario getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilUsuario perfil) {
        this.perfil = perfil;
    }
}

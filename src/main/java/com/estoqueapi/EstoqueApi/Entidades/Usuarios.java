package com.estoqueapi.EstoqueApi.Entidades;

import com.estoqueapi.EstoqueApi.Enums.PerfilUsuario;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Usuarios {
    @Id
    private int idUsuario;
    private String nome;
    private String senha;

    @Column(columnDefinition = "integer default 1")
    private PerfilUsuario perfil = PerfilUsuario.COMUM;
    @Column(unique = true)
    private String email;

    public Usuarios() {
    }

    public Usuarios(String nome, String senha, PerfilUsuario perfil, String email) {
        this.nome = nome;
        this.senha = senha;
        this.perfil = perfil;
        this.email = email;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
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

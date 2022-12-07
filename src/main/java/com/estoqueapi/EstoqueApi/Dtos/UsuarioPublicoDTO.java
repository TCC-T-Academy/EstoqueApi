package com.estoqueapi.EstoqueApi.Dtos;

public class UsuarioPublicoDTO {

    private long idUsuario;
    private String nome;
    private String email;
    private String role;

    public UsuarioPublicoDTO(long idUsuario, String nome, String email, String role) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.email = email;
        this.role = role;
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

}

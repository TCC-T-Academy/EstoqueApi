package com.estoqueapi.EstoqueApi.Entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRole;
    private String authority;

    public Role() {
    }

    public Role(Long id, String authority) {
        this.idRole = id;
        this.authority = authority;
    }

    public Long getId() {
        return idRole;
    }

    public void setId(Long idRole) {
        this.idRole = idRole;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

}

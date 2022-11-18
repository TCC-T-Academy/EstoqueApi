package com.estoqueapi.EstoqueApi.Controllers;


import com.estoqueapi.EstoqueApi.Entidades.Usuarios;
import com.estoqueapi.EstoqueApi.Enums.PerfilUsuario;

import com.estoqueapi.EstoqueApi.Servicos.UsuariosService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.Column;
import javax.persistence.EntityNotFoundException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class UsuariosControllerTest {

    private Long idUsuarioExistente;
    private String nome;
    private String senha;
    private PerfilUsuario perfil = PerfilUsuario.COMUM;
    private String emailExistente = "joaquim.soares@empresa.com";

    private Usuarios usuarioNovo;
    private Usuarios usuarioAlterado;
    private Usuarios usuarioDesabilitado;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsuariosService usuariosService;

    @BeforeEach
    void setup() throws Exception {
        usuarioNovo = new Usuarios();
        usuarioNovo.setIdUsuario(22l);
        usuarioNovo.setNome("teste");
        usuarioNovo.setSenha("teste123");
        usuarioNovo.setPerfil(PerfilUsuario.COMUM);
        usuarioNovo.setEmail("teste@teste.com");

        usuarioAlterado = new Usuarios();
        usuarioAlterado.setNome("Joaquim Soares");
        usuarioAlterado.setSenha("1234");
        usuarioAlterado.setPerfil(PerfilUsuario.ADMINISTRADOR);
        usuarioAlterado.setEmail("joaquim.soares.silva@empresa.com");

        usuarioDesabilitado = new Usuarios();

    }

    @Test
    @DisplayName("Retorna FOUND ao consultar Usuarios")
    public void retornaFOUNDAoConsultarUsuarios() throws Exception {
        ResultActions resultado = mockMvc.perform(get("/usuarios")
                .accept(MediaType.APPLICATION_JSON));
        resultado.andExpect(status().isFound());
        resultado.andDo(print());
    }

    @Test
    @DisplayName("Retorna CREATED quado salvar o usuario com sucesso")
    void retornarCREATEDQuandoUsuarioSalvarComSucesso() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(usuarioNovo);

        ResultActions resultado = mockMvc.perform(post("/usuarios/novo")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        resultado.andExpect(status().isCreated());
        resultado.andDo(print());
    }

    @Test
    void retornarACCEPTEDQuandoUsuarioSalvarComSucesso() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(usuarioAlterado);

        ResultActions resultado = mockMvc.perform(put("/usuarios/alterar/{email}", emailExistente)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        resultado.andExpect(status().isAccepted());
        resultado.andDo(print());
    }

    @Test
    void retornarACCEPTEDQuandoUsuarioAlterarComSucesso() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(usuarioDesabilitado);

        ResultActions resultado = mockMvc.perform(put("/usuarios/desabilitar/{email}", emailExistente)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        resultado.andExpect(status().isAccepted());
        resultado.andDo(print());
    }

}

package com.estoqueapi.EstoqueApi.Controllers;

import com.estoqueapi.EstoqueApi.Entidades.Item;
import com.estoqueapi.EstoqueApi.Entidades.Previsao;
import com.estoqueapi.EstoqueApi.Entidades.Usuario;
import com.estoqueapi.EstoqueApi.Enums.PerfilUsuario;
import com.estoqueapi.EstoqueApi.Servicos.PrevisaoService;
import com.estoqueapi.EstoqueApi.Utils.ConversorData;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.EntityNotFoundException;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class PrevisaoControllerTest {


    private Long idExistente;
    private Long idNaoExistente;
    private List<Previsao> lista;
    private Previsao previsaoNova;
    private Previsao previsaoExistente;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PrevisaoService service;

    @BeforeEach
    void setup() throws Exception {
        idExistente = 1L;
        idNaoExistente = 2000L;
        previsaoNova = new Previsao();
        Item item = new Item(1600, "PARAF SEXT RI 1/4 X 1-3/4 UNC20 C7/16 A3,8 POLIDO", "INDUSTRIALIZAÇÃO", "PARAFUSOS", "PC",58);
        Usuario usuario = new Usuario("Joao Silva", "1234", PerfilUsuario.COMUM, "joao.silva@empresa.com");
        Instant data = ConversorData.toInstant(LocalDateTime.parse("2022-11-22T00:00:00"));
        previsaoExistente = new Previsao(item, usuario, 150, data, "string", false);

        lista = new ArrayList<>();

        Mockito.when(service.filtrarId(idExistente)).thenReturn(previsaoExistente);
        Mockito.when(service.filtrarId(idNaoExistente)).thenThrow(EntityNotFoundException.class);
        Mockito.when(service.listarPrevisoes()).thenReturn(lista);
        Mockito.when(service.cadastrarPrevisoes(any())).thenReturn(previsaoExistente);
        Mockito.when(service.alterarPrevisao(eq(idExistente), any())).thenReturn(previsaoExistente);
        Mockito.when(service.alterarPrevisao(eq(idNaoExistente), any())).thenThrow(EntityNotFoundException.class);
        Mockito.doNothing().when(service).excluirPrevisao(idExistente);
        Mockito.doThrow(EntityNotFoundException.class).when(service).excluirPrevisao(idNaoExistente);
    }

    // Teste unitário ao listar todas as previsões
    @Test
    @DisplayName("Retorna ok quando listado todas as previsões")
    void deveRetornarOkQuandoConsultarTodos() throws Exception {
        ResultActions result = mockMvc.perform(get("/previsoes").accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("Retorna criação ok quando  adicionado nova previsão")
    void deveRetornar201QuandoPrevisaoSalvaComSucesso() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(previsaoNova);

        ResultActions result = mockMvc.perform(post("/previsoes")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());
    }


    @Test
    @DisplayName("Retorna OK quando pesquisa um ID e ele existe")
    void deveRetornarStatusOKQuandoIdExistente() throws Exception {
        ResultActions result = mockMvc
                .perform(get("/previsoes/{id_previsao}", idExistente).accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
    }
    @Test
    @DisplayName("Retorna Not Found quando pesquisa um ID inexistente")
    void deveRetornarStatusERROQuandoIdNaoExistente() throws Exception {
        ResultActions result = mockMvc
                .perform(get("/previsoes/{id_previsao}", idNaoExistente).accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Retorna 200 quando uma alteração foi feita com sucesso")
    void deveRetornarStatus200QuandoAlterarPrevisaoExistenteComSucesso() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(previsaoExistente);
        ResultActions result = mockMvc.perform(put("/previsoes/alterar/{idPrevisao}", idExistente)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("Retorna 404 quando tenta alterar uma previsão que não existe")
    void deveRetornarStatus404QuandoAlterarPrevisaoInexistente() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(previsaoNova);
        ResultActions result = mockMvc.perform(put("/previsoes/alterar/{idPrevisao}", idNaoExistente)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Retorna código 200 (OK) quando foi excluído com sucesso")
    void deveRetornar200QuandoExcluirPrevisaoExistente() throws Exception {
        ResultActions result = mockMvc.perform(delete("/previsoes/excluir/{idPrevisao}", idExistente)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().is(200));
    }

    @Test
    @DisplayName("Retorna erro 404 quando a IdPrevisão não existe")
    void deveRetornar404QuandoExcluirPrevisaoInexistente() throws Exception {
        ResultActions result = mockMvc.perform(delete("/previsoes/excluir/{idPrevisao}", idNaoExistente)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

}

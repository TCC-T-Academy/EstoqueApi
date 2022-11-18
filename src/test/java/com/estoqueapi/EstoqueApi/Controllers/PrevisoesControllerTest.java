package com.estoqueapi.EstoqueApi.Controllers;

import com.estoqueapi.EstoqueApi.Entidades.Itens;
import com.estoqueapi.EstoqueApi.Entidades.Previsoes;
import com.estoqueapi.EstoqueApi.Entidades.Usuarios;
import com.estoqueapi.EstoqueApi.Enums.PerfilUsuario;
import com.estoqueapi.EstoqueApi.Servicos.PrevisoesService;
import org.junit.jupiter.api.BeforeEach;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.EntityNotFoundException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class PrevisoesControllerTest {


    private Long idExistente;
    private Long idNaoExistente;
    private List<Previsoes> lista;
    private Previsoes previsaoNova;
    private Previsoes previsaoExistente;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PrevisoesService service;

    @BeforeEach
    void setup() throws Exception {
        idExistente = 1L;
        idNaoExistente = 2000L;
        previsaoNova = new Previsoes();
        Itens item = new Itens(1600, "PARAF SEXT RI 1/4 X 1-3/4 UNC20 C7/16 A3,8 POLIDO", "INDUSTRIALIZAÇÃO", "PARAFUSOS", "PC",58);
        Usuarios usuario = new Usuarios("Joao Silva", "1234", PerfilUsuario.COMUM, "joao.silva@empresa.com");
        Date data = new Date(2022-11-22);
        previsaoExistente = new Previsoes(item, usuario, 150, data, "string", false);

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
    void deveRetornarOkQuandoConsultarTodos() throws Exception {
        ResultActions result = mockMvc.perform(get("/previsoes").accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
    }

    @Test
    void deveRetornar201QuandoPrevisaoSalvaComSucesso() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(previsaoNova);

        ResultActions result = mockMvc.perform(post("/previsoes")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());
    }

    @Test
    void deveRetornarStatusOKQuandoIdExistente() throws Exception {
        ResultActions result = mockMvc
                .perform(get("/previsoes/{id_previsao}", idExistente).accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
    }



}

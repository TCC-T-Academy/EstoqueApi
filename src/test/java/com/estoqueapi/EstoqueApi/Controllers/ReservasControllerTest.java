package com.estoqueapi.EstoqueApi.Controllers;

import com.estoqueapi.EstoqueApi.Entidades.Itens;
import com.estoqueapi.EstoqueApi.Entidades.Reservas;
import com.estoqueapi.EstoqueApi.Entidades.Usuarios;
import com.estoqueapi.EstoqueApi.Enums.PerfilUsuario;
import com.estoqueapi.EstoqueApi.Servicos.ReservasService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import javax.persistence.EntityNotFoundException;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testar seguintes endpoints:
 * GET /reservas/
 * GET /reservas/{idItem}
 * GET /reservas/{idItemInexistente}
 * GET /reservas/vencimento/
 * GET /reservas/pendentes/
 * POST /reservas/
 * PUT /reservas/{idReserva}
 * PUT /reservas/{idReservaInexistente}
 * DELETE /reservas/{idReserva}
 * DELETE /reservas/{idReservaInexistente}
 *
 */

@SpringBootTest
@AutoConfigureMockMvc
public class ReservasControllerTest {
    private Usuarios u;
    private Itens i1;
    private Itens i2;
    private Reservas r1;
    private Reservas r2;
    private Reservas r2Alterada;
    private Reservas r3;
    private Long idItemExistente1;
    private Long idItemExistente2;
    private Long idItemInexistente;
    private Long idReservaExistente1;
    private Long idReservaExistente2;
    private Long idReservaExistente3;
    private Long idReservaInexistente;
    private List<Reservas> listaDeReservas;
    private List<Reservas> listaDeReservasPorItem1;
    private List<Reservas> listaDeReservasPorItem2;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservasService service;

    @BeforeEach
    public void setup(){
        idItemExistente1 = 1000l;
        idItemExistente2 = 2000l;
        idItemInexistente = 3000l;
        idReservaExistente1 = 10l;
        idReservaExistente2 = 20l;
        idReservaExistente3 = 30l;
        idReservaInexistente = 40l;


        listaDeReservas = new ArrayList<>();
        listaDeReservasPorItem1 = new ArrayList<>();
        listaDeReservasPorItem2 = new ArrayList<>();

        i1 = new Itens();
        i1.setIdItem(idItemExistente1);
        i1.setDescricao("Item 1");
        i1.setFamilia("Teste");
        i1.setGrupo("Teste");
        i1.setEstoqueSeguranca(20f);
        i1.setUnidade("PC");

        i2 = new Itens();
        i2.setIdItem(idItemExistente2);
        i2.setDescricao("Item 2");
        i2.setFamilia("Teste");
        i2.setGrupo("Teste");
        i2.setEstoqueSeguranca(20f);
        i2.setUnidade("PC");

        u = new Usuarios();
        u.setNome("Usuário de teste");
        u.setEmail("usuario@email.com");
        u.setPerfil(PerfilUsuario.COMUM);

        r1 = new Reservas();
        r1.setIdReserva(idReservaExistente1);
        r1.setItem(i1);
        r1.setQuantidadeReserva(20f);
        r1.setOrdem("ORDEM 123");
        r1.setUsuario(u);


        r2 = new Reservas();
        r2.setIdReserva(idReservaExistente2);
        r2.setItem(i2);
        r2.setQuantidadeReserva(20f);
        r2.setOrdem("ORDEM 321");
        r2.setUsuario(u);


        r3 = new Reservas();
        r3.setIdReserva(idReservaExistente3);
        r3.setItem(i2);
        r3.setQuantidadeReserva(20f);
        r3.setOrdem("ORDEM 456");
        r3.setUsuario(u);


        r2Alterada = new Reservas();
        r2Alterada.setIdReserva(idReservaExistente2);
        r2Alterada.setItem(i2);
        r2Alterada.setQuantidadeReserva(20f);
        r2Alterada.setOrdem("ORDEM 999");
        r2Alterada.setUsuario(u);


        listaDeReservas.add(r1);
        listaDeReservas.add(r1);
        listaDeReservas.add(r1);

        listaDeReservasPorItem1.add(r1);

        listaDeReservasPorItem2.add(r2);
        listaDeReservasPorItem2.add(r3);

        Mockito.when(service.consultar()).thenReturn(listaDeReservas);
        Mockito.when(service.consultarByIdItem(idItemExistente1)).thenReturn(listaDeReservasPorItem1);
        Mockito.when(service.consultarByIdItem(idItemExistente2)).thenReturn(listaDeReservasPorItem2);
        Mockito.when(service.consultarByIdItem(idItemInexistente)).thenThrow(EntityNotFoundException.class);
        Mockito.when(service.salvar(any())).thenReturn(r1);
        Mockito.when(service.alterar(eq(idReservaExistente2), any())).thenReturn(r2Alterada);
        Mockito.when(service.alterar(eq(idReservaInexistente), any())).thenThrow(EntityNotFoundException.class);
        Mockito.doNothing().when(service).excluir(idReservaExistente3);
        Mockito.doThrow(EntityNotFoundException.class).when(service).excluir(idReservaInexistente);
    }

    @Test
    public void retornaOKQuandoConsultaReservas() throws Exception {
        ResultActions resultado =
                mockMvc.perform(get("/reservas").accept(MediaType.APPLICATION_JSON));

        resultado.andExpect(status().isOk());
        resultado.andExpect(jsonPath("$.size()").value(listaDeReservas.size()));
        resultado.andDo(print());
    }

    @Test
    public void retornaOKQuandoConsultaReservaPorIdItemExistente1() throws Exception {
        ResultActions resultado =
                mockMvc.perform(get("/reservas/{idItemExistente1}", idItemExistente1).accept(MediaType.APPLICATION_JSON));

        resultado.andExpect(status().isOk());
        resultado.andExpect(jsonPath("$.size()").value(listaDeReservasPorItem1.size()));
        resultado.andExpect(jsonPath("$[0].item.descricao").value("Item 1"));
//        resultado.andDo(print());
    }

    @Test
    public void retornaOKQuandoConsultaReservaPorIdItemExistente2() throws Exception {
        ResultActions resultado =
                mockMvc.perform(get("/reservas/{idItemExistente2}", idItemExistente2).accept(MediaType.APPLICATION_JSON));

        resultado.andExpect(status().isOk());
        resultado.andExpect(jsonPath("$.size()").value(listaDeReservasPorItem2.size()));
        resultado.andExpect(jsonPath("$[0].item.descricao").value("Item 2"));
//        resultado.andDo(print());
    }

    @Test
    public void retornaNotFoundQuandoConsultaReservaPorIdItemInexistente() throws Exception {
        ResultActions resultado =
                mockMvc.perform(get("/reservas/{idItemInexistente}", idItemInexistente).accept(MediaType.APPLICATION_JSON));

        resultado.andExpect(res -> assertTrue(res.getResolvedException() instanceof EntityNotFoundException));
        resultado.andExpect(status().isNotFound());
        resultado.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.error").value(
                "Recurso não encontrado"));
        resultado.andDo(print());
    }

    @Test
    void retornaOKQuandoAlteraReservaExistente() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(r2Alterada);
        ResultActions resultado = mockMvc.perform(put("/reservas/{idReservaExistente2}", idReservaExistente2)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        resultado.andExpect(status().isOk());
        resultado.andExpect(jsonPath("$.idReserva").value(r2.getIdReserva()));
        resultado.andExpect(jsonPath("$.ordem").value("ORDEM 999"));
//        resultado.andDo(print());

    }

    @Test
    public void retornaCreatedQuandoSalvaReserva() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(r1);

        ResultActions resultado = mockMvc.perform(post("/reservas")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        resultado.andExpect(status().isCreated());
        resultado.andExpect(jsonPath("$.idReserva").exists());
        resultado.andExpect(jsonPath("$.idReserva").value(idReservaExistente1));
        resultado.andDo(print());
    }

    @Test
    void retornaNotFoundQuandoAlteraReservaPorIdReservaInexistente() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(r2Alterada);
        ResultActions resultado = mockMvc.perform(put("/reservas/{idReservaInexistente}", idReservaInexistente)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        resultado.andExpect(res -> assertTrue(res.getResolvedException() instanceof EntityNotFoundException));
        resultado.andExpect(status().isNotFound());
        resultado.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.error").value(
                "Recurso não encontrado"));
        resultado.andDo(print());
    }


    @Test
    void retornaNoContentQuandoExcluiReservaPorIdReservaExistente() throws Exception {
        ResultActions resultado = mockMvc.perform(delete("/reservas/{idReservaExistente3}", idReservaExistente3)
                .accept(MediaType.APPLICATION_JSON));

        resultado.andExpect(status().isNoContent());
        resultado.andDo(print());
    }

    @Test
    void retornaNotFoundQuandoExcluiReservaPorIdReservaInexistente() throws Exception {
        ResultActions resultado = mockMvc.perform(delete("/reservas/{idReservaInexistente}", idReservaInexistente)
                .accept(MediaType.APPLICATION_JSON));

        resultado.andExpect(status().isNotFound());
        resultado.andExpect(res -> assertTrue(res.getResolvedException() instanceof EntityNotFoundException));
        resultado.andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.error").value(
                "Recurso não encontrado"));
        resultado.andDo(print());
    }

}

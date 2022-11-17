package com.estoqueapi.EstoqueApi.Controllers;

import com.estoqueapi.EstoqueApi.Entidades.Estoque;
import com.estoqueapi.EstoqueApi.Entidades.Itens;
import com.estoqueapi.EstoqueApi.Servicos.EstoqueService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EstoqueControllerTest {

    private Itens item;
    private Estoque estoque;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EstoqueService service;

    @Autowired
    ObjectMapper objectMapper;

//    Testar endpoint /estoque
//    Testar endpoint /estoque/{idItem}


    @BeforeEach
    void setup(){
        item = new Itens();
        item.setIdItem(1000l);
        item.setDescricao("Item teste para teste");
        item.setFamilia("Teste");
        item.setGrupo("Teste");
        item.setEstoqueSeguranca(20f);
        item.setUnidade("PC");

        estoque = new Estoque();
        estoque.setItem(item);
        estoque.setEstoqueReal(40f);
        estoque.setLocalizacao("AA1234");
        estoque.setIdEstoque(10l);
    }


    @Test
    public void retornaFoundQuandoConsultaEstoque() throws Exception {

        ResultActions resultado = mockMvc.perform(get("/estoque").accept(MediaType.APPLICATION_JSON));

        resultado.andExpect(status().isFound()); // Resultado não é 200, mas 302 (found)
    }

    @Test
    public void retornaFoundQuandoConsultaEstoquePorIdItem() throws Exception {


    }
}

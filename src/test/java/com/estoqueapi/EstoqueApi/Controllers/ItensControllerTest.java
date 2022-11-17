package com.estoqueapi.EstoqueApi.Controllers;

import com.estoqueapi.EstoqueApi.Servicos.ItensService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class ItensControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void retornaFoundQuandoConsultaItens() throws Exception {

        ResultActions resultado = mockMvc.perform(get("/itens").accept(MediaType.APPLICATION_JSON));

        resultado.andExpect(status().isFound()); // Resultado não é 200 (ok), mas 302 (found)
    }
}

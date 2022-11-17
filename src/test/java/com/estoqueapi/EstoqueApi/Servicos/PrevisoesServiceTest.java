package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.Previsoes;
import com.estoqueapi.EstoqueApi.Repositorios.PrevisoesRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import javax.persistence.EntityNotFoundException;
import java.sql.Date;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class PrevisoesServiceTest {

    private Previsoes previsoes;
    private Optional<Previsoes> optPrevisoes;

    @Mock
    private PrevisoesRepository previsoesRepository;

    @InjectMocks
    private PrevisoesService previsoesService;

    @BeforeEach
    void setup(){
        java.sql.Date data = new java.sql.Date(2022-11-16);
        previsoes = new Previsoes();
        previsoes.setIdPrevisao(22);
        previsoes.setQuantidadePrevista(10);
        previsoes.setDataPrevista(data);
        previsoes.setOrdem("OC-1234");
        previsoes.setFinalizada(false);

        optPrevisoes = Optional.of(previsoes);
    }

    //filtrarId (filtra ID correto)
    @Test
    @DisplayName("Retorna previsao quando encontrado")
    public void retornaPrevisaoQuandoEncontrado(){
        long idPrevisaoCorreta = 22;

        Mockito.when(previsoesRepository.findById(previsoes.getIdPrevisao())).thenReturn(optPrevisoes);
        Assertions.assertEquals(previsoes,previsoesService.filtrarId(idPrevisaoCorreta));
    }

}






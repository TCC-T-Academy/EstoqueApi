package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.Previsao;
import com.estoqueapi.EstoqueApi.Repositorios.PrevisaoRepository;
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
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class PrevisaoServiceTest {
    private Long idNaoExistente;

    private Previsao previsao;
    private Optional<Previsao> optPrevisoes;

    @Mock
    private PrevisaoRepository previsaoRepository;

    @InjectMocks
    private PrevisaoService previsaoService;

    @BeforeEach
    void setup() {
        java.sql.Date data = new java.sql.Date(2022 - 11 - 16);
        idNaoExistente = 7L;
        previsao = new Previsao();
        previsao.setIdPrevisao(22);
        previsao.setQuantidadePrevista(10);
        previsao.setDataPrevista(data);
        previsao.setOrdem("OC-1234");
        previsao.setFinalizada(false);;

        optPrevisoes = Optional.of(previsao);
    }

    //filtrarId (filtra ID correto)
    @Test
    @DisplayName("Retorna previsao quando encontrado")
    public void retornaPrevisaoQuandoEncontrado() {
        long idPrevisaoCorreta = 22;

        Mockito.when(previsaoRepository.findById(previsao.getIdPrevisao())).thenReturn(optPrevisoes);
        Assertions.assertEquals(previsao, previsaoService.filtrarId(idPrevisaoCorreta));
    }
    @Test
    @DisplayName("Retorna lista não null a quando utilizado o findAll")
    void retornaNotNull() {
        Iterable<Previsao> listaPrevisoes = previsaoService.listarPrevisoes();
        Assertions.assertNotNull(listaPrevisoes);

        Mockito.verify(previsaoRepository).findAll();
    }
    @Test
    @DisplayName("Lança EntityNotFoundException ao tentar deletar idInexistente")
    public void lancaEntityNotFoundException() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            previsaoService.excluirPrevisao(idNaoExistente);
        });
        Mockito.verify(previsaoRepository, Mockito.times(1)).findById(idNaoExistente);
    }

}


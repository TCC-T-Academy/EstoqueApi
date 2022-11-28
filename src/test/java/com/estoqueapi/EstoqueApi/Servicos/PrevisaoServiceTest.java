package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.Item;
import com.estoqueapi.EstoqueApi.Entidades.Previsao;
import com.estoqueapi.EstoqueApi.Entidades.Usuario;
import com.estoqueapi.EstoqueApi.Exceptions.AcaoNaoPermitidaException;
import com.estoqueapi.EstoqueApi.Repositorios.ItemRepository;
import com.estoqueapi.EstoqueApi.Repositorios.PrevisaoRepository;
import com.estoqueapi.EstoqueApi.Utils.ConversorData;
import com.sun.source.tree.ModuleTree;
import org.hibernate.mapping.Any;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class PrevisaoServiceTest {
    private Long idNaoExistente;

    private Previsao previsao;
    private Optional<Previsao> optPrevisoes;

    @Mock
    private PrevisaoRepository previsaoRepository;
    @Mock
    private ItemService itemService;
    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private PrevisaoService previsaoService;

    @BeforeEach
    void setup() {
        Instant data = ConversorData.toInstant(LocalDateTime.parse("2022-11-16T00:00:00"));
        idNaoExistente = 7L;
        previsao = new Previsao();
        previsao.setIdPrevisao(22);
        previsao.setQuantidadePrevista(10);
        previsao.setDataPrevista(data);
        previsao.setOrdem("OC-1234");
        previsao.setFinalizada(false);;

        optPrevisoes = Optional.of(previsao);
    }

    @Test
    @DisplayName("Retorna previsao se salvamento correto")
    public void retornaPrevisaoSeSalvamentoCorreto(){
        Item mockItem = new Item();
        mockItem.setIdItem(100l);
        previsao.setItem(mockItem);

        Usuario mockUsuario = new Usuario();
        mockUsuario.setIdUsuario(1l);
        previsao.setUsuario(mockUsuario);

        previsao.setDataPrevista(Instant.now().plus(5, ChronoUnit.DAYS));

        List<Previsao> mockList = new ArrayList<>();
        mockList.add(previsao);

        Mockito.when(itemService.consultarItemById(mockItem.getIdItem())).thenReturn(mockItem);
        Mockito.when(usuarioService.buscarUsuarioById(mockUsuario.getIdUsuario())).thenReturn(mockUsuario);
        Mockito.when(previsaoRepository.save(previsao)).thenReturn(previsao);

        Assertions.assertEquals(previsao.getIdPrevisao(),previsaoService.cadastrarPrevisoes(previsao).getIdPrevisao());

    }

    @Test
    @DisplayName("Lanca Excecao se data anterior a atual")
    public void lancaExcecaoSeDataAnteriorAtual(){
        Item mockItem = new Item();
        mockItem.setIdItem(100l);
        previsao.setItem(mockItem);

        Usuario mockUsuario = new Usuario();
        mockUsuario.setIdUsuario(1l);
        previsao.setUsuario(mockUsuario);

        previsao.setDataPrevista(previsao.getDataPrevista().minus(5, ChronoUnit.DAYS));

        List<Previsao> mockList = new ArrayList<>();
        mockList.add(previsao);

        Mockito.when(itemService.consultarItemById(mockItem.getIdItem())).thenReturn(mockItem);
        Mockito.when(usuarioService.buscarUsuarioById(mockUsuario.getIdUsuario())).thenReturn(mockUsuario);
        Mockito.when(previsaoRepository.save(previsao)).thenReturn(previsao);

        AcaoNaoPermitidaException ex = Assertions.assertThrows(AcaoNaoPermitidaException.class,() -> previsaoService.cadastrarPrevisoes(previsao).getIdPrevisao());

    }

    @Test
    @DisplayName("Retorna previsao existente com qtd atualizada se ordem encontrada e pendente")
    public void retornaPrevisaoExistenteComQtdAtualizadaSeOrdemEncontradaPendente(){
        Item mockItem = new Item();
        mockItem.setIdItem(100l);
        previsao.setItem(mockItem);

        Usuario mockUsuario = new Usuario();
        mockUsuario.setIdUsuario(1l);
        previsao.setUsuario(mockUsuario);

        previsao.setDataPrevista(Instant.now().plus(5, ChronoUnit.DAYS));

        List<Previsao> mockList = new ArrayList<>();
        mockList.add(previsao);

        float mockNovaQuantidade = 10;
        Previsao mockPrev = new Previsao();
        mockPrev.setDataPrevista(previsao.getDataPrevista());
        mockPrev.setOrdem(previsao.getOrdem());
        mockPrev.setItem(previsao.getItem());
        mockPrev.setIdPrevisao(0);
        mockPrev.setFinalizada(previsao.getFinalizada());
        mockPrev.setQuantidadePrevista(mockNovaQuantidade);
        mockPrev.setUsuario(previsao.getUsuario());

        optPrevisoes = Optional.of(previsao);
        Mockito.when(itemService.consultarItemById(mockItem.getIdItem())).thenReturn(mockItem);
        Mockito.when(usuarioService.buscarUsuarioById(mockUsuario.getIdUsuario())).thenReturn(mockUsuario);
        Mockito.when(previsaoRepository.save(previsao)).thenReturn(previsao);
        Mockito.when(previsaoRepository.findById(previsao.getIdPrevisao())).thenReturn(optPrevisoes);
        Mockito.when(previsaoRepository.findByOrdem(previsao.getOrdem())).thenReturn(mockList);

        float retornoEsperado = previsao.getQuantidadePrevista() + mockNovaQuantidade;

        Assertions.assertEquals(retornoEsperado,previsaoService.cadastrarPrevisoes(previsao).getQuantidadePrevista());

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


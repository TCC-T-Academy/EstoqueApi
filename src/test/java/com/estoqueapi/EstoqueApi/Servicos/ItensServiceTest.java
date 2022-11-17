package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.Itens;
import com.estoqueapi.EstoqueApi.Repositorios.ItensRepository;
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
public class ItensServiceTest {

    private Itens item;
    private Optional<Itens> optItem;

    @Mock
    private ItensRepository itensRepository;

    @InjectMocks
    private ItensService itensService;

    @BeforeEach
    void setup(){
        item = new Itens();
        item.setIdItem(1000l);
        item.setDescricao("Item teste para teste");
        item.setFamilia("Teste");
        item.setGrupo("Teste");
        item.setEstoqueSeguranca(20f);
        item.setUnidade("PC");

        optItem = Optional.of(item);
    }

    @Test
    @DisplayName("Retorna item quando encontrado")
    public void retornaItemQuandoEncontrado(){
        long idItemCorreto = 1000;

        Mockito.when(itensRepository.findById(item.getIdItem())).thenReturn(optItem);

        Assertions.assertEquals(item, itensService.consultarItemById(idItemCorreto));
    }

    @Test
    @DisplayName("Lanca excecao se item nao encontrado")
    public void lancaExcecaoSeItemNaoEncontrado(){
        long idItemErrado = 1001;
        String msgEsperada = "Item nao encontrado";

        Mockito.when(itensRepository.findById(item.getIdItem())).thenReturn(optItem);

        EntityNotFoundException ex = Assertions.assertThrows(EntityNotFoundException.class, () -> itensService.consultarItemById(idItemErrado));
        Assertions.assertEquals(msgEsperada,ex.getMessage());

    }


}

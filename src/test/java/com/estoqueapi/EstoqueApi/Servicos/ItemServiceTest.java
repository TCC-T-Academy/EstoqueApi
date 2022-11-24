package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.Item;
import com.estoqueapi.EstoqueApi.Repositorios.ItemRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ItemServiceTest {

    private Item item;
    private Optional<Item> optItem;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @BeforeEach
    void setup(){
        item = new Item();
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

        Mockito.when(itemRepository.findById(item.getIdItem())).thenReturn(optItem);

        Assertions.assertEquals(item, itemService.consultarItemById(idItemCorreto));
    }

    @Test
    @DisplayName("Lanca excecao se item nao encontrado")
    public void lancaExcecaoSeItemNaoEncontrado(){
        long idItemErrado = 1001;
        String msgEsperada = "Item nao encontrado";

        Mockito.when(itemRepository.findById(item.getIdItem())).thenReturn(optItem);

        EntityNotFoundException ex = Assertions.assertThrows(EntityNotFoundException.class, () -> itemService.consultarItemById(idItemErrado));
        Assertions.assertEquals(msgEsperada,ex.getMessage());

    }

    @Test
    @DisplayName("Retorna item apos salvamento correto")
    public void retornaItemAposSalvamentoCorreto(){

        //Mockando retorno
        Mockito.when(itemRepository.save(item)).thenReturn(item);

        //Testando
        Assertions.assertEquals(item, itemService.salvar(item));
    }

    @Test
    @DisplayName("Retorna item alterado apos alteracao")
    public void retornaItemAlteradoAposAlteracao(){
        Item itemAlterado = new Item();
        itemAlterado.setIdItem(item.getIdItem());
        itemAlterado.setFamilia("NOVA FAMILIA");
        itemAlterado.setDescricao("NOVA DESCRICAO");
        itemAlterado.setGrupo("NOVO GRUPO");
        itemAlterado.setEstoqueSeguranca(20f);
        itemAlterado.setUnidade("NOVA UNIDADE");

        //Mockando retorno
        Mockito.when(itemRepository.findById(item.getIdItem())).thenReturn(optItem);
        Mockito.when(itemRepository.save(item)).thenReturn(item);

        //Testando se a alteracao do item realmente aconteceu
        Item itenRetornado = itemService.alterarItem(item.getIdItem(), itemAlterado);
        Assertions.assertEquals(itemAlterado.getFamilia(),itenRetornado.getFamilia());
        Assertions.assertEquals(itemAlterado.getDescricao(),itenRetornado.getDescricao());
        Assertions.assertEquals(itemAlterado.getGrupo(),itenRetornado.getGrupo());
        Assertions.assertEquals(itemAlterado.getUnidade(),itenRetornado.getUnidade());
        Assertions.assertEquals(itemAlterado.getEstoqueSeguranca(),itenRetornado.getEstoqueSeguranca());
    }



}

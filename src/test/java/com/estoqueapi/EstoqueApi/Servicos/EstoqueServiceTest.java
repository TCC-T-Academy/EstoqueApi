package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.Estoque;
import com.estoqueapi.EstoqueApi.Entidades.Item;
import com.estoqueapi.EstoqueApi.Exceptions.ItemForaEstoqueException;
import com.estoqueapi.EstoqueApi.Exceptions.MovimentacaoInvalidaException;
import com.estoqueapi.EstoqueApi.Repositorios.EstoqueRepository;
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
public class EstoqueServiceTest {

    private Item item;
    private Estoque estoque;
    private Optional<Estoque> optEstoque;

    @Mock
    EstoqueRepository estoqueRepository;

    @InjectMocks
    EstoqueService estoqueService;

    @BeforeEach
    void setup(){
        item = new Item();
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
    @DisplayName("Retorna objeto estoque se encontrado")
    public void retornaObjetoEstoqueSeEncontrado(){
        optEstoque = Optional.of(estoque);

        //Mockando funcoes do repositorio do estoque
        Mockito.when(estoqueRepository.findByIdItem(item.getIdItem())).thenReturn(optEstoque);

        //testando funcao
        Assertions.assertEquals(optEstoque.get(),estoqueService.buscarEstoqueIdItem(item.getIdItem()));
    }

    @Test
    @DisplayName("Lanca excecao se objeto estoque nao encontrado")
    public void lancaExcecaoSeObjetoEstoqueNaoEncontrado(){
        optEstoque = Optional.of(estoque);
        String msgEsperada = "Item nao encontrado no estoque.";
        long idItemErrado = 100;

        //Mockando funcoes do repositorio do estoque
        Mockito.when(estoqueRepository.findByIdItem(item.getIdItem())).thenReturn(optEstoque);

        //testando funcao
        EntityNotFoundException ex =  Assertions.assertThrows(EntityNotFoundException.class, () -> estoqueService.buscarEstoqueIdItem(idItemErrado));
        Assertions.assertEquals(msgEsperada, ex.getMessage());
    }


    @Test
    @DisplayName("Retorna estoque com quantidade incrementada")
    public void retornaEstoqueQuantidadeIncrementada(){
        estoque.setEstoqueReal(20f);
        optEstoque = Optional.of(estoque);
        float qtd = 20;
        float qtdEsperada = 40;

        //Mockando funcoes do repositorio do estoque
        Mockito.when(estoqueRepository.findByIdItem(item.getIdItem())).thenReturn(optEstoque);
        Mockito.when(estoqueRepository.save(estoque)).thenReturn(estoque);

        //testando funcao
        estoqueService.adicionarEstoque(item.getIdItem(),qtd);

        Assertions.assertEquals(qtdEsperada,estoque.getEstoqueReal());
    }

    @Test
    @DisplayName("Retorna estoque com quantidade decrementada")
    public void retornaEstoqueQuantidadeDecrementada(){
        estoque.setEstoqueReal(30f);
        optEstoque = Optional.of(estoque);
        float qtd = 20;
        float qtdEsperada = 10;

        //Mockando funcoes do repositorio do estoque
        Mockito.when(estoqueRepository.findByIdItem(item.getIdItem())).thenReturn(optEstoque);
        Mockito.when(estoqueRepository.save(estoque)).thenReturn(estoque);

        //testando funcao
        estoqueService.subtrairEstoque(item.getIdItem(),qtd);

        Assertions.assertEquals(qtdEsperada,estoque.getEstoqueReal());
    }

    @Test
    @DisplayName("Lanca excecao se quantidade adicionada igual a 0")
    public void lancaExcecaoQuantidadeAdicionadaIgualZero(){
        estoque.setEstoqueReal(30f);
        optEstoque = Optional.of(estoque);
        float qtd = 0;
        String msgEsperada = "Quantidade Invalida";

        //Mockando funcoes do repositorio do estoque
        Mockito.when(estoqueRepository.findByIdItem(item.getIdItem())).thenReturn(optEstoque);
        Mockito.when(estoqueRepository.save(estoque)).thenReturn(estoque);

        //testando funcao
        MovimentacaoInvalidaException ex = Assertions.assertThrows(MovimentacaoInvalidaException.class,() -> estoqueService.adicionarEstoque(item.getIdItem(),qtd));
        Assertions.assertEquals(msgEsperada,ex.getMessage());
    }



    @Test
    @DisplayName("Lanca excecao se quantidade subtraida maior que real")
    public void lancaExcecaoQuantidadeSubtraidaMaiorQueReal(){
        estoque.setEstoqueReal(30f);
        optEstoque = Optional.of(estoque);
        float qtd = 40;
        String msgEsperada = "Quantidade Indisponível";

        //Mockando funcoes do repositorio do estoque
        Mockito.when(estoqueRepository.findByIdItem(item.getIdItem())).thenReturn(optEstoque);
        Mockito.when(estoqueRepository.save(estoque)).thenReturn(estoque);

        //testando funcao
        MovimentacaoInvalidaException ex = Assertions.assertThrows(MovimentacaoInvalidaException.class,() -> estoqueService.subtrairEstoque(item.getIdItem(),qtd));
        Assertions.assertEquals(msgEsperada,ex.getMessage());
    }

    @Test
    @DisplayName("Lanca excecao se quantidade subtraida igual a 0")
    public void lancaExcecaoQuantidadeSubtraidaIgualZero(){
        estoque.setEstoqueReal(30f);
        optEstoque = Optional.of(estoque);
        float qtd = 0;
        String msgEsperada = "Quantidade Invalida";

        //Mockando funcoes do repositorio do estoque
        Mockito.when(estoqueRepository.findByIdItem(item.getIdItem())).thenReturn(optEstoque);
        Mockito.when(estoqueRepository.save(estoque)).thenReturn(estoque);

        //testando funcao
        MovimentacaoInvalidaException ex = Assertions.assertThrows(MovimentacaoInvalidaException.class,() -> estoqueService.subtrairEstoque(item.getIdItem(),qtd));
        Assertions.assertEquals(msgEsperada,ex.getMessage());
    }

    @Test
    @DisplayName("Retorna estoque buscando pelo idEstoque")
    public void restornaEstoqueBuscandoPeloIdEstoque(){
        optEstoque = Optional.of(estoque);
        long idEstoqueCorreto = 10;

        //Mockando funcoes do repositorio do estoque
        Mockito.when(estoqueRepository.findById(estoque.getIdEstoque())).thenReturn(optEstoque);

        //testando funcao
        Assertions.assertEquals(estoque,estoqueService.buscarEstoqueById(idEstoqueCorreto));
    }

    @Test
    @DisplayName("Lanca excecao se estoque nao encontrado")
    public void lancaExcecaoSeEstoqueNaoEncontrado(){
        optEstoque = Optional.of(estoque);
        long idEstoqueErrado = 20;
        String msgEsperada = "Estoque nao encontrado";

        //Mockando funcoes do repositorio do estoque
        Mockito.when(estoqueRepository.findById(estoque.getIdEstoque())).thenReturn(optEstoque);

        //testando funcao
        EntityNotFoundException ex = Assertions.assertThrows(EntityNotFoundException.class,() -> estoqueService.buscarEstoqueById(idEstoqueErrado));
        Assertions.assertEquals(msgEsperada,ex.getMessage());
    }
}

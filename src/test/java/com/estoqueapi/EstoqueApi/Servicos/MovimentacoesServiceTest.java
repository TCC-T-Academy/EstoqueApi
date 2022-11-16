package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.Estoque;
import com.estoqueapi.EstoqueApi.Entidades.Itens;
import com.estoqueapi.EstoqueApi.Entidades.Movimentacoes;
import com.estoqueapi.EstoqueApi.Entidades.Usuarios;
import com.estoqueapi.EstoqueApi.Enums.PerfilUsuario;
import com.estoqueapi.EstoqueApi.Exceptions.MovimentacaoInvalidaException;
import com.estoqueapi.EstoqueApi.Repositorios.EstoqueRepository;
import com.estoqueapi.EstoqueApi.Repositorios.ItensRepository;
import com.estoqueapi.EstoqueApi.Repositorios.MovimentacoesRepository;
import com.estoqueapi.EstoqueApi.Repositorios.UsuariosRepository;
import com.estoqueapi.EstoqueApi.Servicos.MovimentacoesService;
import com.estoqueapi.EstoqueApi.Servicos.ValidacoesService;
import org.assertj.core.internal.bytebuddy.dynamic.DynamicType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class MovimentacoesServiceTest {

    private Itens item;
    private Estoque estoque;
    private Movimentacoes m;
    private Usuarios user;

    @Mock
    private MovimentacoesRepository movimentacoesRepository;
    @Mock
    private ItensRepository itensRepository;
    @Mock
    private EstoqueRepository estoqueRepository;
    @Mock
    private UsuariosRepository usuariosRepository;
    @Mock
    private ValidacoesService validacoesService;

    @InjectMocks
    private MovimentacoesService service;


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

        user = new Usuarios("JOSE","asdf", PerfilUsuario.COMUM,"jose@jose");

        m = new Movimentacoes();
        m.setItem(new Itens());
        m.setEstoque(estoque);
        m.setDataMovimentacao(Instant.now());
        m.setQuantidade(25);
        m.setUsuario(user);


        Mockito.when(movimentacoesRepository.save(m)).thenReturn(m);
        Mockito.when(validacoesService.validarMovimentacao(m)).thenReturn(m);

    }

    @Test
    public void retornaMovimentacaoAoSalvar(){

        Assertions.assertNotNull(service.salvar(m));
        Mockito.verify(movimentacoesRepository,Mockito.times(1)).save(m);

    }

}

package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.Estoque;
import com.estoqueapi.EstoqueApi.Entidades.Itens;
import com.estoqueapi.EstoqueApi.Entidades.Movimentacoes;
import com.estoqueapi.EstoqueApi.Entidades.Usuarios;
import com.estoqueapi.EstoqueApi.Exceptions.MovimentacaoInvalidaException;
import com.estoqueapi.EstoqueApi.Repositorios.EstoqueRepository;
import com.estoqueapi.EstoqueApi.Repositorios.ItensRepository;
import com.estoqueapi.EstoqueApi.Repositorios.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class ValidacoesService {

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private ItensService itensService;

    @Autowired
    private EstoqueService estoqueService;
    @Autowired
    private UsuariosService usuariosService;

    public boolean usuarioExiste(Long idUsuario){
        return usuariosRepository.existsById(idUsuario);
    }

    public Movimentacoes validarMovimentacao(Movimentacoes m){
        Usuarios usuario = usuariosService.buscarUsuarioById(m.getUsuario().getIdUsuario());
        Estoque estoque = estoqueService.buscarEstoqueById(m.getEstoque().getIdEstoque());
        Itens item = itensService.consultarItemById(m.getItem().getIdItem());

        if(m.getQuantidade() <= 0){
            throw new MovimentacaoInvalidaException("Quantidade Inválida!");
        }

        /*
        * Validacoes de usuário, quantidade, item ou outras
        * */

        return m;

    }



}

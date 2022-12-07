package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.Usuario;
import com.estoqueapi.EstoqueApi.Repositorios.UsuarioRepository;
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
public class UsuarioServiceTest {

    private Usuario usuario;
    private Optional<Usuario> optUsuario;

    @Mock
    UsuarioRepository usuarioRepository;

    @InjectMocks
    UsuarioService usuarioService;

    @BeforeEach
    void setup(){
        usuario = new Usuario("Jose","1234", "jose@jose");
        usuario.setIdUsuario(1l);
    }

    @Test
    @DisplayName("Retorna usuario buscado pelo id")
    public void retornaUsuarioBuscadoPeloId(){
        long idUsuarioBuscado = 1;
        optUsuario = Optional.of(usuario);

        Mockito.when(usuarioRepository.findById((long) usuario.getIdUsuario())).thenReturn(optUsuario);
        Assertions.assertEquals(usuario, usuarioService.buscarUsuarioById(idUsuarioBuscado));
    }

    @Test
    @DisplayName("Lanca excecao se usuario nao encontrado")
    public void lancaExcecaoSeUsuarioNaoEncontrado(){
        long idUsuarioBuscado = 2;
        String msgEsperada = "Usuario nao encontrado";
        optUsuario = Optional.of(usuario);

        Mockito.when(usuarioRepository.findById((long) usuario.getIdUsuario())).thenReturn(optUsuario);
        EntityNotFoundException ex = Assertions.assertThrows(EntityNotFoundException.class,() -> usuarioService.buscarUsuarioById(idUsuarioBuscado));
        Assertions.assertEquals(msgEsperada, ex.getMessage());
    }

}

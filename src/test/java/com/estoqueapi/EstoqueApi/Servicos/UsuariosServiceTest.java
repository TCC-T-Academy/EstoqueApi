package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.Usuarios;
import com.estoqueapi.EstoqueApi.Enums.PerfilUsuario;
import com.estoqueapi.EstoqueApi.Repositorios.UsuariosRepository;
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
public class UsuariosServiceTest {

    private Usuarios usuario;
    private Optional<Usuarios> optUsuario;

    @Mock
    UsuariosRepository usuariosRepository;

    @InjectMocks
    UsuariosService usuariosService;

    @BeforeEach
    void setup(){
        usuario = new Usuarios("Jose","1234", PerfilUsuario.COMUM,"jose@jose");
        usuario.setIdUsuario(1);
    }

    @Test
    @DisplayName("Retorna usuario buscado pelo id")
    public void retornaUsuarioBuscadoPeloId(){
        long idUsuarioBuscado = 1;
        optUsuario = Optional.of(usuario);

        Mockito.when(usuariosRepository.findById((long) usuario.getIdUsuario())).thenReturn(optUsuario);
        Assertions.assertEquals(usuario,usuariosService.buscarUsuarioById(idUsuarioBuscado));
    }

    @Test
    @DisplayName("Lanca excecao se usuario nao encontrado")
    public void lancaExcecaoSeUsuarioNaoEncontrado(){
        long idUsuarioBuscado = 2;
        String msgEsperada = "Usuario nao encontrado";
        optUsuario = Optional.of(usuario);

        Mockito.when(usuariosRepository.findById((long) usuario.getIdUsuario())).thenReturn(optUsuario);
        EntityNotFoundException ex = Assertions.assertThrows(EntityNotFoundException.class,() -> usuariosService.buscarUsuarioById(idUsuarioBuscado));
        Assertions.assertEquals(msgEsperada, ex.getMessage());
    }

}

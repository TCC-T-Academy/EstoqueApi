package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.Usuarios;
import com.estoqueapi.EstoqueApi.Enums.PerfilUsuario;
import com.estoqueapi.EstoqueApi.Exceptions.AlteracaoNaoPermitidaException;
import com.estoqueapi.EstoqueApi.Repositorios.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class UsuariosService {

    @Autowired
    UsuariosRepository usuariosRepository;

    private boolean usuarioExiste(String email) {
        return usuariosRepository.findByEmail(email).orElse(null) != null;
    }

    public Usuarios consultarByEmail(String email){
       return usuariosRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Usuario nao encontrado"));
    }

    public List<Usuarios> listarUsuarios() {
        return usuariosRepository.findAll();
    }

    public Usuarios buscarUsuarioById(long idUsuario) {
        Usuarios u = usuariosRepository.findById(idUsuario).orElseThrow(() -> new EntityNotFoundException("Usuario nao encontrado"));
        return u;
    }

    public Usuarios salvar(Usuarios usuario) {
        Usuarios vUsuario = validarUsuario(usuario);
        if(vUsuario.getIdUsuario() == 0 && this.usuarioExiste(usuario.getEmail())){
            throw new AlteracaoNaoPermitidaException("Email ja existe");
        }

        return usuariosRepository.save(vUsuario);
    }

    private Usuarios validarUsuario(Usuarios usuarios){

        if(usuarios.getNome().isEmpty() || usuarios.getNome().isBlank()){
            throw new AlteracaoNaoPermitidaException("nome vazio");
        }else if(usuarios.getPerfil() == null){
            throw new AlteracaoNaoPermitidaException("perfil vazio");
        }else if(usuarios.getSenha().isEmpty() || usuarios.getSenha().isBlank()){
            throw new AlteracaoNaoPermitidaException("senha vazia");
        }else if(usuarios.getSenha().isEmpty() || usuarios.getSenha().isBlank()){
            throw new AlteracaoNaoPermitidaException("senha vazia");
        }else {
            return usuarios;
        }

    }



    public Usuarios alterar(String email, Usuarios usuario) {

        Usuarios vUsuario = consultarByEmail(email);

        if(!usuario.getNome().isEmpty() && !usuario.getNome().isBlank()){
            vUsuario.setNome(usuario.getNome());
        }
        if(usuario.getPerfil() != null){
            vUsuario.setPerfil(usuario.getPerfil());
        }
        if(!usuario.getSenha().isEmpty() && !usuario.getSenha().isBlank()){
            vUsuario.setSenha(usuario.getSenha());
        }
        if(!usuario.getEmail().isEmpty() && !usuario.getEmail().isBlank()){
            vUsuario.setEmail(usuario.getEmail());
        }

        return this.salvar(vUsuario);

    }

    public Usuarios desabilitarUsuario(String email) {

        Usuarios vUsuario = this.consultarByEmail(email);

        vUsuario.setPerfil(PerfilUsuario.DESABILITADO);

        return this.salvar(vUsuario);
    }
}

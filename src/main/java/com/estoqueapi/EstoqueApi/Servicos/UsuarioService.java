package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.Role;
import com.estoqueapi.EstoqueApi.Entidades.Usuario;
import com.estoqueapi.EstoqueApi.Exceptions.AcaoNaoPermitidaException;
import com.estoqueapi.EstoqueApi.Repositorios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoleService roleService;
    @Autowired
    BCryptPasswordEncoder b;

    private boolean usuarioExiste(String email) {
        return usuarioRepository.findByEmail(email).orElse(null) != null;
    }

    public Usuario consultarByEmail(String email){
       return usuarioRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarUsuarioById(long idUsuario) {
        Usuario u = usuarioRepository.findById(idUsuario).orElseThrow(() -> new EntityNotFoundException("Usuario nao encontrado"));
        return u;
    }

    public Usuario salvar(Usuario usuario) {
        Usuario vUsuario = validarUsuario(usuario);
        if(vUsuario.getIdUsuario() == 0 && this.usuarioExiste(usuario.getEmail())){
            throw new AcaoNaoPermitidaException("Email ja existe");
        }

        return usuarioRepository.save(vUsuario);
    }

    private Usuario validarUsuario(Usuario usuarios){
        HashSet<Role> roles = new HashSet<Role>();
            roles.add(roleService.consultarByAuthority(usuarios.getRoles().stream().findFirst().orElseThrow(() -> new EntityNotFoundException("Role não encontrada")).getAuthority()));
            usuarios.setRoles(roles);
            usuarios.setSenha(b.encode(usuarios.getPassword()));
        if(usuarios.getNome().isEmpty() || usuarios.getNome().isBlank()){
            throw new AcaoNaoPermitidaException("nome vazio");
        }else if(usuarios.getSenha().isEmpty() || usuarios.getSenha().isBlank()){
            throw new AcaoNaoPermitidaException("senha vazia");
        }else {
            return usuarios;
        }
    }



    public Usuario alterar(String email, Usuario usuario) {

        Usuario vUsuario = consultarByEmail(email);

        if(!usuario.getNome().isEmpty() && !usuario.getNome().isBlank()){
            vUsuario.setNome(usuario.getNome());
        }
        if(!usuario.getSenha().isEmpty() && !usuario.getSenha().isBlank()){
            vUsuario.setSenha(usuario.getSenha());
        }
        if(!usuario.getEmail().isEmpty() && !usuario.getEmail().isBlank()){
            vUsuario.setEmail(usuario.getEmail());
        }

        return this.salvar(vUsuario);

    }

    public Usuario desabilitarUsuario(String email) {

        Usuario vUsuario = this.consultarByEmail(email);


        return this.salvar(vUsuario);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario user = this.consultarByEmail(username);
        return user;
    }
}

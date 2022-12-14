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
import java.util.regex.Pattern;

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

    public static boolean isValidEmail(String email) {
        // Define a padrão do endereço de e-mail
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

        // Verifique se o endereço de e-mail coincide com o padrão
        return email.matches(regex);
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
        if(!this.isValidEmail(usuario.getEmail())){
            throw new AcaoNaoPermitidaException("O endereço de e-mail não é válido.");
        }
        return usuarioRepository.save(vUsuario);
    }

    private Usuario validarUsuario(Usuario usuarios){
        HashSet<Role> roles = new HashSet<Role>();
            roles.add(roleService.consultarByAuthority(usuarios.getRoles().stream().findFirst().orElseThrow(() -> new EntityNotFoundException("Role não encontrada")).getAuthority()));
            usuarios.setRoles(roles);
            if(isValidPassword(usuarios.getPassword())){
                usuarios.setSenha(b.encode(usuarios.getPassword()));
            }
        if(usuarios.getNome().isEmpty() || usuarios.getNome().isBlank()){
            throw new AcaoNaoPermitidaException("nome vazio");
        }else if(usuarios.getSenha().isEmpty() || usuarios.getSenha().isBlank()){
            throw new AcaoNaoPermitidaException("senha vazia");
        }else {
            return usuarios;
        }
    }

    public static boolean isValidPassword(String password) {
        // Verifica se a string tem entre 8 e 12 caracteres
        if (password.length() < 8 || password.length() > 12) {
            throw new AcaoNaoPermitidaException("Senha precisa ter entre 8 e 12 Caracteres.");
        }

        // Verifica se a string contém apenas letras e números
        if (!containsLetterAndNumber(password)) {
            throw new AcaoNaoPermitidaException("Senha precisa conter letras e números.");
        }

        // Verifica se a string contém ao menos uma letra maiúscula
        if (!password.matches(".*[A-Z].*")) {
            throw new AcaoNaoPermitidaException("Senha precisa ter ao menos uma letra maiúscula.");
        }

        // Verifica se a string contém ao menos um caractere especial
        if (!possuiCaractereEspecial(password)) {
            throw new AcaoNaoPermitidaException("Senha precisa ter ao menos um caracter especial.");
        }

        return true;
    }

    public static boolean possuiCaractereEspecial(String str) {
        // Cria a expressão regular
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");

        // Verifica se a string possui algum caractere que não seja uma letra de A a Z,
        // nem de a a z, nem um dígito de 0 a 9
        return pattern.matcher(str).find();
    }

    public static boolean containsLetterAndNumber(String s) {
        // Verifica se a string é vazia
        if (s == null || s.isEmpty()) {
            return false;
        }
        // Flag que indica se a string contém uma letra
        boolean containsLetter = false;

        // Flag que indica se a string contém um número
        boolean containsNumber = false;
        // Percorre cada caractere da string
        for (char c : s.toCharArray()) {
            // Verifica se o caractere é uma letra
            if (Character.isLetter(c)) {
                containsLetter = true;
            }
            // Verifica se o caractere é um número
            if (Character.isDigit(c)) {
                containsNumber = true;
            }
            // Se a string já contém pelo menos uma letra e um número, pode retornar true
            if (containsLetter && containsNumber) {
                return true;
            }
        }
        // Se chegou até aqui, a string não contém pelo menos uma letra e um número
        return false;
    }

    public Usuario alterar(String email, Usuario usuario) {

        Usuario vUsuario = consultarByEmail(email);

        if(!usuario.getNome().isEmpty() && !usuario.getNome().isBlank()){
            vUsuario.setNome(usuario.getNome());
        }
        if(usuario.getSenha() != null && !usuario.getSenha().isEmpty()){
            if(isValidPassword(usuario.getPassword())){
                vUsuario.setSenha(b.encode(usuario.getSenha()));
            }
        }
        if(!usuario.getEmail().isEmpty() && !usuario.getEmail().isBlank()){
            vUsuario.setEmail(usuario.getEmail());
        }

        vUsuario.setRoles(usuario.getRoles());

        return usuarioRepository.save(vUsuario);
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

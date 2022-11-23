package com.estoqueapi.EstoqueApi.Controllers;

import com.estoqueapi.EstoqueApi.Entidades.Usuario;
import com.estoqueapi.EstoqueApi.Servicos.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<Usuario>> consultarUsuarios(){
        return ResponseEntity.status(HttpStatus.OK).body(usuarioService.listarUsuarios());
    }

    @PostMapping("/novo")
    public ResponseEntity<Usuario> salvar(@RequestBody Usuario usuario){
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.salvar(usuario));
    }

    @PutMapping("/alterar/{email}")
    public ResponseEntity<Usuario> alterar(@PathVariable("email") String email, @RequestBody Usuario usuario){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(usuarioService.alterar(email,usuario));
    }

    @PutMapping("/desabilitar/{email}")
    public ResponseEntity<Usuario> desabilitar(@PathVariable("email") String email){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(usuarioService.desabilitarUsuario(email));
    }

}

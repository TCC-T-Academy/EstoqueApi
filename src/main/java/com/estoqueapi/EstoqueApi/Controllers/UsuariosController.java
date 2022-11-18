package com.estoqueapi.EstoqueApi.Controllers;

import com.estoqueapi.EstoqueApi.Entidades.Usuarios;
import com.estoqueapi.EstoqueApi.Servicos.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("usuarios")
public class UsuariosController {

    @Autowired
    private UsuariosService usuariosService;

    @GetMapping
    public ResponseEntity<List<Usuarios>> consultarUsuarios(){
        return ResponseEntity.status(HttpStatus.FOUND).body(usuariosService.listarUsuarios());
    }

    @PostMapping("/novo")
    public ResponseEntity<Usuarios> salvar(@RequestBody Usuarios usuario){
        return ResponseEntity.status(HttpStatus.CREATED).body(usuariosService.salvar(usuario));
    }

    @PutMapping("/alterar/{email}")
    public ResponseEntity<Usuarios> alterar(@PathVariable("email") String email, @RequestBody Usuarios usuario){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(usuariosService.alterar(email,usuario));
    }

    @PutMapping("/desabilitar/{email}")
    public ResponseEntity<Usuarios> desabilitar(@PathVariable("email") String email){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(usuariosService.desabilitarUsuario(email));
    }

}

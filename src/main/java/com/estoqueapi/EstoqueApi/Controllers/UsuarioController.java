package com.estoqueapi.EstoqueApi.Controllers;

import com.estoqueapi.EstoqueApi.Dtos.UsuarioNovoDTO;
import com.estoqueapi.EstoqueApi.Dtos.UsuarioPublicoDTO;
import com.estoqueapi.EstoqueApi.Entidades.Usuario;
import com.estoqueapi.EstoqueApi.Mapper.Mapper;
import com.estoqueapi.EstoqueApi.Servicos.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private Mapper mapper;

    @GetMapping
    public ResponseEntity<List<UsuarioPublicoDTO>> consultarUsuarios(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(usuarioService.listarUsuarios()
                        .stream()
                        .map(mapper::toUsuarioPublicoDTO)
                        .collect(Collectors.toList()));
    }

    @GetMapping("/{email}")
    public ResponseEntity<UsuarioPublicoDTO> consultarUsuarioPorEmail(@PathVariable("email")String email){
        return ResponseEntity.status(HttpStatus.OK).body(mapper.toUsuarioPublicoDTO(usuarioService.consultarByEmail(email)));
    }

    @PostMapping("/novo")
    public ResponseEntity<UsuarioPublicoDTO> salvar(@RequestBody UsuarioNovoDTO usuarioNovoDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toUsuarioPublicoDTO(usuarioService.salvar(mapper.toUsuario(usuarioNovoDTO))));
    }

    //Alterar para DTO
    @PutMapping("/alterar/{email}")
    public ResponseEntity<Usuario> alterar(@PathVariable("email") String email, @RequestBody Usuario usuario){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(usuarioService.alterar(email,usuario));
    }

    //Alterar de perfil para role
    @PutMapping("/desabilitar/{email}")
    public ResponseEntity<Usuario> desabilitar(@PathVariable("email") String email){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(usuarioService.desabilitarUsuario(email));
    }
}

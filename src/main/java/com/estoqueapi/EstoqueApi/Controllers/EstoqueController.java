package com.estoqueapi.EstoqueApi.Controllers;

import com.estoqueapi.EstoqueApi.Entidades.Estoque;
import com.estoqueapi.EstoqueApi.Repositorios.EstoqueRepository;
import com.estoqueapi.EstoqueApi.Servicos.EstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("estoque")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

    @Autowired
    private EstoqueRepository estoqueRepository;

    @GetMapping
    public ResponseEntity<List<Estoque>> consultarEstoque(){
        return ResponseEntity.status(HttpStatus.OK).body(estoqueService.listarEstoque());
    }

    @GetMapping("/baixo")
    public ResponseEntity<List<Estoque>> consultarEstoqueBaixo(){
        return ResponseEntity.status(HttpStatus.OK).body(estoqueService.consultarEstoqueAbaixoLimite());
    }

    @GetMapping("/{idItem}")
    public ResponseEntity<Estoque> consultarEstoqueIdItem(@PathVariable("idItem") long idItem){
        return ResponseEntity.status(HttpStatus.OK).body(estoqueService.buscarEstoqueIdItem(idItem));
    }
}

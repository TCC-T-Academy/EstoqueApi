package com.estoqueapi.EstoqueApi.Controllers;

import com.estoqueapi.EstoqueApi.Entidades.Estoque;
import com.estoqueapi.EstoqueApi.Servicos.EstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.PrimitiveIterator;

@RestController
@RequestMapping("estoque")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

    @GetMapping
    public ResponseEntity<List<Estoque>> consultarEstoque(){
        return ResponseEntity.status(HttpStatus.FOUND).body(estoqueService.listarEstoque());
    }

    @GetMapping("/{idItem}")
    public ResponseEntity<Estoque> consultarEstoqueIdItem(@PathVariable("idItem") long idItem){
        return ResponseEntity.status(HttpStatus.FOUND).body(estoqueService.buscarEstoqueIdItem(idItem));
    }

    @PutMapping("/{idEstoque}")
    public ResponseEntity<Estoque> alterarEstoque(@PathVariable("idEstoque") long idEstoque, @RequestBody Estoque estoque){
        return ResponseEntity.status(HttpStatus.FOUND).body(estoqueService.alterarEstoque(idEstoque,estoque));
    }


}

package com.estoqueapi.EstoqueApi.Controllers;

import com.estoqueapi.EstoqueApi.Entidades.Itens;
import com.estoqueapi.EstoqueApi.Repositorios.ItensRepository;
import com.estoqueapi.EstoqueApi.Servicos.ItensService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("itens")
public class ItensController {

    @Autowired
    private ItensService itensService;

    @GetMapping("/{idItem}")
    public ResponseEntity<Itens> consultarItemById(@PathVariable("idItem") long idItem){
        return ResponseEntity.status(HttpStatus.FOUND).body(itensService.consultarItemById(idItem));
    }

    @GetMapping
    public ResponseEntity<List<Itens>> consultarItens(){
        return ResponseEntity.status(HttpStatus.FOUND).body(itensService.listarItens());
    }

    @PostMapping
    public ResponseEntity<Itens> salvarItem (@RequestBody Itens item){
        return ResponseEntity.status(HttpStatus.CREATED).body(itensService.salvar(item));
    }

    @PutMapping("/{idItem}")
    public ResponseEntity<Itens> salvarItem (@PathVariable("idItem") long idItem, @RequestBody Itens item){
        return ResponseEntity.status(HttpStatus.OK).body(itensService.alterarItem(idItem, item));
    }

}

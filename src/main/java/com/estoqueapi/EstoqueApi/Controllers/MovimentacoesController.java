package com.estoqueapi.EstoqueApi.Controllers;

import com.estoqueapi.EstoqueApi.Entidades.Estoque;
import com.estoqueapi.EstoqueApi.Entidades.Movimentacoes;
import com.estoqueapi.EstoqueApi.Servicos.MovimentacoesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/movimentacoes")
public class MovimentacoesController {

    @Autowired
    MovimentacoesService movimentacoesService;

    @GetMapping
    public ResponseEntity<List<Movimentacoes>> consultar(){
        return ResponseEntity.status(HttpStatus.FOUND).body(movimentacoesService.consultar());
    }

    @GetMapping("/{idItem}")
    public ResponseEntity<List<Movimentacoes>> consultar(@PathVariable ("idItem") Long idItem){
        return ResponseEntity.status(HttpStatus.FOUND).body(movimentacoesService.consultarByIdItem(idItem));
    }

    //Pensar sobre remoçao
    /*    @PostMapping("/varios")
    public ResponseEntity<List<Movimentacoes>> salvarVarios(@RequestBody List<Movimentacoes> list){
        return ResponseEntity.status(HttpStatus.CREATED).body(movimentacoesService.salvarVarios(list));
    }*/

    @PostMapping("/entrada")
    public ResponseEntity<Movimentacoes> entrada(@RequestBody Movimentacoes movimentacoes){
        return ResponseEntity.status(HttpStatus.CREATED).body(movimentacoesService.entradaItem(movimentacoes));
    }

    @PostMapping("/saida")
    public ResponseEntity<Movimentacoes> saida(@RequestBody Movimentacoes movimentacoes){
        return ResponseEntity.status(HttpStatus.CREATED).body(movimentacoesService.saidaItem(movimentacoes));
    }

}

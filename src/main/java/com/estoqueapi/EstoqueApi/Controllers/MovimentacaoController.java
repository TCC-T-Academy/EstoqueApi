package com.estoqueapi.EstoqueApi.Controllers;

import com.estoqueapi.EstoqueApi.Entidades.Movimentacao;
import com.estoqueapi.EstoqueApi.Servicos.MovimentacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movimentacoes")
public class MovimentacaoController {

    @Autowired
    MovimentacaoService movimentacaoService;

    @GetMapping
    public ResponseEntity<List<Movimentacao>> consultar(){
        return ResponseEntity.status(HttpStatus.FOUND).body(movimentacaoService.consultar());
    }

    @GetMapping("/{idItem}")
    public ResponseEntity<List<Movimentacao>> consultar(@PathVariable ("idItem") Long idItem){
        return ResponseEntity.status(HttpStatus.FOUND).body(movimentacaoService.consultarByIdItem(idItem));
    }

    //Pensar sobre remoçao
    /*    @PostMapping("/varios")
    public ResponseEntity<List<Movimentacoes>> salvarVarios(@RequestBody List<Movimentacoes> list){
        return ResponseEntity.status(HttpStatus.CREATED).body(movimentacoesService.salvarVarios(list));
    }*/

    @PostMapping("/entrada")
    public ResponseEntity<Movimentacao> entrada(@RequestBody Movimentacao movimentacoes){
        return ResponseEntity.status(HttpStatus.CREATED).body(movimentacaoService.entradaItem(movimentacoes));
    }

    @PostMapping("/saida")
    public ResponseEntity<Movimentacao> saida(@RequestBody Movimentacao movimentacoes){
        return ResponseEntity.status(HttpStatus.CREATED).body(movimentacaoService.saidaItem(movimentacoes));
    }

}

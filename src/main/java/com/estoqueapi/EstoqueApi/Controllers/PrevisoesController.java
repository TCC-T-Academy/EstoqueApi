package com.estoqueapi.EstoqueApi.Controllers;

import com.estoqueapi.EstoqueApi.Entidades.Reservas;
import com.estoqueapi.EstoqueApi.Servicos.PrevisoesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.estoqueapi.EstoqueApi.Entidades.Previsoes;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@RestController
@RequestMapping("/previsoes")
public class PrevisoesController {

    @Autowired
    private PrevisoesService service;

    //Rota para listar todas as previsões cadastradas
    @GetMapping("")
    public Iterable<Previsoes> listarPrevisoes(){
        return service.listarPrevisoes();
    }

    //Rota para cadastrar novas previsões *Verificar validações*
    @PostMapping("")
    public ResponseEntity<Previsoes> cadastrarPrevisoes(@Valid @RequestBody Previsoes pr){
        Previsoes prev = service.cadastrarPrevisoes(pr);
        return ResponseEntity.status(HttpStatus.CREATED).body(prev);
    }
    //Filtrar por ID da previsão
    @GetMapping("/{idPrevisao}")
    public ResponseEntity<Previsoes> filtrarId(@PathVariable ("idPrevisao") Long idPrevisao) {
        return ResponseEntity.ok().body(service.filtrarId(idPrevisao));
    }
    //Filtrar por OC realizada ou não
    @GetMapping("/status/{finalizada}")
    public ResponseEntity<List<Previsoes>> findByFinalizada(@PathVariable ("finalizada") boolean finalizada){
        List<Previsoes> listar = (List<Previsoes>) service.findByFinalizada(finalizada);
        return ResponseEntity.status(HttpStatus.OK).body(listar);
    }
    //Alterar previsões
    @PutMapping("/alterar/{idPrevisao}")
    public ResponseEntity<Object> alterarPrevisao(@PathVariable ("idPrevisao") Long idPrevisao,
    @Valid @RequestBody Previsoes previsoes) {
        return ResponseEntity.status(HttpStatus.OK).body(service.alterarPrevisao(idPrevisao, previsoes));
    }

    //Excluir previsão
    @DeleteMapping("/excluir/{idPrevisao}")
    public ResponseEntity<Void> excluirPrevisao(@PathVariable ("idPrevisoes") Long idPrevisoes){
        service.excluirPrevisao(idPrevisoes);
        return ResponseEntity.noContent().build();
    }
}

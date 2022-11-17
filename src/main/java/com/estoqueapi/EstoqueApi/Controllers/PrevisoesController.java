package com.estoqueapi.EstoqueApi.Controllers;

import com.estoqueapi.EstoqueApi.Servicos.PrevisoesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.estoqueapi.EstoqueApi.Entidades.Previsoes;

import javax.validation.Valid;
import java.util.List;


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
    public ResponseEntity<Previsoes> filtrarId(@PathVariable ("idPrevisao") long idPrevisao) {
        return ResponseEntity.ok().body(service.filtrarId(idPrevisao));
    }
    // Filtra previsões com 2 opções: Data com vencimento anterior ou a partir de hoje / Finalizada (true ou false)
    @GetMapping("/iditem/{iditem}")
    public ResponseEntity<List<Previsoes>> consultarPrevisaoByIdItem(@PathVariable("iditem") Long iditem){
        List<Previsoes> lista = service.consultarByIdItem(iditem);
        return ResponseEntity.status(HttpStatus.OK).body(lista);
    }
    //Filtrar por ordem de compra/produção realizadas ou não
    @GetMapping("/status/{finalizada}")
    public ResponseEntity<List<Previsoes>> findByFinalizada(@PathVariable ("finalizada") boolean finalizada){
        List<Previsoes> listar = service.findByFinalizada(finalizada);
        return ResponseEntity.status(HttpStatus.OK).body(listar);
    }

    //Filtrar pela data as previsões que já venceram
    @GetMapping("/vencidas")
    public ResponseEntity<List<Previsoes>> consultaVencidos(){
        List<Previsoes> listar = service.findByDataPrevistaVencidos(true);
        return ResponseEntity.status(HttpStatus.OK).body(listar);
    }

    //Filtrar pela data as previsões que ainda serão realizadas (pendentes)
    @GetMapping("/pendentes")
    public ResponseEntity<List<Previsoes>> findByDataPrevistaVencidos(){
        List<Previsoes> listar = service.findByDataPrevistaVencidos(false);
        return ResponseEntity.status(HttpStatus.OK).body(listar);
    }

    @GetMapping("/vencimentoefinalizada/{vencida}/{finalizada}")
    public ResponseEntity<List<Previsoes>> findByDataPrevistaVencidos(@PathVariable("vencida") boolean vencida, @PathVariable("finalizada") boolean finalizada){
        List<Previsoes> listar = service.findByDataPrevistaFinalizada(vencida, finalizada);
        return ResponseEntity.status(HttpStatus.OK).body(listar);
    }

    //Alterar previsões
    @PutMapping("/alterar/{idPrevisao}")
    public ResponseEntity<Object> alterarPrevisao(@PathVariable ("idPrevisao") Long idPrevisao,
                                                  @Valid @RequestBody Previsoes previsoes) {
        return ResponseEntity.status(HttpStatus.OK).body(service.alterarPrevisao(idPrevisao, previsoes));
    }

    //Excluir previsão (só pode ser excluído quando finalizada = false)
    @DeleteMapping("/excluir/{idPrevisao}")
    public ResponseEntity<String> excluirPrevisao(@PathVariable ("idPrevisao") long idPrevisao){
        service.excluirPrevisao(idPrevisao);
        return ResponseEntity.status(HttpStatus.OK).body("Removido com sucesso");
    }
}

package com.estoqueapi.EstoqueApi.Controllers;

import com.estoqueapi.EstoqueApi.Dtos.MovimentacaoDTO;
import com.estoqueapi.EstoqueApi.Dtos.MovimentacaoNovaDTO;
import com.estoqueapi.EstoqueApi.Dtos.PrevisaoDTO;
import com.estoqueapi.EstoqueApi.Dtos.PrevisaoNovaDTO;
import com.estoqueapi.EstoqueApi.Entidades.Reserva;
import com.estoqueapi.EstoqueApi.Servicos.PrevisaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.estoqueapi.EstoqueApi.Mapper.Mapper;

import com.estoqueapi.EstoqueApi.Entidades.Previsao;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/previsoes")
public class PrevisaoController {

    @Autowired
    private PrevisaoService service;
    @Autowired
    private Mapper mapper;

    //Rota para listar todas as previsões cadastradas
    @GetMapping("")
    public Iterable<Previsao> listarPrevisoes(){
        return service.listarPrevisoes();
    }

    @GetMapping("/paginada")
    public ResponseEntity<Page<Previsao>> consultaPaginada(Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(service.consultaPaginada(pageable));
    }


    // Rota para cadastrar nova previsão
    @PostMapping("")
    public ResponseEntity<PrevisaoDTO> cadastrarPrevisao(@RequestBody PrevisaoNovaDTO previsaoNovaDTO){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.toPrevisaoDTO(service.cadastrarPrevisoes(mapper.toPrevisao(previsaoNovaDTO))));
    }
    //Filtrar por ID da previsão
    @GetMapping("/{idPrevisao}")
    public ResponseEntity<Previsao> filtrarId(@PathVariable ("idPrevisao") long idPrevisao) {
        return ResponseEntity.status(HttpStatus.OK).body(service.filtrarId(idPrevisao));
    }

    // Filtra previsões com 2 opções: Data com vencimento anterior ou a partir de hoje / Finalizada (true ou false)
    @GetMapping("/iditem/{iditem}")
    public ResponseEntity<List<Previsao>> consultarPrevisaoByIdItem(@PathVariable("iditem") Long iditem){
        List<Previsao> lista = service.consultarByIdItem(iditem);
        return ResponseEntity.status(HttpStatus.OK).body(lista);
    }
    //Filtrar por ordem de compra/produção realizadas ou não
    @GetMapping("/status/{finalizada}")
    public ResponseEntity<List<Previsao>> findByFinalizada(@PathVariable ("finalizada") boolean finalizada){
        List<Previsao> listar = service.findByFinalizada(finalizada);
        return ResponseEntity.status(HttpStatus.OK).body(listar);
    }

    //Filtrar pela data as previsões que já venceram
    @GetMapping("/vencidas")
    public ResponseEntity<List<Previsao>> consultaVencidos(){
        List<Previsao> listar = service.findByDataPrevistaVencidos(true);
        return ResponseEntity.status(HttpStatus.OK).body(listar);
    }

    //Filtrar pela data as previsões que ainda serão realizadas (pendentes)
    @GetMapping("/pendentes")
    public ResponseEntity<List<Previsao>> findByDataPrevistaVencidos(){
        List<Previsao> listar = service.findByDataPrevistaVencidos(false);
        return ResponseEntity.status(HttpStatus.OK).body(listar);
    }

    @GetMapping("/vencimentoefinalizada/{vencida}/{finalizada}")
    public ResponseEntity<List<Previsao>> findByDataPrevistaVencidos(@PathVariable("vencida") boolean vencida, @PathVariable("finalizada") boolean finalizada){
        List<Previsao> listar = service.findByDataPrevistaFinalizada(vencida, finalizada);
        return ResponseEntity.status(HttpStatus.OK).body(listar);
    }

    //Alterar previsões
    @PutMapping("/alterar/{idPrevisao}")
    public ResponseEntity<Object> alterarPrevisao(@PathVariable ("idPrevisao") Long idPrevisao,
                                                  @Valid @RequestBody Previsao previsao) {
        return ResponseEntity.status(HttpStatus.OK).body(service.alterarPrevisao(idPrevisao, previsao));
    }

    //Excluir previsão (só pode ser excluído quando finalizada = false)
    @DeleteMapping("/excluir/{idPrevisao}")
    public ResponseEntity<String> excluirPrevisao(@PathVariable ("idPrevisao") long idPrevisao){
        service.excluirPrevisao(idPrevisao);
        return ResponseEntity.status(HttpStatus.OK).body("Removido com sucesso");
    }
    @GetMapping("/hoje")
    public ResponseEntity<List<Previsao>> consultaVencimentoHoje(){
        return ResponseEntity.status(HttpStatus.OK).body(service.consultarVencimentoHoje());
    }

}

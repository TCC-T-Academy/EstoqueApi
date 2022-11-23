package com.estoqueapi.EstoqueApi.Controllers;

import com.estoqueapi.EstoqueApi.Dtos.MovimentacaoDTO;
import com.estoqueapi.EstoqueApi.Dtos.MovimentacaoNovaDTO;
import com.estoqueapi.EstoqueApi.Entidades.Movimentacao;
import com.estoqueapi.EstoqueApi.Mapper.Mapper;
import com.estoqueapi.EstoqueApi.Servicos.MovimentacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/movimentacoes")
public class MovimentacaoController {

    @Autowired
    MovimentacaoService movimentacaoService;
    @Autowired
    private Mapper mapper;

    @GetMapping
    public ResponseEntity<List<MovimentacaoDTO>> consultar(){
        /*
        * List<Movimentacao> list = movimentacaoService.consultar();
        * List<MovimentacaoDTO> listDTO = new ArrayList<>();
        * list.forEach(movimentacao -> listDTO.add(mapper.toMovimentacaoDto(movimentacao)));
        * return  ResponseEntity.status(HttpStatus.OK).body(listDTO);
        *
        * RETORNO OTIMIZADO PARA RECURSO ABAIXO:
        * */

        return ResponseEntity.status(HttpStatus.OK)
                .body(movimentacaoService.consultar()
                        .stream()
                        .map(mapper::toMovimentacaoDto)
                        .collect(Collectors.toList()));
    }

    @GetMapping("/{idItem}")
    public ResponseEntity<List<MovimentacaoDTO>> consultar(@PathVariable ("idItem") Long idItem){
        return ResponseEntity.status(HttpStatus.OK)
                .body(movimentacaoService.consultarByIdItem(idItem)
                        .stream()
                        .map(mapper::toMovimentacaoDto)
                        .collect(Collectors.toList()));
    }

    @PostMapping("/entrada")
    public ResponseEntity<MovimentacaoNovaDTO> entrada(@RequestBody MovimentacaoNovaDTO movimentacaoNovaDTO){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.toMovimentacaoNovaDTO(movimentacaoService.entradaItem(mapper.toMovimentacao(movimentacaoNovaDTO))));
    }

    @PostMapping("/saida")
    public ResponseEntity<MovimentacaoNovaDTO> saida(@RequestBody MovimentacaoNovaDTO movimentacaoNovaDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toMovimentacaoNovaDTO(movimentacaoService.saidaItem(mapper.toMovimentacao(movimentacaoNovaDTO))));
    }

}

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

    // Rota para cadastrar nova previsão
    @PostMapping("")
    public ResponseEntity<PrevisaoDTO> cadastrarPrevisao(@RequestBody PrevisaoNovaDTO previsaoNovaDTO){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.toPrevisaoDTO(service.cadastrarPrevisoes(mapper.toPrevisao(previsaoNovaDTO))));
    }

    // Filtra previsões por iditem
    @GetMapping("/iditem/{iditem}")
    public ResponseEntity<List<Previsao>> consultarPrevisaoByIdItem(@PathVariable("iditem") Long iditem){
        List<Previsao> lista = service.consultarByIdItem(iditem);
        return ResponseEntity.status(HttpStatus.OK).body(lista);
    }

    //Alterar previsões
    @PutMapping("/alterar/{idPrevisao}")
    public ResponseEntity<Object> alterarPrevisao(@PathVariable ("idPrevisao") Long idPrevisao,
                                                  @Valid @RequestBody PrevisaoNovaDTO previsaoNovaDTO) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(mapper.toPrevisaoDTO(service.alterarPrevisao(idPrevisao, mapper.toPrevisao(previsaoNovaDTO))));
    }

    //Excluir previsão (só pode ser excluído quando finalizada = false)
    @DeleteMapping("/excluir/{idPrevisao}")
    public ResponseEntity<String> excluirPrevisao(@PathVariable ("idPrevisao") long idPrevisao){
        service.excluirPrevisao(idPrevisao);
        return ResponseEntity.status(HttpStatus.OK).body("Removido com sucesso");
    }
}

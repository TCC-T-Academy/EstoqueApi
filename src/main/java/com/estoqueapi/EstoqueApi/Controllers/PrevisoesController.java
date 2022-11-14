package com.estoqueapi.EstoqueApi.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.estoqueapi.EstoqueApi.Entidades.Previsoes;
import com.estoqueapi.EstoqueApi.Repositorios.PrevisoesRepository;

import java.util.Optional;


@RestController
@RequestMapping("/previsoes")
public class PrevisoesController {

    @Autowired
    private PrevisoesRepository acao;

    // Cadastro de novas previsões
    @PostMapping("")
    public Previsoes cadastraPrevisoes(@RequestBody Previsoes obj) {
        return obj;
    }
    
    // Listar todas as previsões
    @GetMapping("")
    public Iterable<Previsoes> listaPrevisoes() {
        return acao.findAll();
    }

    //Filtrar por ID da previsão
    @GetMapping("/{idPrevisao}")
    public Optional<Previsoes> filtrarID(@PathVariable long idPrevisao){
        return acao.findById(idPrevisao);
    }

    //Filtrar por OC realizada ou não
    @GetMapping("/status/{finalizada}")
    public Iterable<Previsoes> statusPrevisoes(@PathVariable boolean finalizada){
        return acao.findByFinalizada(finalizada);
    }
    // Alterar previsão
    @PutMapping("/alterar")
    public Previsoes alterarPrevisoes(@RequestBody Previsoes obj) {
        return acao.save(obj);
    }

    // Excluir previsão     VERIFICAR VALIDAÇÃO PARA EXCLUSÃO
    @DeleteMapping("/deletar/{id_previsao}")
    public void deletarPrevisoes(@PathVariable long id_previsao) {
        acao.deleteById(id_previsao);
    }

}

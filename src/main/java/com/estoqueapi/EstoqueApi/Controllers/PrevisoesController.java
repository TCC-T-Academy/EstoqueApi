package com.estoqueapi.EstoqueApi.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.estoqueapi.EstoqueApi.Entidades.Previsoes;
import com.estoqueapi.EstoqueApi.Repositorios.PrevisoesRepository;



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

package com.estoqueapi.EstoqueApi.Controllers;

import com.estoqueapi.EstoqueApi.Entidades.Estoque;
import com.estoqueapi.EstoqueApi.Entidades.LogFuturo;
import com.estoqueapi.EstoqueApi.Servicos.LogFuturoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("log")
public class LogFuturoController {

    @Autowired
    LogFuturoService logFuturoService;

    @GetMapping("/{idItem}")
    public ResponseEntity<List<LogFuturo>> consultarLogIdItem(@PathVariable("idItem") long idItem){
        return ResponseEntity.status(HttpStatus.OK).body(logFuturoService.buscarLogIdItem(idItem));
    }
}

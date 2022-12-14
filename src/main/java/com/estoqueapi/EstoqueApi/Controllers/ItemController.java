package com.estoqueapi.EstoqueApi.Controllers;

import com.estoqueapi.EstoqueApi.Entidades.Item;
import com.estoqueapi.EstoqueApi.Servicos.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("itens")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/{idItem}")
    public ResponseEntity<Item> consultarItemById(@PathVariable("idItem") long idItem){
        return ResponseEntity.status(HttpStatus.OK).body(itemService.consultarItemById(idItem));
    }
}

package com.estoqueapi.EstoqueApi.Controllers;

import com.estoqueapi.EstoqueApi.Entidades.Reservas;
import com.estoqueapi.EstoqueApi.Servicos.ReservasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/reservas") //http://localhost:8081/reservas
public class ReservasController {

    @Autowired
    ReservasService service;
    @GetMapping
    public ResponseEntity<List<Reservas>> consultarReservas(){
        List<Reservas> lista = service.consultar();
        return ResponseEntity.status(HttpStatus.OK).body(lista);
    }

    @GetMapping("/{idreserva}")
    public ResponseEntity<Reservas> consultarById(@PathVariable("idreserva") Long idreserva){
        return  ResponseEntity.ok().body(service.consultarById(idreserva));
    }

    /*@GetMapping("/ConsultaByIdItem/{iditem}")
    public ResponseEntity<Object> consultarReservasByIdItem(@PathVariable("iditem") Long iditem){
        Object lista = service.consultarByIdItem(iditem);
        return ResponseEntity.status(HttpStatus.OK).body(lista);
    }*/

    @PostMapping
    public ResponseEntity<Reservas> salvar(@Valid @RequestBody Reservas reservas){
        Reservas res = service.salvar(reservas);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/{idreserva}")
    public ResponseEntity<Object> alterar(
            @PathVariable("idreserva") Long idreserva,
            @Valid @RequestBody Reservas reservas){
        return ResponseEntity.status(HttpStatus.OK).body(service.alterar(idreserva, reservas));
    }

    @DeleteMapping("/{idreserva}")
    public ResponseEntity<Void> excluir(@PathVariable("idreserva") Long idreserva) {
        service.excluir(idreserva);
        return ResponseEntity.noContent().build();
    }
}

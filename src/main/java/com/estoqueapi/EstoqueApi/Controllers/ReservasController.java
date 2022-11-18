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

    /*@GetMapping("/{idreserva}")
    public ResponseEntity<Reservas> consultarById(@PathVariable("idreserva") Long idreserva){
        return  ResponseEntity.ok().body(service.consultarById(idreserva));
    }*/

    @GetMapping("/{iditem}")
    public ResponseEntity<List<Reservas>> consultarReservasByIdItem(@PathVariable("iditem") Long iditem){
        List<Reservas> lista = service.consultarByIdItem(iditem);
        return ResponseEntity.status(HttpStatus.OK).body(lista);
    }

    //Filtrar pela data as previsões que venceram e estão a vencer. TESTAR ENDPOINT
    @GetMapping("/vencimento")
    public ResponseEntity<List<Reservas>> consultaVencidos(@RequestParam(value = "finalizada") Boolean finalizada){
        List<Reservas> listar = service.findByDataPrevista(true, finalizada);
        return ResponseEntity.status(HttpStatus.OK).body(listar);
    }

    //Filtrar pela data as previsões que venceram e estão a vencer. TESTAR ENDPOINT
    @GetMapping("/pendentes")
    public ResponseEntity<List<Reservas>> findByDataPrevistaVencidos(@RequestParam(value = "finalizada") Boolean finalizada){
        List<Reservas> listar = service.findByDataPrevista(false, finalizada);
        return ResponseEntity.status(HttpStatus.OK).body(listar);
    }

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

package com.estoqueapi.EstoqueApi.Controllers;

import com.estoqueapi.EstoqueApi.Entidades.Reserva;
import com.estoqueapi.EstoqueApi.Servicos.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/reservas") //http://localhost:8081/reservas
public class ReservaController {

    @Autowired
    ReservaService service;
    @GetMapping
    public ResponseEntity<List<Reserva>> consultarReservas(){
        List<Reserva> lista = service.consultar();
        return ResponseEntity.status(HttpStatus.OK).body(lista);
    }

    /*@GetMapping("/{idreserva}")
    public ResponseEntity<Reservas> consultarById(@PathVariable("idreserva") Long idreserva){
        return  ResponseEntity.ok().body(service.consultarById(idreserva));
    }*/

    @GetMapping("/{iditem}")
    public ResponseEntity<List<Reserva>> consultarReservasByIdItem(@PathVariable("iditem") Long iditem){
        List<Reserva> lista = service.consultarByIdItem(iditem);
        return ResponseEntity.status(HttpStatus.OK).body(lista);
    }

    //Filtrar pela data as previsões que venceram e estão a vencer. TESTAR ENDPOINT
    @GetMapping("/vencimento")
    public ResponseEntity<List<Reserva>> consultaVencidos(@RequestParam(value = "finalizada") Boolean finalizada){
        List<Reserva> listar = service.findByDataPrevista(true, finalizada);
        return ResponseEntity.status(HttpStatus.OK).body(listar);
    }

    //Filtrar pela data as previsões que venceram e estão a vencer. TESTAR ENDPOINT
    @GetMapping("/pendentes")
    public ResponseEntity<List<Reserva>> findByDataPrevistaVencidos(@RequestParam(value = "finalizada") Boolean finalizada){
        List<Reserva> listar = service.findByDataPrevista(false, finalizada);
        return ResponseEntity.status(HttpStatus.OK).body(listar);
    }

    @PostMapping
    public ResponseEntity<Reserva> salvar(@Valid @RequestBody Reserva reserva){
        Reserva res = service.salvar(reserva);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @PutMapping("/{idreserva}")
    public ResponseEntity<Object> alterar(
            @PathVariable("idreserva") Long idreserva,
            @Valid @RequestBody Reserva reserva){
        return ResponseEntity.status(HttpStatus.OK).body(service.alterar(idreserva, reserva));
    }

    @DeleteMapping("/{idreserva}")
    public ResponseEntity<Void> excluir(@PathVariable("idreserva") Long idreserva) {
        service.excluir(idreserva);
        return ResponseEntity.noContent().build();
    }
}

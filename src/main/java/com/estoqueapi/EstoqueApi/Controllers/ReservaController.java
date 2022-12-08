package com.estoqueapi.EstoqueApi.Controllers;

import com.estoqueapi.EstoqueApi.Dtos.PrevisaoDTO;
import com.estoqueapi.EstoqueApi.Dtos.PrevisaoNovaDTO;
import com.estoqueapi.EstoqueApi.Dtos.ReservaDTO;
import com.estoqueapi.EstoqueApi.Dtos.ReservaNovaDTO;
import com.estoqueapi.EstoqueApi.Entidades.Reserva;
import com.estoqueapi.EstoqueApi.Mapper.Mapper;
import com.estoqueapi.EstoqueApi.Servicos.ReservaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    private Mapper mapper;

    @GetMapping
    public ResponseEntity<List<Reserva>> consultarReservas(){
        List<Reserva> lista = service.consultar();
        return ResponseEntity.status(HttpStatus.OK).body(lista);
    }

    @GetMapping("/{iditem}")
    public ResponseEntity<List<Reserva>> consultarReservasByIdItem(@PathVariable("iditem") Long iditem){
        List<Reserva> lista = service.consultarByIdItem(iditem);
        return ResponseEntity.status(HttpStatus.OK).body(lista);
    }

    // Salvar reserva nova
    @PostMapping("")
    public ResponseEntity<ReservaDTO> salvar(@Valid @RequestBody ReservaNovaDTO reservaNovaDTO){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.toReservaDTO(service.salvar(mapper.toReserva(reservaNovaDTO))));
    }

    @PutMapping("/{idreserva}")
    public ResponseEntity<Object> alterar(
            @PathVariable("idreserva") Long idreserva,
            @Valid @RequestBody ReservaNovaDTO reservaNovaDTO){
        return ResponseEntity.status(HttpStatus.OK)
                .body(mapper.toReservaDTO(service.alterar(idreserva, mapper.toReserva(reservaNovaDTO))));
    }

    @DeleteMapping("/{idreserva}")
    public ResponseEntity<Void> excluir(@PathVariable("idreserva") Long idreserva) {
        service.excluir(idreserva);
        return ResponseEntity.noContent().build();
    }
}

package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.Item;
import com.estoqueapi.EstoqueApi.Entidades.Reserva;
import com.estoqueapi.EstoqueApi.Entidades.Usuario;
import com.estoqueapi.EstoqueApi.Repositorios.ReservaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ReservaServiceTest {
    private List<Reserva> listReserva;
    private Reserva reserva1;
    private Reserva reserva2;
    private Usuario usuario;
    private Item item1;
    private Item item2;

    @Mock
    private ReservaRepository reservaRepository;
    @Mock
    private UsuarioService usuarioService;
    @Mock
    private ItemService itemService;
    @InjectMocks
    private ReservaService reservaService;

    @BeforeEach
    public void setup(){

        usuario = new Usuario("Jose","1234", "jose@jose");

        item1 = new Item(1l,"ITEM 1","GRUPO 1","FAM","UND",25);
        item2 = new Item(2l,"ITEM 2","GRUPO 1","FAM","UND",25);

        reserva1 = new Reserva();
        reserva1.setIdReserva(1l);
        reserva1.setOrdem("ORDEM1");
        reserva1.setQuantidadeReserva(25);
        reserva1.setUsuario(usuario);
        reserva1.setItem(item1);
        reserva1.setFinalizada(false);
        reserva1.setDataPrevista(LocalDate.now().plus(5, ChronoUnit.DAYS));

        reserva2 = new Reserva();
        reserva2.setIdReserva(2l);
        reserva2.setOrdem("ORDEM2");
        reserva2.setQuantidadeReserva(25);
        reserva2.setUsuario(usuario);
        reserva2.setItem(item2);
        reserva2.setFinalizada(false);
        reserva2.setDataPrevista(LocalDate.now().now().plus(4, ChronoUnit.DAYS));

        listReserva = new ArrayList<>();
        listReserva.add(reserva1);
        listReserva.add(reserva2);
    }

    @Test
    @DisplayName("Retorna reserva apos salvar")
    public void retornaReservaAposSalvar(){
        Reserva mockReserva = new Reserva();
        mockReserva.setIdReserva(1l);
        mockReserva.setOrdem("1000");
        mockReserva.setQuantidadeReserva(25);
        mockReserva.setUsuario(usuario);
        mockReserva.setItem(item2);
        mockReserva.setFinalizada(false);
        mockReserva.setDataPrevista(LocalDate.now().plus(5, ChronoUnit.DAYS));

        Mockito.when(reservaRepository.save(mockReserva)).thenReturn(reserva1);
        Mockito.when(usuarioService.buscarUsuarioById(reserva1.getUsuario().getIdUsuario())).thenReturn(usuario);
        Mockito.when(itemService.consultarItemById(reserva1.getItem().getIdItem())).thenReturn(item1);

        Assertions.assertEquals(mockReserva.getIdReserva(),reservaService.salvar(mockReserva).getIdReserva());
    }

    @Test
    @DisplayName("Retorna reserva com quantidade incrementada se ordem ja existir ao salvar")
    public void retornaReservaQuantidadeIncrementadaOrdemExistirAoSalvar(){
        Reserva mockReserva = new Reserva();
        mockReserva.setOrdem("ORDEM1");
        mockReserva.setQuantidadeReserva(25);
        mockReserva.setUsuario(usuario);
        mockReserva.setItem(item1);
        mockReserva.setFinalizada(false);
        mockReserva.setDataPrevista(LocalDate.now().plus(5, ChronoUnit.DAYS));

        Optional<Reserva> optRes = Optional.of(reserva1);

        Mockito.when(reservaRepository.save(reserva1)).thenReturn(reserva1);
        Mockito.when(usuarioService.buscarUsuarioById(reserva1.getUsuario().getIdUsuario())).thenReturn(usuario);
        Mockito.when(itemService.consultarItemById(reserva1.getItem().getIdItem())).thenReturn(item1);
        Mockito.when(reservaRepository.findByOrdem(mockReserva.getOrdem())).thenReturn(listReserva);
        Mockito.when(reservaRepository.findById(reserva1.getIdReserva())).thenReturn(optRes);

        float valorEsperado = reserva1.getQuantidadeReserva() + mockReserva.getQuantidadeReserva();
        Assertions.assertEquals(valorEsperado,reservaService.salvar(mockReserva).getQuantidadeReserva());
    }
}

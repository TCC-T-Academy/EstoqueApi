package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.Item;
import com.estoqueapi.EstoqueApi.Entidades.Previsao;
import com.estoqueapi.EstoqueApi.Entidades.Reserva;
import com.estoqueapi.EstoqueApi.Entidades.Usuario;
import com.estoqueapi.EstoqueApi.Exceptions.AcaoNaoPermitidaException;
import com.estoqueapi.EstoqueApi.Repositorios.ReservaRepository;
import com.estoqueapi.EstoqueApi.Utils.ConversorData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservaService {
    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private ItemService itemService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private EstoqueService estoqueService;
    @Autowired
    private LogFuturoService logFuturoService;

    public List<Reserva> consultar(){
        return reservaRepository.findAllOrderByDesc();
    }

    public Reserva consultarById(Long id){
        Optional<Reserva> obj = reservaRepository.findById(id);
        Reserva res = null;// obj.orElseThrow(()-> new EntityNotFoundException("Reserva não encontrada"));
        try{
            res = obj.get();
        }
        catch (NoSuchElementException e){
            throw new EntityNotFoundException("Reserva não encontrada");
        }
        return res;
    }

    public List<Reserva> consultarByIdItem(Long idItem){
        //Lança exceção se o item nao existir.
        Item i = itemService.consultarItemById(idItem);
        return reservaRepository.ConsultarByIdItem(idItem);
    }

    /** Metodo para cadastrar novas reserva. **/
    @Transactional
    public Reserva salvar(Reserva reserva){
        //Validacoes gerais
        reserva = this.validarReserva(reserva);

        //Validacão especifica
        if(reserva.getDataPrevista().isBefore(LocalDate.now())) {
            throw new AcaoNaoPermitidaException("Informe uma data maior que a atual");
        }

        //  Verifica se existe a uma reserva de status finalizado = false para a ordem
        //    alterando a quantidade em caso positivo.
        long idItem = reserva.getItem().getIdItem();
        List<Reserva> list = reservaRepository.findByOrdem(reserva.getOrdem())
                .stream()
                .filter(reserva1 -> reserva1.getItem().getIdItem() == idItem)
                .collect(Collectors.toList());
        if(list.size() > 0){
            Reserva r = list.get(0);
            if(!r.isFinalizada() && r.getItem().getIdItem() == reserva.getItem().getIdItem()){
                r.setQuantidadeReserva(r.getQuantidadeReserva() + reserva.getQuantidadeReserva());
                return this.alterar(r.getIdReserva(),r);
            }
        }
        Reserva reserva1 = reservaRepository.save(reserva);
        //Atualiza o estoque futuro do item
        estoqueService.atualizarEstoqueFuturo(idItem, logFuturoService.buscarLogIdItem(idItem));
        return reserva1;
    }


    /** Metodo para alteracao de uma reserva a partir de um id. **/
    public Reserva alterar(Long idreserva, Reserva reserva){
        Reserva res = this.consultarById(idreserva);

        // Não deixa alterar se reserva finalizada
        if (res.isFinalizada()) {
            throw new AcaoNaoPermitidaException("Reserva já finalizada");
        }
        // Não deixa alterar se nova data é anterior a atual da reserva e anterior a now()
        if(reserva.getDataPrevista().isBefore(res.getDataPrevista())
                && reserva.getDataPrevista().isBefore(LocalDate.now())){
            throw new AcaoNaoPermitidaException("Informe uma data maior que a atual");
        }
        //Lancará excecões caso haja problemas
        reserva = this.validarReserva(reserva);
        res.setFinalizada(reserva.isFinalizada());
        res.setQuantidadeReserva(reserva.getQuantidadeReserva());
        res.setDataPrevista(reserva.getDataPrevista());
        res.setOrdem(reserva.getOrdem());
        res.setUsuario(reserva.getUsuario());
        res.setItem(reserva.getItem());
        Reserva reserva1 = reservaRepository.save(res);

        //Atualiza o estoque futuro do item
        estoqueService.atualizarEstoqueFuturo(res.getItem().getIdItem(), logFuturoService.buscarLogIdItem(res.getItem().getIdItem()));
        return reserva1;
    }

    @Transactional
    public void excluir(Long idreserva){
        Reserva reservas = this.consultarById(idreserva);
        reservaRepository.delete(reservas);
    }

    public List<Reserva> consultarPendentesByIdItem(Long idItem){
        return reservaRepository.consultarPendentesByIdItem(idItem);
    }

    /** Metodo para validacao de uma reserva. **/
    public Reserva validarReserva(Reserva reserva) {
        if (reserva.getQuantidadeReserva() <= 0) {
            throw new AcaoNaoPermitidaException("Quantidade inválida");
        }
        if (reserva.getOrdem() == null || reserva.getOrdem().isEmpty()) {
            throw new AcaoNaoPermitidaException("Ordem nao informada");
        }
        if (reserva.getItem() == null) {
            throw new AcaoNaoPermitidaException("Item nao informado");
        }
        if (reserva.getDataPrevista() == null) {
            throw new AcaoNaoPermitidaException("Informe uma data válida");
        }
        //Vai lancar excecao se o item for invalido
        Item i = itemService.consultarItemById(reserva.getItem().getIdItem());
        Usuario u = usuarioService.buscarUsuarioById(reserva.getUsuario().getIdUsuario());
        reserva.setItem(i);
        reserva.setUsuario(u);
        return reserva;
    }

    /** Metodo para criar uma copia de objeto em memoria para evitar conflitos. **/
    public Reserva clonar(Reserva reserva){
        Reserva nRes = new Reserva();
        nRes.setItem(reserva.getItem());
        nRes.setUsuario(reserva.getUsuario());
        nRes.setIdReserva(reserva.getIdReserva());
        nRes.setFinalizada(reserva.isFinalizada());
        nRes.setOrdem(reserva.getOrdem());
        nRes.setQuantidadeReserva(reserva.getQuantidadeReserva());
        nRes.setDataPrevista(reserva.getDataPrevista());
        return nRes;
    }
}

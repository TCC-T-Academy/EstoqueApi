package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.Reserva;
import com.estoqueapi.EstoqueApi.Repositorios.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ReservaService {
    @Autowired
    private ReservaRepository reservaRepository;
    public List<Reserva> consultar(){
        return reservaRepository.findAll();
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
        return reservaRepository.ConsultarByIdItem(idItem);
    }

        /*public List<Reservas> consultarByFinalizada(Boolean finalizada){
        return reservasRepository.consultarByFinalizada(finalizada);
    }*/

    public List<Reserva> findByDataPrevista(boolean vencida, boolean finalizada) {
        if (vencida == true) {
            List<Reserva> lista = (finalizada) ? reservaRepository.findByDataPrevistaVencidos(finalizada) : reservaRepository.findByDataPrevistaVencidos(finalizada);
            return lista;
        } else{
            List<Reserva> lista = (finalizada) ? reservaRepository.findByDataPrevistaAVencer(finalizada) : reservaRepository.findByDataPrevistaAVencer(finalizada);
            return lista;
        }
    }

    @Transactional
    public Reserva salvar(Reserva reserva){
        return reservaRepository.save(reserva);
    }

    public Reserva alterar(Long idreserva, Reserva reservas){
        Reserva res = this.consultarById(idreserva);

        res.setFinalizada(reservas.isFinalizada());
        res.setQuantidadeReserva(reservas.getQuantidadeReserva());
        res.setDataPrevista(reservas.getDataPrevista());
        res.setOrdem(reservas.getOrdem());
        res.setUsuario(reservas.getUsuario());
        res.setItem(reservas.getItem());

        return this.salvar(res);
    }

    @Transactional
    public void excluir(Long idreserva){
        Reserva reservas = this.consultarById(idreserva);
        reservaRepository.delete(reservas);
    }

    public List<Reserva> consultarPendentesByIdItem(Long idItem){
        return reservaRepository.consultarPendentesByIdItem(idItem);
    }

}

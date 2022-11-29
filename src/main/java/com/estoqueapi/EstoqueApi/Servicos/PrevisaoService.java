package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.Item;
import com.estoqueapi.EstoqueApi.Entidades.Previsao;
import com.estoqueapi.EstoqueApi.Entidades.Reserva;
import com.estoqueapi.EstoqueApi.Entidades.Usuario;
import com.estoqueapi.EstoqueApi.Exceptions.AcaoNaoPermitidaException;
import com.estoqueapi.EstoqueApi.Repositorios.ItemRepository;
import com.estoqueapi.EstoqueApi.Repositorios.PrevisaoRepository;
import com.estoqueapi.EstoqueApi.Utils.ConversorData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class PrevisaoService {

    @Autowired
    private PrevisaoRepository previsaoRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UsuarioService usuarioService;

    // Listar todas as previsões cadastradas
   public Iterable<Previsao> listarPrevisoes() {
        return previsaoRepository.findAll();
    }

    // Filtrar previsões por id do item
    public List<Previsao> consultarByIdItem(Long idItem) {
        return previsaoRepository.ConsultarByIdItem(idItem);
    }


    /** Metodo para cadastrar novas previsoes.
     * @param pr Previsao: Objeto a ser cadastrado.
     * @return Previsao: Objeto retornado pelo repositorio após validacoes.
     * */
    @Transactional
    public Previsao cadastrarPrevisoes(Previsao pr){
        //Validacoes gerais
        pr = this.validarPrevisao(pr);

        //Validacão especifica
        if(pr.getDataPrevista().isBefore(LocalDate.now())) {
            throw new AcaoNaoPermitidaException("Informe uma data maior que a atual");
        }


        //  Verifica se existe a uma previsao de status finalizado = false para a ordem
        //    alterando a quantidade em caso positivo.
        long idItem = pr.getItem().getIdItem();
        List<Previsao> list = previsaoRepository.findByOrdem(pr.getOrdem())
                                                    .stream()
                                                    .filter(previsao -> previsao.getItem().getIdItem() == idItem)
                                                    .collect(Collectors.toList());
        if(list.size() > 0){
            Previsao p = list.get(0);
            if(!p.getFinalizada() && p.getItem().getIdItem() == pr.getItem().getIdItem()){
                p.setQuantidadePrevista(p.getQuantidadePrevista() + pr.getQuantidadePrevista());
                return this.alterarPrevisao(p.getIdPrevisao(),p);
            }
        }

        return previsaoRepository.save(pr);
    }


    //Filtrar previsão por idPrevisao
    public Previsao filtrarId(long idPrevisao){
        Optional<Previsao> obj = previsaoRepository.findById(idPrevisao);
        Previsao prev = null;
        try{
            prev = obj.get();
        } catch (NoSuchElementException exception) {
            throw new EntityNotFoundException("Previsão não localizada");
        }
        return prev;
    }

    //Filtrar por ordem de compra/produção realizadas ou não
    public List<Previsao> findByFinalizada(boolean finalizada){
        return previsaoRepository.findByFinalizada(finalizada);
    }

    //Filtrar pela data as previsões que venceram e estão a vencer.
    public List<Previsao> findByDataPrevistaVencidos(boolean finalizada) {
        if (finalizada == true) {
            return previsaoRepository.findByDataPrevistaVencidos();
        } else
            return previsaoRepository.findByDataPrevistaAVencer();
    }

        // Lista previsões com opção de escolha tanto com data anterior quanto a partir de hoje e finalizadas ou não
    public List<Previsao> findByDataPrevistaFinalizada(boolean vencimento, boolean finalizada) {
        if (vencimento == true) {
            return previsaoRepository.findByDataPrevistaMenorFinalizada(finalizada);
        } else {
            return previsaoRepository.findByDataPrevistaMaiorIgualFinalizada(finalizada);
        }
    }

    /**
     * Metodo para alteracao de uma previsao a partir de um id.
     * @param idPrevisao Long: Id da previsao a ser alterada.
     * @param previsao Previsao: objeto previsao com as informacoes para atualizacao.
     * @return Previsao: objeto retornado pelo repositorio após validacoes
     * */
    public Previsao alterarPrevisao(Long idPrevisao, Previsao previsao){

        Previsao prev = this.filtrarId(idPrevisao);

        // Não deixa alterar se previsao finalizada
        if (prev.getFinalizada()) {
            throw new AcaoNaoPermitidaException("Previsão já finalizada");
        }

        // Não deixa alterar se nova data é anterior a atual da previsao e anterior a now()
        if(previsao.getDataPrevista().isBefore(prev.getDataPrevista())
                && previsao.getDataPrevista().isBefore(LocalDate.now())){
            throw new AcaoNaoPermitidaException("Informe uma data maior que a atual");
        }

        //Lancará excecões caso haja problemas
        previsao = this.validarPrevisao(previsao);

        prev.setItem(previsao.getItem());
        prev.setUsuario(previsao.getUsuario());
        prev.setFinalizada(previsao.getFinalizada());
        prev.setOrdem(previsao.getOrdem());
        prev.setQuantidadePrevista(previsao.getQuantidadePrevista());
        prev.setDataPrevista(previsao.getDataPrevista());

        return previsaoRepository.save(prev);
    }


    //Excluir previsão (só pode ser excluído quando finalizada = false)
    @Transactional
    public void excluirPrevisao(long idPrevisao){
        Previsao previsao = this.filtrarId(idPrevisao);
        if(previsao.getFinalizada() == false){
            previsaoRepository.delete(previsao);
        }else{
            throw new AcaoNaoPermitidaException("Previsao já finalizada");
        }
    }


    public List<Previsao> consultarPendentesByIdItem(Long idItem) {
        return previsaoRepository.ConsultarPendentesByIdItem(idItem);
    }


    /**Metodo para validacao de uma previsao.
     * @param previsao Previsao - Objeto para ser validado;
     * @return Previsao - Objeto validado
     * */
    public Previsao validarPrevisao(Previsao previsao) {

        if (previsao.getQuantidadePrevista() <= 0) {
            throw new AcaoNaoPermitidaException("Quantidade inválida");
        }
        if (previsao.getOrdem() == null || previsao.getOrdem().isEmpty()) {
            throw new AcaoNaoPermitidaException("Ordem nao informada");
        }
        if (previsao.getItem() == null) {
            throw new AcaoNaoPermitidaException("Item nao informado");
        }

        if (previsao.getDataPrevista() == null) {
            throw new AcaoNaoPermitidaException("Informe uma data válida");
        }

        //Vai lancar excecao se o item for invalido
        Item i = itemService.consultarItemById(previsao.getItem().getIdItem());
        Usuario u = usuarioService.buscarUsuarioById(previsao.getUsuario().getIdUsuario());

        return previsao;
    }


    /**
     * Metodo para criar uma copia de objeto em memoria para evitar conflitos.
     * @param previsao Previsao: objeto base para clonagem
     * @return Previsao: objeto com endereco de memoria diferente.
     * */
    public Previsao clonar(Previsao previsao){
        Previsao nPrev = new Previsao();

        nPrev.setItem(previsao.getItem());
        nPrev.setUsuario(previsao.getUsuario());
        nPrev.setIdPrevisao(previsao.getIdPrevisao());
        nPrev.setFinalizada(previsao.getFinalizada());
        nPrev.setOrdem(previsao.getOrdem());
        nPrev.setQuantidadePrevista(previsao.getQuantidadePrevista());
        nPrev.setDataPrevista(previsao.getDataPrevista());

        return nPrev;
    }

    public List<Previsao> consultarVencimentoHoje() {
        String date = (LocalDate.from(ConversorData.toLocalDateTime(Instant.now()))).toString();
        return previsaoRepository.consultarVencimentoHoje(date);
    }

}


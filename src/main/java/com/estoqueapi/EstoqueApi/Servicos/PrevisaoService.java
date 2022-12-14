package com.estoqueapi.EstoqueApi.Servicos;

import com.estoqueapi.EstoqueApi.Entidades.Item;
import com.estoqueapi.EstoqueApi.Entidades.Previsao;
import com.estoqueapi.EstoqueApi.Entidades.Reserva;
import com.estoqueapi.EstoqueApi.Entidades.Usuario;
import com.estoqueapi.EstoqueApi.Exceptions.AcaoNaoPermitidaException;
import com.estoqueapi.EstoqueApi.Repositorios.EstoqueRepository;
import com.estoqueapi.EstoqueApi.Repositorios.ItemRepository;
import com.estoqueapi.EstoqueApi.Repositorios.PrevisaoRepository;
import com.estoqueapi.EstoqueApi.Utils.ConversorData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private EstoqueService estoqueService;
    @Autowired
    private LogFuturoService logFuturoService;

    @Autowired
    private UsuarioService usuarioService;

    // Listar todas as previsões cadastradas
    public Iterable<Previsao> listarPrevisoes() {
        return previsaoRepository.findAll();
    }

    // Filtrar previsões por id do item
    public List<Previsao> consultarByIdItem(Long idItem) {
        //Lança exceção se o item nao existir.
        Item i = itemService.consultarItemById(idItem);

        return previsaoRepository.ConsultarByIdItem(idItem);
    }


    /** Metodo para cadastrar novas previsoes. **/
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
        //Atualiza estoque futuro
        Previsao previsao1 = previsaoRepository.save(pr);
        estoqueService.atualizarEstoqueFuturo(idItem ,logFuturoService.buscarLogIdItem(idItem));
        return previsao1;
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

    /** Metodo para alteracao de uma previsao a partir de um id. * */
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
        //Atualiza estoque futuro
        Previsao previsao1 = previsaoRepository.save(prev);
        estoqueService.atualizarEstoqueFuturo(prev.getItem().getIdItem(), logFuturoService.buscarLogIdItem(prev.getItem().getIdItem()));
        return previsao1;
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


    /** Metodo para validacao de uma previsao. * */
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

        previsao.setItem(i);
        previsao.setUsuario(u);
        return previsao;
    }


    /** Metodo para criar uma copia de objeto em memoria para evitar conflitos. **/
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
}


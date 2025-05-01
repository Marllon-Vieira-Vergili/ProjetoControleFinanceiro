package com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.Implements;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.ContaUsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.ContaUsuarioResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DadosInvalidosException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.JaExisteException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.ContaNaoEncontrada;
import com.marllon.vieira.vergili.catalogo_financeiro.mapper.ContaUsuarioMapper;
import com.marllon.vieira.vergili.catalogo_financeiro.models.CategoriaFinanceira;
import com.marllon.vieira.vergili.catalogo_financeiro.models.ContaUsuario;
import com.marllon.vieira.vergili.catalogo_financeiro.models.HistoricoTransacao;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Pagamentos;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposContas;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.ContaUsuarioRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.ContaUsuarioAssociation;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.ContaUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposContas.CONTA_CORRENTE;

@Service
public class ContaUsuarioImpl implements ContaUsuarioService {


    @Autowired
    private ContaUsuarioAssociation contaUsuarioAssociation;

    @Autowired
    private ContaUsuarioMapper contaUsuarioMapper;

    @Autowired
    private ContaUsuarioService contaUsuarioService;

    @Autowired
    private ContaUsuarioRepository contaRepository;


    @Override
    public ContaUsuarioResponse criarConta(ContaUsuarioRequest request) {


        //Criar uma conta
        ContaUsuario novaConta = new ContaUsuario();
        novaConta.setNome(request.nome());
        novaConta.setSaldo(request.saldo());

        if(novaConta.getNome().isEmpty()){
            throw new DadosInvalidosException("Por favor, digite um nome válido!");
        }
        if(seSaldoEstiverNegativo(request.saldo())){
            if(novaConta.getTipoConta().equals(CONTA_CORRENTE)){
                novaConta.setSaldo(request.saldo());
            }else{
                throw new IllegalArgumentException("Somente conta corrente aceita valores negativos");
            }
        } else if (seSaldoEstiverPositivo(request.saldo())) {
            novaConta.setSaldo(request.saldo());
        }

        //verificar se o tipo de conta existe
        if(tipoContaExiste(request.tipoConta())){
            novaConta.setTipoConta(request.tipoConta());
        }

        if(jaExisteUmaContaIgual(novaConta)){
            throw new JaExisteException("Já existe uma conta igual a essa criada!");
        }

        //Salvar a nova conta
        contaRepository.save(novaConta);


        return contaUsuarioMapper.retornarDadosContaUsuario(novaConta);
    }

    @Override
    public Optional<ContaUsuarioResponse> encontrarContaPorId(Long id) {

        ContaUsuario contaEncontrada = getContaById(id);

        return Optional.ofNullable(contaUsuarioMapper.retornarDadosContaUsuario(contaEncontrada));
    }

    @Override
    public List<ContaUsuarioResponse> encontrarContaPorNome(String nome) {
        ContaUsuario contaEncontrada = contaRepository.encontrarContaPeloNome(nome);

        return List.of(contaUsuarioMapper.retornarDadosContaUsuario(contaEncontrada));
    }

    @Override
    public Page<ContaUsuarioResponse> encontrarTodas(Pageable pageable) {

        Page<ContaUsuario>
                todasContasEncontradas = contaRepository.findAll(pageable);

        if(todasContasEncontradas.isEmpty()){
            throw new ContaNaoEncontrada("Não há nenhuma conta salva na base de dados");
        }
        List<ContaUsuarioResponse> contaUsuariosResponse=
                todasContasEncontradas.stream()
                        .map(contasUsuario -> new
                                ContaUsuarioResponse(contasUsuario.getId(),
                                contasUsuario.getNome(), contasUsuario.getSaldo(),contasUsuario.getTipoConta())).collect(Collectors.toList());

        return new PageImpl<>(contaUsuariosResponse);

    }

    @Override
    public ContaUsuarioResponse atualizarUmaConta(Long id, ContaUsuarioRequest request) {

        //Encontrar a conta pela id


        ContaUsuario contaEncontrada = getContaById(id);

        //Obter os novos dados
        contaEncontrada.setNome(request.nome());
        contaEncontrada.setSaldo(request.saldo());
        contaEncontrada.setTipoConta(request.tipoConta());


        if(contaEncontrada.getNome().isEmpty()){
            throw new DadosInvalidosException("Por favor, digite um nome válido!");
        }
        if(seSaldoEstiverNegativo(request.saldo())){
            if(contaEncontrada.getTipoConta().equals(CONTA_CORRENTE)){
                contaEncontrada.setSaldo(request.saldo());
            }else{
                throw new IllegalArgumentException("Somente conta corrente aceita valores negativos");
            }
        } else if (seSaldoEstiverPositivo(request.saldo())) {
            contaEncontrada.setSaldo(request.saldo());
        }

        //verificar se o tipo de conta existe, e associar
        if(tipoContaExiste(request.tipoConta())){
            contaEncontrada.setTipoConta(request.tipoConta());
        }

        if(jaExisteUmaContaIgual(contaEncontrada)){
            throw new JaExisteException("Já existe uma conta igual a essa criada!");
        }

        //Salvar a nova conta
        contaRepository.save(contaEncontrada);

        return contaUsuarioMapper.retornarDadosContaUsuario(contaEncontrada);
    }

    @Override
    public void deletarConta(Long id) {

        ContaUsuario contaSerRemovida = getContaById(id);


        try{
            contaUsuarioAssociation.desassociarContaDeTipoConta(id,contaSerRemovida.getTipoConta());
        }catch (DesassociationErrorException e){
            throw new DesassociationErrorException("Erro ao desassociar do tipo de conta ");
        }

        try{
            contaUsuarioAssociation.desassociarContaDeUsuario(id, contaSerRemovida.getUsuarioRelacionado().getId());

        } catch (DesassociationErrorException e) {
            throw new DesassociationErrorException("Erro ao desassociar de Usuario:  " + e.getMessage());
        }

        try{
            for(CategoriaFinanceira categoriaEncontrada: contaSerRemovida.getCategoriasRelacionadas()){
                contaUsuarioAssociation.desassociarContaDeCategorias(id, categoriaEncontrada.getId());
            }
        }catch (DesassociationErrorException e){
            throw new DesassociationErrorException("Erro ao desassociar de Categorias Financeiras:  " + e.getMessage());
        }

        try{
            for(Pagamentos pagamentosEncontrados: contaSerRemovida.getPagamentosRelacionados()){
                contaUsuarioAssociation.desassociarContaDePagamento(id, pagamentosEncontrados.getId());
            }
        }catch (DesassociationErrorException e){
            throw new DesassociationErrorException("Erro ao desassociar de Pagamentos encontrados:  " + e.getMessage());
        }

        try{
            for(HistoricoTransacao historicosEncontrados: contaSerRemovida.getTransacoesRelacionadas()){
                contaUsuarioAssociation.desassociarContaDeHistoricoDeTransacao(id, historicosEncontrados.getId());
            }
        }catch (DesassociationErrorException e){
            throw new DesassociationErrorException("Erro ao desassociar de Historico de transações:  " + e.getMessage());
        }

        //Remover a conta encontrada
        contaRepository.delete(contaSerRemovida);

    }

    @Override
    public String verificarTipoConta(Long contaId) {

        ContaUsuario contaUsuario = getContaById(contaId);

        if(contaUsuario.getTipoConta() != null){
            return contaUsuario.getTipoConta().name();
        }
        return null;
    }

    @Override
    public boolean seSaldoEstiverNegativo(BigDecimal saldo) {
        return saldo.compareTo(BigDecimal.ZERO) < 0;
    }

    @Override
    public boolean seSaldoEstiverPositivo(BigDecimal saldo) {
        return saldo.compareTo(BigDecimal.ZERO) > 0;
    }

    @Override
    public void adicionarSaldo(ContaUsuario conta, BigDecimal valor) {
        ContaUsuario contaEncontrada = contaRepository.encontrarContaPeloSaldo(conta.getSaldo());

       if(contaUsuarioService.jaExisteUmaContaIgual(contaEncontrada) && contaEncontrada.getId().equals(conta.getId())){
           BigDecimal novoSaldo = contaEncontrada.getSaldo().add(valor);
           contaRepository.save(contaEncontrada);
       }else{
           throw new ContaNaoEncontrada("Não foi encontrado nenhuma conta com esse valor");
       }
    }


    @Override
    public void subtrairSaldo(ContaUsuario conta, BigDecimal valor) {
        ContaUsuario contaEncontrada = contaRepository.encontrarContaPeloSaldo(conta.getSaldo());

        if(contaUsuarioService.jaExisteUmaContaIgual(contaEncontrada) && contaEncontrada.getId()
                .equals(conta.getId())){
            BigDecimal novoSaldo = contaEncontrada.getSaldo().subtract(valor);
            contaRepository.save(contaEncontrada);
        }else{
            throw new ContaNaoEncontrada("Não foi encontrado nenhuma conta com esse valor");
        }
    }

    @Override
    public BigDecimal consultarSaldo(Long contaId) {
        ContaUsuario contaUsuario = contaRepository.findById(contaId).orElseThrow(()
                -> new ContaNaoEncontrada("Nenhuma conta foi encontrada com essa id"));

       if(contaUsuario != null){
           return contaUsuario.getSaldo();
       }
       return null;
    }

    @Override
    public boolean contaExistePelaID(Long id) {
        Optional<ContaUsuario> contaEncontrada = contaRepository.findById(id);
        return contaEncontrada.isPresent();
    }

    @Override
    public ContaUsuario getContaById(Long id) {
        return contaRepository.findById(id).orElseThrow(()
                ->new ContaNaoEncontrada("Nenhuma conta de usuário foi encontrada com essa id informada!"));
    }

    @Override
    public boolean tipoContaExiste(TiposContas tipoConta) {
        TiposContas[] tiposContasEncontrados = TiposContas.values();
        return Arrays.asList(tiposContasEncontrados).contains(tipoConta);
    }

    @Override
    public boolean jaExisteUmaContaIgual(ContaUsuario contaUsuario) {
        List<ContaUsuario> todasContasCriadas = contaRepository.findAll();
        return todasContasCriadas.stream().anyMatch(contaUsuarioEncontrada  ->
                contaUsuarioEncontrada.equals(contaUsuario));
    }
}

package com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.Implements;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.ContaUsuario.ContaUsuarioCreateRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.ContaUsuario.ContaUsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.ContaUsuarioResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.AssociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DadosInvalidosException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.ContaNaoEncontrada;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.TiposContasNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.UsuarioNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.mapper.ContaUsuarioMapper;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposContas;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.ContaUsuarioRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.UsuarioRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.ContaUsuarioAssociation;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.ContaUsuarioService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ContaUsuarioImpl implements ContaUsuarioService {

    @Autowired
    private ContaUsuarioRepository contaUsuarioRepository;

    @Autowired
    private ContaUsuarioAssociation contaUsuarioAssociation;

    @Autowired
    private ContaUsuarioMapper contaUsuarioMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public ContaUsuarioResponse criarConta(ContaUsuarioCreateRequest request, Long idUsuario) {

        //Criando a conta do usuário
        ContaUsuario contaNova = new ContaUsuario();
        contaNova.setNome(request.nome());
        contaNova.setSaldo(BigDecimal.ZERO); //Saldo padrão para a conta é zero
        contaNova.setTipoConta(request.tipoConta());

        //Salvando primeira vez pra gerar ID
        contaUsuarioRepository.save(contaNova);

        Usuario usuarioLocalizado = usuarioRepository.findById(idUsuario).orElseThrow(()->
                new UsuarioNaoEncontrado(super.toString()));

        if (contaNova.getUsuarioRelacionado() == null){
            try{
                contaUsuarioAssociation.associarContaComUsuario(contaNova.getId(), usuarioLocalizado.getId());
            } catch (RuntimeException e) {
                throw new AssociationErrorException(e.getMessage());
            }
        }

        //Salvando denovo
        contaUsuarioRepository.save(contaNova);
        return contaUsuarioMapper.retornarDadosContaUsuario(contaNova);
    }

    @Override
    public Optional<ContaUsuarioResponse> encontrarContaPorId(Long id) {

        Optional<ContaUsuario> contaEncontrada = Optional.of(contaUsuarioRepository.findById(id)
                .orElseThrow(()->new ContaNaoEncontrada("Esta conta com a id: " + id + "não foi localizada")));

        return Optional.ofNullable(contaUsuarioMapper.retornarDadosContaUsuario(contaEncontrada.get()));
    }

    @Override
    public List<ContaUsuarioResponse> encontrarContaPorNome(String nome) {

        List<ContaUsuario> contasEncontradas = contaUsuarioRepository.encontrarContaPeloNome(nome);

        if (contasEncontradas.isEmpty()){
            throw new ContaNaoEncontrada(super.toString());
        }
        return contasEncontradas.stream().map(contaUsuarioMapper::retornarDadosContaUsuario).toList();
    }

    @Override
    public Page<ContaUsuarioResponse> encontrarTodas(Pageable pageable) {

        List<ContaUsuario> todasContasLocalizadas = contaUsuarioRepository.findAll();

        if(todasContasLocalizadas.isEmpty()){
            throw new ContaNaoEncontrada("A lista de contas está vazia");
        }
        Page<ContaUsuario> paginaContas = new PageImpl<>(todasContasLocalizadas);
        return paginaContas.map(contaUsuarioMapper::retornarDadosContaUsuario);
    }

    @Override
    @Transactional
    public ContaUsuarioResponse alterarNomeDeUmaConta(Long id, String nomeNovo) {

        ContaUsuario contaUsuarioLocalizada =
                contaUsuarioRepository.findById(id).orElseThrow(()
                        -> new ContaNaoEncontrada("Esta conta com a id: " + id + "não foi localizada"));

            contaUsuarioLocalizada.setNome(nomeNovo);

            contaUsuarioRepository.save(contaUsuarioLocalizada);

        return contaUsuarioMapper.retornarDadosContaUsuario(contaUsuarioLocalizada);
    }

    @Override
    @Transactional
    public void deletarConta(Long id) {

        //Encontrar o usuário pela id
        ContaUsuario contaUsuarioLocalizada = contaUsuarioRepository.findById(id).orElseThrow(()
                ->new ContaNaoEncontrada("Esta conta com a id: " + id + "não foi localizada"));

        if(contaUsuarioLocalizada.getUsuarioRelacionado() != null){
            Usuario usuarioAssociado = contaUsuarioLocalizada.getUsuarioRelacionado();
            try{
                contaUsuarioAssociation.desassociarContaDeUsuario(contaUsuarioLocalizada.getId(), usuarioAssociado.getId());
            } catch (RuntimeException e) {
                throw new DesassociationErrorException("Erro ao desassociar conta de usuário do usuário. " +
                        e.getMessage());
            }
        }

        if (!contaUsuarioLocalizada.getTransacoesRelacionadas().isEmpty()){
            List<HistoricoTransacao> transacoesRelacionadas = contaUsuarioLocalizada
                    .getTransacoesRelacionadas();
            try{
                for (HistoricoTransacao transacoesPercorridas: transacoesRelacionadas){
                    contaUsuarioAssociation.desassociarContaDeHistoricoDeTransacao(contaUsuarioLocalizada.getId(),
                            transacoesPercorridas.getId());
                }
            } catch (RuntimeException e) {
                throw new DesassociationErrorException("Erro ao desassociar conta de usuário dos históricos de transação. " +
                        e.getMessage());
            }
            contaUsuarioLocalizada.getTransacoesRelacionadas().clear();
        }

        if (!contaUsuarioLocalizada.getPagamentosRelacionados().isEmpty()){
            List<Pagamentos> pagamentosRelacionados = contaUsuarioLocalizada.getPagamentosRelacionados();
            try{
                for(Pagamentos pagamentosPercorridos: pagamentosRelacionados){
                    contaUsuarioAssociation.desassociarContaDePagamento(contaUsuarioLocalizada.getId(), pagamentosPercorridos.getId());
                }
            } catch (RuntimeException e) {
                throw new DesassociationErrorException("Erro ao desassociar conta de usuário dos pagamentos. " +
                        e.getMessage());
            }
            contaUsuarioLocalizada.getPagamentosRelacionados().clear();
        }
        if (!contaUsuarioLocalizada.getCategoriasRelacionadas().isEmpty()){
            List<CategoriaFinanceira>categoriasRelacionadas = contaUsuarioLocalizada.getCategoriasRelacionadas();

            try{
                for(CategoriaFinanceira categoriasPercorridas: categoriasRelacionadas){
                    contaUsuarioAssociation.desassociarContaDeCategoria(contaUsuarioLocalizada.getId(), categoriasPercorridas.getId());
                }
            } catch (RuntimeException e) {
                throw new DesassociationErrorException("Erro ao desassociar conta de usuário das Categorias financeiras. " +
                        e.getMessage());
            }
            contaUsuarioLocalizada.getCategoriasRelacionadas().clear();
        }

        //Remover agora a conta de usuário
        contaUsuarioRepository.delete(contaUsuarioLocalizada);
    }

    @Override
    public TiposContas verificarTipoConta(Long contaId) {

        ContaUsuario contaLocalizada = contaUsuarioRepository.findById(contaId)
                .orElseThrow(()-> new ContaNaoEncontrada("Não foi localizada nenhuma conta com essa id: " + contaId));

        if (contaLocalizada.getTipoConta() != null){
            return contaLocalizada.getTipoConta();
        }
        throw new TiposContasNaoEncontrado("O tipo de conta não foi encontrado. Os Tipos de contas são: " +
                Arrays.toString(TiposContas.values()));
    }

    @Override
    public boolean seSaldoEstiverNegativo(BigDecimal saldo) {
        if (saldo.compareTo(BigDecimal.ZERO) <= 0){
            return true;
        }
        return false;
    }

    @Override
    public boolean seSaldoEstiverPositivo(BigDecimal saldo) {
        if (saldo.compareTo(BigDecimal.ZERO) >= 1){
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public void adicionarSaldo(Long idConta, BigDecimal valor) {
        Optional<ContaUsuario> contaEncontrada = contaUsuarioRepository.findById(idConta);

        if(contaEncontrada.isPresent()){
            ContaUsuario conta = contaEncontrada.get();
            BigDecimal novoValor = contaEncontrada.get().getSaldo().add(valor);
            contaEncontrada.get().setSaldo(novoValor);
            contaUsuarioRepository.save(conta);
        }
    }

    @Override
    @Transactional
    public void subtrairSaldo(Long idConta, BigDecimal valor) {

        Optional<ContaUsuario> contaEncontrada = contaUsuarioRepository.findById(idConta);

            if(contaEncontrada.isPresent()){
                ContaUsuario conta = contaEncontrada.get();
                if (valor.compareTo(conta.getSaldo()) > 0 && !TiposContas.CONTA_CORRENTE.equals(conta.getTipoConta())){
                    throw new DadosInvalidosException("Não é possível Realizar o pagamento, pois não possui saldo suficiente");
                }
                BigDecimal novoValor = contaEncontrada.get().getSaldo().subtract(valor);
                contaEncontrada.get().setSaldo(novoValor);
                contaUsuarioRepository.save(conta);
            }
        }


    @Override
    public BigDecimal consultarSaldo(Long contaId) {

        ContaUsuario contaLocalizada = contaUsuarioRepository.findById(contaId)
                .orElseThrow(()-> new ContaNaoEncontrada("Não foi localizada nenhuma conta com essa id: " + contaId));

        return contaLocalizada.getSaldo();
    }


    @Override
    public boolean jaExisteUmaContaIgual(String nome, TiposContas tipoConta) {

        List<ContaUsuario> contaEncontradaPeloNome =
                contaUsuarioRepository.encontrarContaPeloNome(nome);

        List<ContaUsuario> contaEncontradaPeloTipo = contaUsuarioRepository.encontrarPeloTipoDeConta(tipoConta.name());

        if(contaEncontradaPeloNome.getFirst().getTipoConta().equals(tipoConta) &&
                contaEncontradaPeloTipo.equals(contaEncontradaPeloNome)){

            return true;
        }
        return false;
    }
}
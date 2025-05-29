package com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.Implements;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.ContaUsuario.ContaUsuarioCreateRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.ContaUsuario.ContaUsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.ContaUsuarioResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.AssociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.ContaNaoEncontrada;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.UsuarioNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.mapper.ContaUsuarioMapper;
import com.marllon.vieira.vergili.catalogo_financeiro.models.ContaUsuario;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Usuario;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposContas;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.ContaUsuarioRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.UsuarioRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.ContaUsuarioAssociation;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.ContaUsuarioService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
                .orElseThrow(()->new ContaNaoEncontrada(super.toString())));

        return Optional.ofNullable(contaUsuarioMapper.retornarDadosContaUsuario(contaEncontrada.get()));
    }

    @Override
    public List<ContaUsuarioResponse> encontrarContaPorNome(String nome) {
        return List.of();
    }

    @Override
    public Page<ContaUsuarioResponse> encontrarTodas(Pageable pageable) {
        return null;
    }

    @Override
    @Transactional
    public ContaUsuarioResponse atualizarUmaConta(Long id, ContaUsuarioRequest request) {
        return null;
    }

    @Override
    public void deletarConta(Long id) {

    }

    @Override
    public TiposContas verificarTipoConta(Long contaId) {

        return null;
    }

    @Override
    public boolean seSaldoEstiverNegativo(BigDecimal saldo) {
        return false;
    }

    @Override
    public boolean seSaldoEstiverPositivo(BigDecimal saldo) {
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
            BigDecimal novoValor = contaEncontrada.get().getSaldo().subtract(valor);
            contaEncontrada.get().setSaldo(novoValor);
            contaUsuarioRepository.save(conta);
        }
    }

    @Override
    public BigDecimal consultarSaldo(Long contaId) {
        return null;
    }

    @Override
    public boolean tipoContaExiste(TiposContas tipoConta) {
        return false;
    }

    @Override
    public boolean jaExisteUmaContaIgual(ContaUsuario contaUsuario) {
        return false;
    }
}
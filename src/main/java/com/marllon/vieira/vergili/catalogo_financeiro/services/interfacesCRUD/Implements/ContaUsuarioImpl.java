package com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.Implements;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.ContaUsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.ContaUsuarioResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.mapper.ContaUsuarioMapper;
import com.marllon.vieira.vergili.catalogo_financeiro.models.ContaUsuario;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposContas;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.ContaUsuarioRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.ContaUsuarioAssociation;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.ContaUsuarioService;
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

    @Override
    public ContaUsuarioResponse criarConta(ContaUsuarioRequest request) {
        return null;
    }

    @Override
    public Optional<ContaUsuario> encontrarContaPorId(Long id) {
        return Optional.empty();
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
    public ContaUsuarioResponse atualizarUmaConta(Long id, ContaUsuarioRequest request) {
        return null;
    }

    @Override
    public void deletarConta(Long id) {

    }

    @Override
    public String verificarTipoConta(Long contaId) {
        return "";
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
package com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.Implements;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.UsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.ContaUsuarioResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.UsuarioResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DadosInvalidosException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.JaExisteException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.ContaNaoEncontrada;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.UsuarioNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.mapper.UsuarioMapper;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposContas;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.ContaUsuarioRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.UsuarioRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.UsuariosAssociation;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioImpl implements UsuariosService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuariosAssociation usuariosAssociation;

    @Autowired
    private UsuarioMapper usuarioMapper;

    @Autowired
    private ContaUsuarioRepository contaUsuarioRepository;

    @Override
    public UsuarioResponse criarUsuario(UsuarioRequest usuario) {

        //Analisando se o email passado já não existe uma conta criada
        if (existeUsuarioIgualCriadoPeloEmail(usuario.email())) {
            throw new JaExisteException("Já existe um usuário criado com esse email informado");
        }

        //Criando o novo usuário
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(usuario.nome());
        novoUsuario.setEmail(usuario.email());
        novoUsuario.setSenha(usuario.senha());
        novoUsuario.setTelefone(usuario.telefone());

        //Validações se os valores estão corretos serão realizados controller

        //Salvando os valores do novo usuário
        usuarioRepository.save(novoUsuario);

        //Retornando os valores
        return usuarioMapper.retornarDadosUsuario(novoUsuario);
    }

    @Override
    public Optional<UsuarioResponse> encontrarUsuarioPorId(Long id) {

        Optional<Usuario> usuarioEncontrado = Optional.of(usuarioRepository.findById(id).orElseThrow(()
                -> new UsuarioNaoEncontrado("O usuário não foi encontrado com essa id informada")));

        return Optional.ofNullable(usuarioMapper.retornarDadosUsuario(usuarioEncontrado.get()));
    }

    @Override
    public List<UsuarioResponse> buscarUsuarioPorNome(String nome) {

        List<Usuario> usuariosEncontrados = usuarioRepository.encontrarUsuarioPorNome(nome);

        if (usuariosEncontrados.isEmpty()) {
            throw new UsuarioNaoEncontrado(super.toString());
        }
        return usuariosEncontrados.stream().map(usuarioMapper::retornarDadosUsuario).toList();
    }

    @Override
    public List<UsuarioResponse> buscarPorContaId(Long contaId) {
        return List.of();
    }

    @Override
    public Page<UsuarioResponse> buscarTodosUsuarios(Pageable pageable) {

        List<Usuario> listaUsuariosEncontrados = usuarioRepository.findAll();
        if (listaUsuariosEncontrados.isEmpty()) {
            throw new UsuarioNaoEncontrado(super.toString());
        }
        Page<Usuario> paginaUsuarios = new PageImpl<>(listaUsuariosEncontrados);

        return paginaUsuarios.map(usuarioMapper::retornarDadosUsuario);
    }

    @Override
    public void deletarUsuario(Long id) {

        Usuario usuarioEncontrado = usuarioRepository.findById(id).orElseThrow();


        if (!usuarioEncontrado.getContasRelacionadas().isEmpty()) {
            List<ContaUsuario> contaEncontrada = usuarioEncontrado.getContasRelacionadas();
            for (ContaUsuario contasDoUsuario : contaEncontrada) {
                try {
                    usuariosAssociation.desassociarUsuarioComConta
                            (usuarioEncontrado.getId(), contasDoUsuario.getId());
                } catch (RuntimeException e) {
                    throw new DesassociationErrorException(e.getMessage());
                }
            }
        }
        if (!usuarioEncontrado.getTransacoesRelacionadas().isEmpty()) {
            List<HistoricoTransacao> transacoesEncontradas = usuarioEncontrado.getTransacoesRelacionadas();
            for (HistoricoTransacao transacoesPercorridas : transacoesEncontradas) {
                try {
                    usuariosAssociation.desassociarUsuarioComTransacao(usuarioEncontrado.getId(), transacoesPercorridas.getId());
                } catch (RuntimeException e) {
                    throw new DesassociationErrorException(e.getMessage());
                }
            }
        }
        if (!usuarioEncontrado.getPagamentosRelacionados().isEmpty()) {
            List<Pagamentos> pagamentosEncontrados = usuarioEncontrado.getPagamentosRelacionados();
            for (Pagamentos pagamentosPercorridos : pagamentosEncontrados) {
                try {
                    usuariosAssociation.desassociarUsuarioComPagamento(usuarioEncontrado.getId(), pagamentosPercorridos.getId());
                } catch (RuntimeException e) {
                    throw new DesassociationErrorException(e.getMessage());
                }
            }
        }
        if (!usuarioEncontrado.getCategoriasRelacionadas().isEmpty()) {
            List<CategoriaFinanceira> categoriaFinanceiraEncontrada = usuarioEncontrado.getCategoriasRelacionadas();

            for (CategoriaFinanceira categoriasPercorridas : categoriaFinanceiraEncontrada) {
                try {
                    usuariosAssociation.desassociarUsuarioComCategoria(usuarioEncontrado.getId(), categoriasPercorridas.getId());
                } catch (RuntimeException e) {
                    throw new DesassociationErrorException(e.getMessage());
                }
            }
        }

        if (!usuarioEncontrado.getContasRelacionadas().isEmpty()) {
            List<ContaUsuario> contasEncontradas = usuarioEncontrado.getContasRelacionadas();
            for (ContaUsuario contasPercorridas : contasEncontradas) {
                try {
                    usuariosAssociation.desassociarUsuarioComConta(usuarioEncontrado.getId(), contasPercorridas.getId());
                } catch (RuntimeException e) {
                    throw new DesassociationErrorException(e.getMessage());
                }
            }
        }

        //Remover agora a conta
        contaUsuarioRepository.deleteById(usuarioEncontrado.getId());
    }


    @Override
    public List<ContaUsuarioResponse> buscarContasAssociadas(Long usuarioId) {

        Optional<Usuario> usuarioEncontrado = Optional.of(usuarioRepository.findById(usuarioId)
                .orElseThrow());


            List<ContaUsuario> contasRelacionadas = usuarioEncontrado.get().getContasRelacionadas();

            if(contasRelacionadas.isEmpty()){
                throw new ContaNaoEncontrada(super.toString());
            }

        return contasRelacionadas.stream().map(
                contaUsuario -> new ContaUsuarioResponse(contaUsuario.getId(),
                        contaUsuario.getNome(),
                        contaUsuario.getSaldo(),
                        contaUsuario.getTipoConta()))
                .toList();

    }

    @Override
    public void alterarSenhaUsuario(Long id, String novaSenha) {

        //Encontrar o usuário
        Optional<Usuario> usuarioEncontrado = Optional.of(usuarioRepository.findById(id).orElseThrow());

        //Alterar a senha
        usuarioEncontrado.ifPresent(usuario -> usuario.setSenha(novaSenha));

    }

    @Override
    public boolean usuarioTemContaTipo(Long usuarioId, TiposContas tipoConta) {

        //Primeiramente, encontrando o usuário pela ID
        Optional<Usuario> usuarioEncontrado = Optional.of(usuarioRepository.findById(usuarioId).orElseThrow());

        List<ContaUsuario> contaUsuario = usuarioEncontrado.get().getContasRelacionadas();

        if (tipoConta.equals(contaUsuario.getFirst().getTipoConta())){
            return true;
        }
        return false;
    }

    @Override
    public boolean existePelaId(Long id) {

        boolean usuarioEncontradoPelaId = usuarioRepository.existsById(id);

        if (usuarioEncontradoPelaId){
            return true;
        }
        return false;
    }

    @Override
    public boolean existeUsuarioIgualCriadoPeloEmail(String emailUsuario) {

        boolean emailLocalizado = usuarioRepository.existsByEmail(emailUsuario);
        if(emailLocalizado){
            return true;
        }
        return false;
    }
}

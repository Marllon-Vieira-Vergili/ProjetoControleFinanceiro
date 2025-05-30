package com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.Implements;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.Usuario.UsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.Usuario.UsuarioUpdateRequest;
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
import jakarta.transaction.Transactional;
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
    @Transactional
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
                -> new UsuarioNaoEncontrado("Esse usuário com a id: " + id + "não foi localizada")));

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
    public List<UsuarioResponse> buscarPorIdConta(Long contaId) {

        //Encontrando a conta usuário pela id
        Optional<ContaUsuario> contaUsuarioEncontrada = contaUsuarioRepository.findById(contaId);
        Usuario usuarioLocalizado = null;
        if (contaUsuarioEncontrada.isPresent()){
           usuarioLocalizado = contaUsuarioEncontrada.get().getUsuarioRelacionado();
        }
        return Collections.singletonList(usuarioMapper.retornarDadosUsuario(usuarioLocalizado));
    }

    @Override
    public Page<UsuarioResponse> buscarTodosUsuarios(Pageable pageable) {

        List<Usuario> listaUsuariosEncontrados = usuarioRepository.findAll();
        if (listaUsuariosEncontrados.isEmpty()) {
            throw new UsuarioNaoEncontrado("Usuários não foram encontrados!");
        }
        Page<Usuario> paginaUsuarios = new PageImpl<>(listaUsuariosEncontrados);

        return paginaUsuarios.map(usuarioMapper::retornarDadosUsuario);
    }

    @Override
    @Transactional
    public UsuarioResponse atualizarDadosUsuario(Long id, UsuarioUpdateRequest dadosUsuario) {

        //Encontrando o usuário pela id
        Optional<Usuario> usuarioEncontrado = Optional.ofNullable(usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontrado(super.toString())));

        if (usuarioEncontrado.isPresent()){

            try{
                if (dadosUsuario.nome() != null){
                    usuarioEncontrado.get().setNome(dadosUsuario.nome());
                }
                if (dadosUsuario.email() != null){
                    usuarioEncontrado.get().setEmail(dadosUsuario.email());
                }
                if (dadosUsuario.telefone() != null){
                    usuarioEncontrado.get().setTelefone(dadosUsuario.telefone());
                }
                usuarioRepository.save(usuarioEncontrado.get());
            } catch (RuntimeException e) {
                throw new DadosInvalidosException(e.getMessage());
            }
        }



        return usuarioMapper.retornarDadosUsuario(usuarioEncontrado.get());
    }

    @Override
    @Transactional
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
            usuarioEncontrado.getContasRelacionadas().clear();
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
            usuarioEncontrado.getTransacoesRelacionadas().clear();
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
            usuarioEncontrado.getPagamentosRelacionados().clear();
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
            usuarioEncontrado.getCategoriasRelacionadas().clear();
        }


        //Remover agora a conta
        usuarioRepository.deleteById(usuarioEncontrado.getId());
    }


    @Override
    public List<ContaUsuarioResponse> buscarContasAssociadas(Long usuarioId) {

        Optional<Usuario> usuarioEncontrado = Optional.of(usuarioRepository.findById(usuarioId)
                .orElseThrow());


            List<ContaUsuario> contasRelacionadas = usuarioEncontrado.get().getContasRelacionadas();

            if(contasRelacionadas.isEmpty()){
                throw new ContaNaoEncontrada("Essa conta com a id: " +
                        usuarioEncontrado.get().getId() + "não foi localizada");
            }

        return contasRelacionadas.stream().map(
                contaUsuario -> new ContaUsuarioResponse(contaUsuario.getId(),
                        contaUsuario.getNome(),
                        contaUsuario.getSaldo(),
                        contaUsuario.getTipoConta()))
                .toList();

    }

    @Override
    @Transactional
    public void alterarSenhaUsuario(Long id, String novaSenha) {

        //Encontrar o usuário
        Usuario usuarioEncontrado = usuarioRepository.findById(id).orElseThrow(()->
                new UsuarioNaoEncontrado("Esse usuário com a id: " + id + "não foi localizada"));

        //Alterar a senha
        usuarioEncontrado.setSenha(novaSenha);

        //Salvando a nova senha
        usuarioRepository.save(usuarioEncontrado);

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

package com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.Implements;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.ContaUsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.UsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.ContaUsuarioResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.UsuarioResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.ContaNaoEncontrada;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.UsuarioNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.mapper.UsuarioMapper;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposContas;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.ContaUsuarioRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.UsuarioRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.UsuariosAssociation;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.ContaUsuarioService;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

public class UsuarioImpl implements UsuariosService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ContaUsuarioService contaUsuarioService;

    @Autowired
    private ContaUsuarioRepository contaUsuarioRepository;

    @Autowired
    private UsuariosAssociation usuariosAssociation;
    @Autowired
    private UsuarioMapper usuarioMapper;

    @Override
    public UsuarioResponse criarUsuario(UsuarioRequest usuario,ContaUsuarioRequest conta) {

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(usuario.nome());
        novoUsuario.setEmail(usuario.email());
        novoUsuario.setSenha(usuario.senha());
        novoUsuario.setTelefone(usuario.telefone());

        //Se o usuário não possuir uma conta ainda..
        ContaUsuarioResponse novaConta = contaUsuarioService.criarConta(conta);

        //Associar essa conta já criada a esse novo usuário
       usuariosAssociation.associarUsuarioComConta(novoUsuario.getId(), novaConta.id());

        //salvar os valores
        usuarioRepository.save(novoUsuario);

        //Retornar os valores mapeados
        return usuarioMapper.retornarDadosUsuario(novoUsuario);
    }

    @Override
    public Optional<UsuarioResponse> buscarUsuarioPorId(Long id) {

        Usuario usuarioEncontrado = usuarioRepository.findById(id).orElseThrow(()
                -> new UsuarioNaoEncontrado("Usuário não encontrado com essa id"));

        return Optional.ofNullable(usuarioMapper.retornarDadosUsuario(usuarioEncontrado));
    }

    @Override
    public List<UsuarioResponse> buscarUsuarioPorNome(String nome) {

        List<Usuario> usuarioEncontrado = usuarioRepository.encontrarUsuarioPorNome(nome);
        if(usuarioEncontrado.isEmpty()){
            throw new UsuarioNaoEncontrado("Usuario(s) com o nome: " + usuarioEncontrado + " não foi encontrado");
        }
        return usuarioEncontrado.stream().map(usuarioMapper::retornarDadosUsuario).collect(Collectors.toList());
    }

    @Override
    public List<UsuarioResponse> buscarPorContaId(Long contaId) {

        ContaUsuario contaUsuarioId = contaUsuarioRepository.findById(contaId).orElseThrow(()
                -> new ContaNaoEncontrada("Nenhuma conta foi encontrada com a id informada"));

        Usuario usuarioEncontrada = contaUsuarioId.getUsuarioRelacionado();

        if(usuarioEncontrada == null){
            throw new UsuarioNaoEncontrado("Não foi encontrado nenhum usuário associado a essa conta");
        }

        return List.of(usuarioMapper.retornarDadosUsuario(usuarioEncontrada));
    }

    @Override
    public Page<UsuarioResponse> buscarTodosUsuarios(Pageable pageable) {

        Page<Usuario> usuariosEncontrados = usuarioRepository.findAll(pageable);

        if(usuariosEncontrados.isEmpty()){
            throw new NoSuchElementException("Não há nenhum usuário salvo na base de dados");
        }
        return new PageImpl<>(usuariosEncontrados.stream()
                .map(usuarioMapper::retornarDadosUsuario).toList());
    }

    @Override
    public void deletarUsuario(Long id) {

        Usuario usuarioSerRemovido = usuarioRepository.findById(id).orElseThrow(() ->
                new ContaNaoEncontrada("Não foi encontrado nenhum usuario com essa id informada"));


        try{
            for(ContaUsuario contasRelacioandas: usuarioSerRemovido.getContasRelacionadas()){
                usuariosAssociation.desassociarUsuarioComConta(id, contasRelacioandas.getId());
            }
        }catch (DesassociationErrorException e){
            throw new DesassociationErrorException("Erro ao desassociar usuário de suas contas relacionadas:  " + e.getMessage());
        }



        try{
            for(CategoriaFinanceira categoriasEncontradas: usuarioSerRemovido.getCategoriasRelacionadas()){
                usuariosAssociation.desassociarUsuarioComCategoria(id, categoriasEncontradas.getId());
            }
        }catch (DesassociationErrorException e){
            throw new DesassociationErrorException("Erro ao desassociar usuário de suas Categorias Financeiras:  " + e.getMessage());
        }

        try{
            for(Pagamentos pagamentosEncontrados: usuarioSerRemovido.getPagamentosRelacionados()){
                usuariosAssociation.desassociarUsuarioComPagamento(id, pagamentosEncontrados.getId());
            }
        }catch (DesassociationErrorException e){
            throw new DesassociationErrorException("Erro ao desassociar usuário de seus pagamentos:  " + e.getMessage());
        }

        try{
            for(HistoricoTransacao historicoTransacaoEncontrado: usuarioSerRemovido.getTransacoesRelacionadas()){
                usuariosAssociation.desassociarUsuarioComTransacao(id, historicoTransacaoEncontrado.getId());
            }
        }catch (DesassociationErrorException e){
            throw new DesassociationErrorException("Erro ao desassociar usuário de seus historicos de transações:  " + e.getMessage());
        }

        //Remover a conta encontrada
        usuarioRepository.delete(usuarioSerRemovido);
    }

    @Override
    public List<ContaUsuarioResponse> buscarContasAssociadas(Long usuarioId) {
        return List.of();
    }

    @Override
    public boolean usuarioTemContaTipo(Long usuarioId, TiposContas tipoConta) {
        return false;
    }

    @Override
    public boolean existePelaId(Long id) {
        return false;
    }

    @Override
    public boolean existeUsuarioIgual(UsuarioRequest usuario) {
        return false;
    }
}

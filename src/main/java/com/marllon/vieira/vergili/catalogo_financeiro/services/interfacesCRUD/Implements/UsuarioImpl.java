package com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.Implements;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.UsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.ContaUsuarioResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.UsuarioResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DadosInvalidosException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.JaExisteException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.UsuarioNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.mapper.UsuarioMapper;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Usuario;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposContas;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.UsuarioRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.UsuariosAssociation;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    @Override
    public UsuarioResponse criarUsuario(UsuarioRequest usuario) {

        //Analisando se o email passado já não existe uma conta criada
        if(existeUsuarioIgualCriadoPeloEmail(usuario.email())){
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
        ->new UsuarioNaoEncontrado("O usuário não foi encontrado com essa id informada")));

        return Optional.ofNullable(usuarioMapper.retornarDadosUsuario(usuarioEncontrado.get()));
    }

    @Override
    public List<UsuarioResponse> buscarUsuarioPorNome(String nome) {
        return List.of();
    }

    @Override
    public List<UsuarioResponse> buscarPorContaId(Long contaId) {
        return List.of();
    }

    @Override
    public Page<UsuarioResponse> buscarTodosUsuarios(Pageable pageable) {
        return null;
    }

    @Override
    public void deletarUsuario(Long id) {

    }

    @Override
    public List<ContaUsuarioResponse> buscarContasAssociadas(Long usuarioId) {
        return List.of();
    }

    @Override
    public void alterarSenhaUsuario(Long id, String novaSenha) {

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
    public boolean existeUsuarioIgualCriadoPeloEmail(String emailUsuario) {

        boolean emailLocalizado = usuarioRepository.existsByEmail(emailUsuario);
        if(emailLocalizado){
            return true;
        }
        return false;
    }
}

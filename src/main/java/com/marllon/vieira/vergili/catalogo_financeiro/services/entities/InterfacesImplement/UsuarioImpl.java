package com.marllon.vieira.vergili.catalogo_financeiro.services.entities.InterfacesImplement;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.UsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.JaExisteException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.UsuarioNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Usuario;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.UsuarioRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;


@Service
public class UsuarioImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;


    @Override
    public Usuario criarNovoUsuario(UsuarioRequest usuario) {

        //Criar um novo usuário
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(usuario.nome().toUpperCase());
        novoUsuario.setEmail(usuario.email());
        novoUsuario.setSenha(usuario.senha());
        novoUsuario.setTelefone(usuario.telefone());


        //Verificar se o usuário não digitou valores nulos ou vazios
        verificarSeDigitouValoresNulos(novoUsuario);


        //Verificar se esse nome, email e telefone passado do parâmetro já não existe no banco um igual
        if(usuarioRepository.existsByNomeAndEmailAndTelefone(novoUsuario.getNome(),
                novoUsuario.getEmail(), novoUsuario.getTelefone())){
            throw new JaExisteException("Já existe uma conta com esse nome, este saldo e tipo de conta criados");
        }

        //Salvar o novo usuário, se estiver tudo certo
        usuarioRepository.save(novoUsuario);

        //Retornar a resposta
        return novoUsuario;

    }

    @Override
    public Usuario encontrarUsuarioPorId(Long id) {

        //Encontrar o usuário pela id
        Usuario usuarioEncontrado = usuarioRepository.findById(id).orElseThrow(() ->
                new UsuarioNaoEncontrado("Nenhum usuário encontrado com a ID informada"));

        //Se não tiver nenhum usuário no banco de dados
        if(usuarioRepository.findAll().isEmpty()){
            throw  new UsuarioNaoEncontrado("Não há nenhum usuário no banco de dados, ele está vazio");
        }
        //Retornar os dados da id encontrada
        return usuarioEncontrado;
    }

    @Override
    public List<Usuario> encontrarTodosUsuarios() {

        //Encontrando todas as contas
        List<Usuario> todosUsuariosEncontrados = usuarioRepository.findAll();

        //Se não tiver nenhum usuario encontrado, retornar Exception
        if(todosUsuariosEncontrados.isEmpty()){
            throw new UsuarioNaoEncontrado("Não há nenhuma conta no banco de dados, ele está vazio");
        }

        //Retornar a lista de todas as contas encontradas
        return todosUsuariosEncontrados;

    }


    @Override
    public Usuario atualizarDadosUsuario(Long id, UsuarioRequest usuario) {

        //Encontrando o usuario que quero atualizar, pela sua id
        Usuario usuarioEncontrado = usuarioRepository.findById(id).orElseThrow(() ->
                new UsuarioNaoEncontrado("Nenhum usuário com essa id foi encontrada"));

        //Se achar.. vamos atualizar os dados do usuario
        usuarioEncontrado.setNome(usuario.nome().toUpperCase());
        usuarioEncontrado.setEmail(usuario.email());
        usuarioEncontrado.setSenha(usuario.senha());
        usuarioEncontrado.setTelefone(usuario.telefone());

        //Verificar se o usuário não digitou valores nulos ou vazios
        verificarSeDigitouValoresNulos(usuarioEncontrado);

        //Salvar os dados atualizados do usuário
        usuarioRepository.save(usuarioEncontrado);

        //Retornar os dados atualizados
        return usuarioEncontrado;
    }

    @Override
    public Usuario removerUsuarioPorId(Long id) {

        //encontrar o usuario pela id
        Usuario usuarioEncontrado = usuarioRepository.findById(id).orElseThrow(() -> new
                UsuarioNaoEncontrado("Nenhum usuario foi encontrado com esta id informada"));

        //Remover o usuario encontrado
        usuarioRepository.delete(usuarioEncontrado);

        //Retornar o valor do usuario que foi deletado
        return usuarioEncontrado;
    }

    @Override
    public void salvarNovoUsuario(Usuario novoUsuario) {
        usuarioRepository.save(novoUsuario);
    }

    @Override
    public List<Usuario> encontrarUsuarioPorNome(String nome) {


        //Encontrar a conta pela id
        List<Usuario> usuariosEncontrados = usuarioRepository.encontrarUsuarioPorNome(nome);

        //Se não tiver nenhuma conta no banco de dados
        if (usuarioRepository.findAll().isEmpty()) {
            throw new UsuarioNaoEncontrado("Não há nenhum usuário no banco de dados, ele está vazio");
        }

        for (Usuario usuariosPercorridos : usuariosEncontrados) {
            if (usuariosPercorridos.getNome().equalsIgnoreCase(nome)) {
                return List.of(usuariosPercorridos);
            }
        }
        throw new UsuarioNaoEncontrado("Nenhum usuário encontrado com o nome: " + nome);
    }

    @Override
    public Usuario encontrarUsuarioPeloEmail(String email) {

        Usuario usuarioEncontrado = usuarioRepository.encontrarUsuarioPeloEmail(email);

        if(usuarioEncontrado == null){
            throw new UsuarioNaoEncontrado("Nenhum usuário foi encontrado com esse email");
        }
        return usuarioEncontrado;
    }


    public void verificarSeDigitouValoresNulos(Usuario usuarioEncontrado){
//Verificar se o usuário não digitou valores nulos ou vazios
        if(usuarioEncontrado.getNome() == null || usuarioEncontrado.getNome().isEmpty() ||
                usuarioEncontrado.getEmail() == null || usuarioEncontrado.getEmail().isEmpty() ||
                usuarioEncontrado.getSenha() == null || usuarioEncontrado.getSenha().isEmpty() ||
                usuarioEncontrado.getTelefone() == null || usuarioEncontrado.getTelefone().isEmpty()){
            throw new IllegalArgumentException("Por favor, preencha todos os campos obrigatórios");
        }
    }
}

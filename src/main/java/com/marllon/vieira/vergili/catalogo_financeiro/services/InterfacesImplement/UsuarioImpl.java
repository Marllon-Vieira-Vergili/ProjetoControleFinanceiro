package com.marllon.vieira.vergili.catalogo_financeiro.services.InterfacesImplement;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.UsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.UsuarioResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.models.Usuario;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.UsuarioRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.services.Interfaces.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;


@Service
public class UsuarioImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;


    @Override
    public UsuarioResponse criarNovoUsuario(UsuarioRequest usuario) {

        //Criar um novo usuário
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(usuario.nome().toUpperCase());
        novoUsuario.setEmail(usuario.email());
        novoUsuario.setSenha(usuario.senha());
        novoUsuario.setTelefone(usuario.telefone());


        //Verificar se esse nome, email e telefone passado do parâmetro já não existe no banco um igual
        if(usuarioRepository.existsByNomeAndEmailAndTelefone(novoUsuario.getNome(),
                novoUsuario.getEmail(), novoUsuario.getTelefone())){
            throw new IllegalArgumentException("Já existe uma conta com esse nome, este saldo e tipo de conta criados");
        }
        //Salvar o novo usuário, se estiver tudo certo
        usuarioRepository.save(novoUsuario);

        //Retornar a resposta
        return new UsuarioResponse(novoUsuario.getId(), novoUsuario.getNome(), novoUsuario.getEmail(),
                novoUsuario.getTelefone());

    }

    @Override
    public UsuarioResponse encontrarUsuarioPorId(Long id) {

        //Encontrar o usuário pela id
        Usuario usuarioEncontrado = usuarioRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Nenhum usuário encontrado com a ID informada"));

        //Se não tiver nenhum usuário no banco de dados
        if(usuarioRepository.findAll().isEmpty()){
            throw  new NullPointerException("Não há nenhum usuário no banco de dados, ele está vazio");
        }
        //Retornar os dados da id encontrada
        return new UsuarioResponse(usuarioEncontrado.getId(), usuarioEncontrado.getNome(), usuarioEncontrado.getEmail()
                ,usuarioEncontrado.getTelefone());
    }

    @Override
    public List<UsuarioResponse> encontrarTodosUsuarios() {

        //Encontrando todas as contas
        List<Usuario> todosUsuariosEncontrados = usuarioRepository.findAll();

        //Se não tiver nenhum usuario encontrado, retornar Exception
        if(todosUsuariosEncontrados.isEmpty()){
            throw new NullPointerException("Não há nenhuma conta no banco de dados, ele está vazio");
        }

        //Retornar a lista de todas as contas encontradas
        return todosUsuariosEncontrados.stream().map(usuario ->
                new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail(),
                        usuario.getTelefone())).toList();

    }


    @Override
    public UsuarioResponse atualizarDadosUsuario(Long id, UsuarioRequest usuario) {

        //Encontrando o usuario que quero atualizar, pela sua id
        Usuario usuarioEncontrado = usuarioRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Nenhum usuário com essa id foi encontrada"));

        //Se achar.. vamos atualizar os dados do usuario
        usuarioEncontrado.setNome(usuario.nome().toUpperCase());
        usuarioEncontrado.setEmail(usuario.email());
        usuarioEncontrado.setSenha(usuario.senha());
        usuarioEncontrado.setTelefone(usuario.telefone());

        //Salvar os dados atualizados do usuário
        usuarioRepository.save(usuarioEncontrado);

        //Retornar os dados atualizados
        return new UsuarioResponse(usuarioEncontrado.getId(), usuarioEncontrado.getNome(), usuarioEncontrado.getEmail()
                ,usuarioEncontrado.getTelefone());
    }

    @Override
    public UsuarioResponse removerUsuarioPorId(Long id) {

        //encontrar o usuario pela id
        Usuario usuarioEncontrado = usuarioRepository.findById(id).orElseThrow(() -> new
                NoSuchElementException("Nenhum usuario foi encontrado com esta id informada"));

        //Remover o usuario encontrado
        usuarioRepository.delete(usuarioEncontrado);

        //Retornar o valor do usuario que foi deletado
        return new UsuarioResponse(usuarioEncontrado.getId(), usuarioEncontrado.getNome(), usuarioEncontrado.getEmail()
                ,usuarioEncontrado.getTelefone());
    }


}

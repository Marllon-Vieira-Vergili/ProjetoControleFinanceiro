package com.marllon.vieira.vergili.catalogo_financeiro.services.associations.InterfacesImplements;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.associations.UsuarioAssociationRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.ContaUsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.UsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.associations.UsuarioAssociationResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.ContaUsuarioResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.UsuarioResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.UsuarioRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.services.associations.Interfaces.IUsuario;
import com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces.ContaUsuarioService;
import com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces.UsuarioService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;


@Service
public class IUsuarioImplement implements IUsuario {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ContaUsuarioService contaUsuarioService;

    @Override
    @Transactional
    public UsuarioAssociationResponse criarEAssociarUsuario(UsuarioAssociationRequest novoUsuario) {
        try {
            // Criar o usuário
            Usuario novoUsuarioCriado = usuarioService.criarNovoUsuario(novoUsuario.usuario());
            if (novoUsuarioCriado == null) {
                throw new RuntimeException("Erro: Usuário não pôde ser criado.");
            }

            // Criar a nova conta de usuário
            ContaUsuario novaContaCriada = contaUsuarioService.criarNovaConta(novoUsuario.contaUsuario());

            if (novaContaCriada == null) {
                throw new RuntimeException("Erro: Conta de usuário não pôde ser criada.");
            }

            //Senão, associar o usuário com a conta
            novoUsuarioCriado.associarUsuarioComConta(novaContaCriada);

            //Salvar os dados
            usuarioRepository.save(novoUsuarioCriado);


            // Converter os valores para DTO
            UsuarioResponse usuarioResponse = new UsuarioResponse(novoUsuarioCriado.getId(), novoUsuarioCriado.getNome(),
                    novoUsuarioCriado.getEmail(), novoUsuarioCriado.getTelefone());

            List<ContaUsuarioResponse> contaUsuarioResponse = Collections.singletonList(
                    new ContaUsuarioResponse(
                            novaContaCriada.getId(),
                    novaContaCriada.getNome(),
                    novaContaCriada.getSaldo(),
                    novaContaCriada.getTipoConta()));

            // Retornar os dados do novo usuário criado e sua conta
            return new UsuarioAssociationResponse(usuarioResponse, contaUsuarioResponse);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar e associar usuário: " + e.getMessage(), e);
        }
    }

    @Override
    public UsuarioAssociationResponse encontrarUsuarioAssociadoPorId(Long id) {

        //Encontrar o usuário pela id
        Usuario usuarioEncontrado = usuarioService.encontrarUsuarioPorId(id);

        //Se achar o usuário, retornar suas contas associadas
        List<ContaUsuario> contasEncontradas = usuarioEncontrado.getContasRelacionadas();

        if (contasEncontradas.isEmpty()) {
            throw new NoSuchElementException("esse usuário não possui nenhuma conta encontrada");
        }

        List<ContaUsuarioResponse> contaUsuarioResponse = contasEncontradas.stream().map(contaUsuario ->
                new ContaUsuarioResponse(contaUsuario.getId(),
                        contaUsuario.getNome(),
                        contaUsuario.getSaldo(),contaUsuario.getTipoConta()
                )).toList();

        //Se encontrar, obter os dto
        UsuarioResponse usuarioResponse = new UsuarioResponse(usuarioEncontrado.getId(), usuarioEncontrado.getNome(),
                usuarioEncontrado.getEmail(), usuarioEncontrado.getTelefone());

        return new UsuarioAssociationResponse(usuarioResponse, contaUsuarioResponse);
    }

    @Override
    public List<UsuarioAssociationResponse> encontrarTodosUsuariosAssociados() {

        //Encontrar todos os usuários
        List<Usuario> todosUsuariosEncontrados = usuarioService.encontrarTodosUsuarios();

        //Encontrar suas contas associadas
        return todosUsuariosEncontrados.stream().map(usuario -> {
            UsuarioResponse usuarioResponse =
                    new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getTelefone()
                    );

            List<ContaUsuarioResponse> contasUsuario = usuario.getContasRelacionadas().stream().map(
                    contaUsuario -> new ContaUsuarioResponse(contaUsuario.getId(),
                            contaUsuario.getNome(), contaUsuario.getSaldo(),contaUsuario.getTipoConta()
                    )).toList();

            //Retornar o usuário associado e sua lista de contas
            return new UsuarioAssociationResponse(usuarioResponse, contasUsuario);
        }).toList();

    }

    @Override
    public List<UsuarioAssociationResponse> encontrarUsuarioAssociadoPorNome(String nome) {

        // Encontrar todos os usuários com o nome informado
        List<Usuario> usuariosEncontrados = usuarioService.encontrarUsuarioPorNome(nome);

        if (usuariosEncontrados.isEmpty()) {
            throw new NoSuchElementException("Nenhum usuário encontrado com esse nome");
        }

        List<UsuarioAssociationResponse> resposta = new ArrayList<>();

        for (Usuario usuario : usuariosEncontrados) {

            // Verifica se o usuário possui contas associadas
            if (usuario.getContasRelacionadas() == null || usuario.getContasRelacionadas().isEmpty()) {
                continue;
            }

            // Converte contas relacionadas para DTO
            List<ContaUsuarioResponse> contaUsuarioResponse = usuario.getContasRelacionadas().stream()
                    .map(conta -> new ContaUsuarioResponse(
                            conta.getId(),
                            conta.getNome(),
                            conta.getSaldo(),conta.getTipoConta()))
                    .toList();

            // Converte o usuário para DTO
            UsuarioResponse usuarioResponse = new UsuarioResponse(
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.getEmail(),
                    usuario.getTelefone());

            // Cria o DTO de resposta combinando o usuário com suas contas
            resposta.add(new UsuarioAssociationResponse(usuarioResponse, contaUsuarioResponse));
        }

        if (resposta.isEmpty()) {
            throw new NoSuchElementException("Os usuários encontrados não possuem contas associadas");
        }

        return resposta;
    }



    @Override
    public UsuarioAssociationResponse atualizarUsuarioAssociado(Long id, UsuarioRequest usuarioAtualizado) {

        //Atualizar os dados do usuário
        Usuario usuariofoiAtualizado = usuarioService.atualizarDadosUsuario(id, usuarioAtualizado);


        //Retornar em DTO os valores convertidos
        UsuarioResponse usuarioResponse = new UsuarioResponse(usuariofoiAtualizado.getId(),
                usuariofoiAtualizado.getNome(), usuariofoiAtualizado.getEmail(), usuariofoiAtualizado.getTelefone());

        List<ContaUsuarioResponse> contaUsuarioResponse = usuariofoiAtualizado.getContasRelacionadas().stream().map(
                contaUsuario -> new ContaUsuarioResponse(contaUsuario.getId(), contaUsuario.getNome(),
                        contaUsuario.getSaldo(),contaUsuario.getTipoConta())).toList();


        return new UsuarioAssociationResponse(usuarioResponse, contaUsuarioResponse);
    }

    @Override
    public void removerUsuarioAssociadoPelaId(Long id) {

        // Encontrar o usuário pela sua id
        Usuario usuarioEncontrado = usuarioService.encontrarUsuarioPorId(id);

        // Remover associações com contas
        for (ContaUsuario contaEncontrada : new ArrayList<>(usuarioEncontrado.getContasRelacionadas())) {
            try {
                usuarioEncontrado.desassociarUsuarioComConta(contaEncontrada);
            } catch (Exception e) {
                System.out.println("Não foi possível desassociar conta: " + e.getMessage());
            }
        }

        // Remover associações com categorias
        for (CategoriaFinanceira categoriaFinanceira : new ArrayList<>(usuarioEncontrado.getCategoriasRelacionadas())) {
            try {
                usuarioEncontrado.desassociarUsuarioComCategoria(categoriaFinanceira);
            } catch (Exception e) {
                System.out.println("Não foi possível desassociar categoria: " + e.getMessage());
            }
        }

        // Remover associações com pagamentos
        for (Pagamentos pagamentosEncontrados : new ArrayList<>(usuarioEncontrado.getPagamentosRelacionados())) {
            try {
                usuarioEncontrado.desassociarUsuarioComPagamento(pagamentosEncontrados);
            } catch (Exception e) {
                System.out.println("Não foi possível desassociar pagamento: " + e.getMessage());
            }
        }

        // Remover associações com transações
        for (HistoricoTransacao transacoesEncontradas : new ArrayList<>(usuarioEncontrado.getTransacoesRelacionadas())) {
            try {
                usuarioEncontrado.desassociarUsuarioComTransacao(transacoesEncontradas);
            } catch (Exception e) {
                System.out.println("Não foi possível desassociar transação: " + e.getMessage());
            }
        }

        // Remover o usuário
        usuarioService.removerUsuarioPorId(id);
    }

}

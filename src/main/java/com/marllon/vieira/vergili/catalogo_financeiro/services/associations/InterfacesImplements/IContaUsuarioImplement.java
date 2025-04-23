package com.marllon.vieira.vergili.catalogo_financeiro.services.associations.InterfacesImplements;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.associations.ContaUsuarioAssociationRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.ContaUsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.associations.ContaUsuarioAssociationResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.associations.UsuarioAssociationResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.ContaUsuarioResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.UsuarioResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposContas;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.ContaUsuarioRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.services.associations.Interfaces.IContaUsuario;
import com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces.ContaUsuarioService;
import com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces.UsuarioService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IContaUsuarioImplement implements IContaUsuario {

    @Autowired
    private ContaUsuarioService contaUsuarioService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ContaUsuarioRepository contaUsuarioRepository;

    @Override
    @Transactional
    public ContaUsuarioAssociationResponse criarEAssociarConta(ContaUsuarioAssociationRequest novaConta) {

        //Verificando se já existe um usuário
        Usuario usuarioEncontrado = usuarioService.encontrarUsuarioPeloEmail(novaConta.emailUsuario());

        //Se não existir ja jogar uma exceção
        if (usuarioEncontrado.getEmail().isEmpty()) {
            throw new NoSuchElementException("Nenhum usuário com o email: " + usuarioEncontrado + " encontrado!");
        }
        //Se ja existir uma associação igual a que o usuário está passando, retornar exceção
        TiposContas tiposContas = novaConta.dadosUsuario().tipoConta();
        boolean jaPossuiMesmoTipo = usuarioEncontrado.getContasRelacionadas().stream().map
                (ContaUsuario::getTipoConta).anyMatch(tipo -> tipo.equals(tiposContas));
        if (jaPossuiMesmoTipo) {
            throw new IllegalArgumentException("Já existe esse tipo de associada a esse usuário");
        }

        //Criar uma nova conta
        ContaUsuario novaContaCriada = contaUsuarioService.criarNovaConta(novaConta.dadosUsuario());
        //Associando a um usuário já existente.. se esse usuário ja tiver uma conta,terá mais uma
        novaContaCriada.associarContaComUsuario(usuarioEncontrado);

        //Salvar a conta
        contaUsuarioRepository.save(novaContaCriada);

        //Mapear o usuário correto
        List<UsuarioResponse> usuarioResponse = Collections.singletonList(new UsuarioResponse(usuarioEncontrado.getId(),
                usuarioEncontrado.getNome(), usuarioEncontrado.getEmail(), usuarioEncontrado.getTelefone()));

        //Mapear a conta correta
        List<ContaUsuarioResponse> contaResponse = Collections.singletonList(new ContaUsuarioResponse
                (novaContaCriada.getId(), novaContaCriada.getNome(), novaContaCriada.getSaldo()));

        //Retornar o DTO ao usuário
        return new ContaUsuarioAssociationResponse(contaResponse, usuarioResponse);
    }

    @Override
    public ContaUsuarioAssociationResponse encontrarContaAssociadaPorId(Long id) {

        //Encontrar a conta pela id
        ContaUsuario contaEncontrada = contaUsuarioService.encontrarContaPorId(id);

        //Se achar a conta, retornar sueu usuario associado
        Usuario usuarioEncontrado = contaEncontrada.getUsuarioRelacionado();

        if (usuarioEncontrado == null) {
            throw new NoSuchElementException("esse usuário não possui nenhuma conta encontrada");
        }
        //Se encontrar, obter os dto
        UsuarioResponse usuarioResponse = new UsuarioResponse(usuarioEncontrado.getId(), usuarioEncontrado.getNome(),
                usuarioEncontrado.getEmail(), usuarioEncontrado.getTelefone());

        List<ContaUsuarioResponse> contaUsuarioResponse = Collections.singletonList
                (new ContaUsuarioResponse(contaEncontrada.getId(), contaEncontrada.getNome(),
                        contaEncontrada.getSaldo()));

        return new ContaUsuarioAssociationResponse(contaUsuarioResponse, Collections.singletonList(usuarioResponse));
    }

    @Override
    public List<ContaUsuarioAssociationResponse> encontrarTodasContasAssociadas() {

        //Encontrar todas as contas
        List<ContaUsuario> todasContasEncontradas = contaUsuarioService.encontrarTodasContas();

        //Mapear cada conta encontrada
        List<ContaUsuarioResponse> contas = todasContasEncontradas.stream()
                .map(contaUsuario -> new ContaUsuarioResponse(contaUsuario.getId(),
                        contaUsuario.getNome(), contaUsuario.getSaldo())).toList();

//Encontrar seus usuarios associados
        List<UsuarioResponse> usuarioResponse = todasContasEncontradas.stream()
                .map(ContaUsuario::getUsuarioRelacionado).distinct().map(usuario -> new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getTelefone())).toList();


        return Collections.singletonList(new ContaUsuarioAssociationResponse(contas, usuarioResponse));
    }

    @Override
    @Transactional
    public ContaUsuarioAssociationResponse atualizarContaAssociada(Long id, ContaUsuarioRequest contaAtualizada) {

        //Atualizar os dados da conta

        ContaUsuario contaSerAtualizada = contaUsuarioService.atualizarConta(id, contaAtualizada);

        if (contaSerAtualizada.getId() == null) {
            throw new IllegalArgumentException("Não foi possível atualizar essa conta, pois não foi encontrado o id");
        }

        //Retornar em DTO os valores convertidos

        List<ContaUsuarioResponse> contaUsuarioResponse = Collections.singletonList
                (new ContaUsuarioResponse(contaSerAtualizada.getId(), contaSerAtualizada.getNome(),
                        contaSerAtualizada.getSaldo()));

        Usuario usuarioRelacionado = contaSerAtualizada.getUsuarioRelacionado();

        UsuarioResponse usuarioResponse = new UsuarioResponse(usuarioRelacionado.getId(), usuarioRelacionado.getNome(),
                usuarioRelacionado.getEmail(), usuarioRelacionado.getTelefone());


        return new ContaUsuarioAssociationResponse(contaUsuarioResponse, Collections.singletonList(usuarioResponse));
    }

    @Override
    @Transactional
    public void removerContaAssociadaPelaId(Long id) {

        // Encontrar a conta pela sua id
        ContaUsuario contaEncontrada = contaUsuarioService.encontrarContaPorId(id);

        // Remover associações com usuário
            Usuario usuarioEncontrado = contaEncontrada.getUsuarioRelacionado();
            contaEncontrada.desassociarContaDeUsuario(usuarioEncontrado);



        // Remover associações com categorias
        for (CategoriaFinanceira categoriaFinanceira : new ArrayList<>(contaEncontrada.getCategoriasRelacionadas())) {
            try {
                contaEncontrada.desassociarContaDeCategorias(categoriaFinanceira);
            } catch (Exception e) {
                System.out.println("Não foi possível desassociar categoria: " + e.getMessage());
            }
        }

        // Remover associações com pagamentos
        for (Pagamentos pagamentosEncontrados : new ArrayList<>(contaEncontrada.getPagamentosRelacionados())) {
            try {
                contaEncontrada.desassociarContaDePagamento(pagamentosEncontrados);
            } catch (Exception e) {
                System.out.println("Não foi possível desassociar pagamento: " + e.getMessage());
            }
        }

        // Remover associações com transações
        for (HistoricoTransacao transacoesEncontradas : new ArrayList<>(contaEncontrada.getTransacoesRelacionadas())) {
            try {
                contaEncontrada.desassociarContaDeHistoricoDeTransacao(transacoesEncontradas);
            } catch (Exception e) {
                System.out.println("Não foi possível desassociar transação: " + e.getMessage());
            }
        }
        // Encontrar a conta pela sua id e remover
        ContaUsuario contadeletada = contaUsuarioService.removerContaPorId(contaEncontrada.getId());
    }
}
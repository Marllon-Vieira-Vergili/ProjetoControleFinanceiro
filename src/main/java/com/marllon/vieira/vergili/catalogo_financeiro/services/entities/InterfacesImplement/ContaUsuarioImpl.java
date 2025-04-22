package com.marllon.vieira.vergili.catalogo_financeiro.services.entities.InterfacesImplement;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.ContaUsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.models.CategoriaFinanceira;
import com.marllon.vieira.vergili.catalogo_financeiro.models.ContaUsuario;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposContas;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.ContaUsuarioRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces.ContaUsuarioService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;


@Service
public class ContaUsuarioImpl implements ContaUsuarioService {

    @Autowired
    private ContaUsuarioRepository contaUsuarioRepository;

    @Override
    @Transactional
    public ContaUsuario criarNovaConta(ContaUsuarioRequest conta) {

        //Criar uma nova conta
        ContaUsuario novaConta = new ContaUsuario();
        novaConta.setNome(conta.nome().toUpperCase());
        novaConta.setSaldo(conta.saldo());
        novaConta.setTipoConta(conta.tipoConta());

        //Validar o subtipo de conta, de acordo com o que o usuário escolheu
        novaConta.associarTipoConta(conta.tipoConta());

        //Verificar se o usuário não digitou valores nulos ou vazios
        if (novaConta.getNome() == null || novaConta.getNome().isEmpty() ||
                novaConta.getSaldo() == null || novaConta.getSaldo().compareTo(BigDecimal.valueOf(0.0)) <= 0) {
            throw new IllegalArgumentException("Por favor, preencha todos os campos obrigatórios");
        } else {
            TiposContas.todosTiposValidos();
        }


        //Verificar se esse nome, e saldo conta passado do parâmetro já não existe no banco um igual
        if (contaUsuarioRepository.existsByNomeAndSaldo(novaConta.getNome(), novaConta.getSaldo())) {
            throw new IllegalArgumentException("Já existe uma conta com esse nome e saldo criados");
        }



        //Salvar a novaConta, se estiver tudo certo
        contaUsuarioRepository.save(novaConta);

        //Retornar a resposta
        return novaConta;
    }

    @Override
    public ContaUsuario encontrarContaPorId(Long id) {

        //Encontrar a conta pela id
        ContaUsuario contaEncontrada = contaUsuarioRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Nenhuma conta encontrada com a ID informada"));

        //Se não tiver nenhuma conta no banco de dados
        if (contaUsuarioRepository.findAll().isEmpty()) {
            throw new NullPointerException("Não há nenhuma conta no banco de dados, ele está vazio");
        }
        //Retornar os dados da id encontrada
        return contaEncontrada;
    }

    @Override
    public ContaUsuario encontrarContaPorNome(String nome) {


        //Encontrar a conta pela id
        List<ContaUsuario> contasEncontradas = contaUsuarioRepository.findAll();

        //Se não tiver nenhuma conta no banco de dados
        if (contaUsuarioRepository.findAll().isEmpty()) {
            throw new NullPointerException("Não há nenhuma conta no banco de dados, ele está vazio");
        }

        for (ContaUsuario contasPercorridas : contasEncontradas) {
            if (contasPercorridas.getNome().equalsIgnoreCase(nome)) {

                return contasPercorridas;
            }
        }
        throw new IllegalArgumentException("Nenhuma conta encontrada com o nome: " + nome);
    }

    @Override
    public List<ContaUsuario> encontrarTodasContas() {

        //Encontrando todas as contas
        List<ContaUsuario> todasContasEncontradas = contaUsuarioRepository.findAll();

        //Se não tiver nenhuma conta encontrada, retornar Exception
        if (todasContasEncontradas.isEmpty()) {
            throw new NullPointerException("Não há nenhuma conta no banco de dados, ele está vazio");
        }

        //Retornar a lista de todas as contas encontradas
        return todasContasEncontradas;
    }


    @Override
    @Transactional
    public ContaUsuario atualizarConta(Long id, ContaUsuarioRequest conta) {

        //Encontrando a conta que quero atualizar, pela sua id
        ContaUsuario contaUsuario = contaUsuarioRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Nenhuma conta com essa id foi encontrada"));

        //Se achar.. vamos atualizar os dados da conta
        contaUsuario.setNome(conta.nome().toUpperCase());
        contaUsuario.setSaldo(conta.saldo());
        contaUsuario.setTipoConta(conta.tipoConta());

        //Validar o subtipo de conta, de acordo com o que o usuário escolheu
        contaUsuario.associarTipoConta(conta.tipoConta());

        //Verificar se o usuário não digitou valores nulos ou vazios
        if (contaUsuario.getNome() == null || contaUsuario.getNome().isEmpty() ||
                contaUsuario.getSaldo() == null || contaUsuario.getSaldo().compareTo(BigDecimal.valueOf(0.0)) <= 0) {
            throw new IllegalArgumentException("Por favor, preencha todos os campos obrigatórios");
        }

        //Verificar se esse nome, e saldo conta passado do parâmetro já não existe no banco um igual
        if (contaUsuarioRepository.existsByNomeAndSaldo(contaUsuario.getNome(), contaUsuario.getSaldo())) {
            throw new IllegalArgumentException("Já existe uma conta com esse nome e saldo criados");
        }

        //Salvar os dados atualizados da conta do usuário
        contaUsuarioRepository.save(contaUsuario);

        //Retornar os dados atualizados
        return contaUsuario;
    }

    @Override
    @Transactional
    public ContaUsuario removerContaPorId(Long id) {
        //encontrar a categoria pela id
        ContaUsuario contaEncontrada = contaUsuarioRepository.findById(id).orElseThrow(() -> new
                NoSuchElementException("Nenhuma conta de usuário foi encontrado com esta id informada"));


        contaUsuarioRepository.delete(contaEncontrada);

        //Retornar o valor do pagamento que foi deletado
        return contaEncontrada;
    }
}
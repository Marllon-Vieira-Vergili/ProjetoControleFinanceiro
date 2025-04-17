package com.marllon.vieira.vergili.catalogo_financeiro.services.InterfacesImplement;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.ContaUsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.ContaUsuarioResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.models.ContaUsuario;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.ContaUsuarioRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.services.Interfaces.ContaUsuarioService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;


@Service
public class ContaUsuarioImpl implements ContaUsuarioService {

    @Autowired
    private ContaUsuarioRepository contaUsuarioRepository;

    @Override
    @Transactional
    public ContaUsuarioResponse criarNovaConta(ContaUsuarioRequest conta) {

        //Criar uma nova conta
        ContaUsuario novaConta = new ContaUsuario();
        novaConta.setNome(conta.nome().toUpperCase());
        novaConta.setSaldo(conta.saldo());


        //Verificar se esse nome, e saldo conta passado do parâmetro já não existe no banco um igual
            if(contaUsuarioRepository.existsByNomeAndSaldo(novaConta.getNome(), novaConta.getSaldo())){
                    throw new IllegalArgumentException("Já existe uma conta com esse nome e saldo criados");
        }
        //Salvar a novaConta, se estiver tudo certo
        contaUsuarioRepository.save(novaConta);

        //Retornar a resposta
        return new ContaUsuarioResponse(novaConta.getId(), novaConta.getNome(), novaConta.getSaldo());
    }

    @Override
    public ContaUsuarioResponse encontrarContaPorId(Long id) {

        //Encontrar a conta pela id
        ContaUsuario contaEncontrada = contaUsuarioRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Nenhuma conta encontrada com a ID informada"));

        //Se não tiver nenhuma conta no banco de dados
        if(contaUsuarioRepository.findAll().isEmpty()){
            throw  new NullPointerException("Não há nenhuma conta no banco de dados, ele está vazio");
        }
        //Retornar os dados da id encontrada
        return new ContaUsuarioResponse(contaEncontrada.getId(), contaEncontrada.getNome(),
                contaEncontrada.getSaldo());
    }

    @Override
    public List<ContaUsuarioResponse> encontrarTodasContas() {

        //Encontrando todas as contas
        List<ContaUsuario> todasContasEncontradas = contaUsuarioRepository.findAll();

        //Se não tiver nenhuma conta encontrada, retornar Exception
        if(todasContasEncontradas.isEmpty()){
            throw new NullPointerException("Não há nenhuma conta no banco de dados, ele está vazio");
        }

        //Retornar a lista de todas as contas encontradas
        return todasContasEncontradas.stream().map(contaUsuario ->
                new ContaUsuarioResponse(contaUsuario.getId(), contaUsuario.getNome(), contaUsuario.getSaldo())).toList();
    }


    @Override
    @Transactional
    public ContaUsuarioResponse atualizarConta(Long id, ContaUsuarioRequest conta) {

        //Encontrando a conta que quero atualizar, pela sua id
        ContaUsuario contaUsuario = contaUsuarioRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Nenhuma conta com essa id foi encontrada"));

        //Se achar.. vamos atualizar os dados da conta
        contaUsuario.setNome(conta.nome().toUpperCase());
        contaUsuario.setSaldo(conta.saldo());

        //Verificar se esse nome, e saldo conta passado do parâmetro já não existe no banco um igual
        if(contaUsuarioRepository.existsByNomeAndSaldo(contaUsuario.getNome(), contaUsuario.getSaldo())){
            throw new IllegalArgumentException("Já existe uma conta com esse nome e saldo criados");
        }

        //Salvar os dados atualizados da conta do usuário
        contaUsuarioRepository.save(contaUsuario);

        //Retornar os dados atualizados
        return new ContaUsuarioResponse(contaUsuario.getId(), contaUsuario.getNome(),
                contaUsuario.getSaldo());
    }

    @Override
    @Transactional
    public boolean removerContaPorId(Long id) {
        if(contaUsuarioRepository.findById(id).isPresent()){
            contaUsuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

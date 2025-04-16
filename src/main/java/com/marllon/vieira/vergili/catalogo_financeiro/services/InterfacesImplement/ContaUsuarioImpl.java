package com.marllon.vieira.vergili.catalogo_financeiro.services.InterfacesImplement;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.ContaUsuarioRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.ContaUsuarioResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.models.ContaUsuario;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.ContaUsuarioRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.services.Interfaces.ContaUsuarioInterface;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;


@Service
public class ContaUsuarioImpl implements ContaUsuarioInterface {

    @Autowired
    private ContaUsuarioRepository contaUsuarioRepository;

    @Override
    @Transactional
    public ContaUsuarioResponse criarNovaConta(ContaUsuarioRequest conta) {

        //Criar uma nova conta
        ContaUsuario novaConta = new ContaUsuario();
        novaConta.setNome(conta.nome().toUpperCase());
        novaConta.setTipoConta(conta.tipoConta().toUpperCase());
        novaConta.setSaldo(conta.saldo());

        //Verificar se esse nome e tipo de conta passado do parâmetro já não existe no banco um igual
        boolean contaExistente = contaUsuarioRepository.existsByNomeIgnoreCaseAndTipoContaIgnoreCase(novaConta.getNome(), novaConta.getTipoConta());
        if(contaExistente){
            throw new IllegalArgumentException("Essa conta já existe no banco de dados!");
        }

        //Salvar a novaConta, se estiver tudo certo
        contaUsuarioRepository.save(novaConta);

        //Retornar a resposta
        return new ContaUsuarioResponse(novaConta.getId(), novaConta.getNome(), novaConta.getSaldo(),
                novaConta.getTipoConta());
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
                contaEncontrada.getSaldo(), contaEncontrada.getTipoConta());
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
                new ContaUsuarioResponse(contaUsuario.getId(), contaUsuario.getNome(), contaUsuario.getSaldo(),
                        contaUsuario.getTipoConta())).toList();
    }

    @Override
    public ContaUsuarioResponse encontrarContaPorNome(String nome) {

        //Encontrar a conta pelo nome
        ContaUsuario contaEncontrada = contaUsuarioRepository.encontrarContaPeloNome(nome);

        //Se não tiver nenhuma conta no banco de dados
        if(contaEncontrada == null){
            throw  new NoSuchElementException("Essa conta não foi encontrada no banco de dados");
        }

        //Retornar os dados da conta pelo nome encontrado
        return new ContaUsuarioResponse(contaEncontrada.getId(), contaEncontrada.getNome(),
                contaEncontrada.getSaldo(), contaEncontrada.getTipoConta());


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
        contaUsuario.setTipoConta(conta.tipoConta().toUpperCase());

        //Salvar os dados atualizados da conta do usuário
        contaUsuarioRepository.save(contaUsuario);
        return new ContaUsuarioResponse(contaUsuario.getId(), contaUsuario.getNome(),
                contaUsuario.getSaldo(), contaUsuario.getTipoConta());
    }

    @Override
    @Transactional
    public boolean removerContaPorId(Long id) {

        //Deletar a conta com a id informada
        try{
            contaUsuarioRepository.deleteById(id);
        } catch (Exception e) {
            throw new NoSuchElementException("Nenhuma conta com essa id foi localizada para ser deletada");
        }
        return true;
    }

}

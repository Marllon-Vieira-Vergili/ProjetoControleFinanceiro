package com.marllon.vieira.vergili.catalogo_financeiro.services.InterfacesImplement;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.CategoriaFinanceiraRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.CategoriaFinanceiraResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.entities.HistoricoTransacaoResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.models.CategoriaFinanceira;
import com.marllon.vieira.vergili.catalogo_financeiro.models.HistoricoTransacao;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.Despesas;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.Receitas;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.CategoriaFinanceiraRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.services.Interfaces.CategoriaFinanceiraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias.DESPESA;
import static com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias.RECEITA;


/**
 *
 * REVISAR TUDO ISSO AQUI
 */
@Service
public class CategoriaFinanceiraImpl implements CategoriaFinanceiraService {


    @Autowired
    private CategoriaFinanceiraRepository categoriaFinanceiraRepository;

    @Override
    public CategoriaFinanceiraResponse criarCategoria(CategoriaFinanceiraRequest categoria) {
        // Instanciar uma nova categoria
        CategoriaFinanceira novaCategoria = new CategoriaFinanceira();
        novaCategoria.setTiposCategorias(categoria.tipoCategoria());

        // Validar o subtipo dependendo do tipo de categoria
        if (novaCategoria.getTiposCategorias().equals(RECEITA)) {
            boolean valido = Arrays.stream(Receitas.values())
                    .map(Enum::name)
                    .anyMatch(valor -> valor.equalsIgnoreCase(categoria.subtipo()));
            if (!valido) {
                throw new IllegalArgumentException("Subtipo inválido para RECEITA. Valores permitidos: " +
                        Arrays.toString(Receitas.values()));
            }
        } else if (novaCategoria.getTiposCategorias().equals(DESPESA)) {
            boolean valido = Arrays.stream(Despesas.values())
                    .map(Enum::name)
                    .anyMatch(valor -> valor.equalsIgnoreCase(categoria.subtipo()));
            if (!valido) {
                throw new IllegalArgumentException("Subtipo inválido para DESPESA. Valores permitidos: " +
                        Arrays.toString(Despesas.values()));
            }
        } else {
            throw new IllegalArgumentException("Tipo de categoria inválido. Use apenas RECEITA ou DESPESA.");
        }

        // Atualizar o subtipo após validação
        novaCategoria.setSubTipo(categoria.subtipo().toUpperCase());

        // Salvar a nova categoria no banco de dados
        categoriaFinanceiraRepository.save(novaCategoria);

        // Retornar a resposta ao usuário
        return new CategoriaFinanceiraResponse(
                novaCategoria.getId(),
                novaCategoria.getTiposCategorias(),
                novaCategoria.getSubTipo());
    }

    @Override
    public CategoriaFinanceiraResponse encontrarCategoriaPorId(Long id) {

        //Verificar se existe uma categoria de conta com o id informado, senao retornar exceção
        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Nenhuma categoria foi encontrada com essa id!"));

        //Retornar a categoria, se encontrada
        return new CategoriaFinanceiraResponse(categoriaEncontrada.getId(),
                categoriaEncontrada.getTiposCategorias(), categoriaEncontrada.getSubTipo());
    }

    @Override
    public List<CategoriaFinanceiraResponse> encontrarTodasCategorias() {

        //Encontrar todas as categorias ou retornar uma exceção se não forem encontradas
        List<CategoriaFinanceira> todasCategorias = categoriaFinanceiraRepository.findAll();

        if(todasCategorias.isEmpty()){
            throw new NoSuchElementException("não há nenhuma categoria financeira na lista");
        }
        //Retornar todas as categorias financeiras
        return todasCategorias.stream().map(categoriaFinanceira ->
                new CategoriaFinanceiraResponse(categoriaFinanceira.getId(),
                        categoriaFinanceira.getTiposCategorias(), categoriaFinanceira.getSubTipo())).toList();
    }

    @Override
    public CategoriaFinanceiraResponse atualizarCategoria(Long id, CategoriaFinanceiraRequest categoria) {

        //Encontrar a id da categoria que será atualizada
        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Nenhuma categoria foi encontrada com essa id!"));

        //Se a categoria for encontrada.. instanciar os novos valores
        categoriaEncontrada.setTiposCategorias(categoria.tipoCategoria());

        //Dependendo da categoria que o usuário escolher, verificar o subtipo deste valor e associar
        // Validar o subtipo dependendo do tipo de categoria
        if (categoriaEncontrada.getTiposCategorias().equals(RECEITA)) {
            boolean valido = Arrays.stream(Receitas.values())
                    .map(Enum::name)
                    .anyMatch(valor -> valor.equalsIgnoreCase(categoria.subtipo()));

            if (!valido) {
                throw new IllegalArgumentException("Subtipo inválido para RECEITA. Valores permitidos: " +
                        Arrays.toString(Receitas.values()));
            }
        } else if (categoriaEncontrada.getTiposCategorias().equals(DESPESA)) {
            boolean valido = Arrays.stream(Despesas.values())
                    .map(Enum::name)
                    .anyMatch(valor -> valor.equalsIgnoreCase(categoria.subtipo()));
            if (!valido) {
                throw new IllegalArgumentException("Subtipo inválido para DESPESA. Valores permitidos: " +
                        Arrays.toString(Despesas.values()));
            }

        } else {
            throw new IllegalArgumentException("Tipo de categoria inválido. Use apenas RECEITA ou DESPESA.");
        }

        categoriaEncontrada.setSubTipo(categoria.subtipo().toUpperCase());
        //Salvar os novos valores
        categoriaFinanceiraRepository.save(categoriaEncontrada);

        //retornar ao usuário
        return new CategoriaFinanceiraResponse(categoriaEncontrada.getId(), categoriaEncontrada.getTiposCategorias(),
                categoriaEncontrada.getSubTipo());
    }

    @Override
    public CategoriaFinanceiraResponse removerCategoria(Long id) {

        //encontrar a categoria pela id
        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraRepository.findById(id).orElseThrow(() -> new
                NoSuchElementException("Nenhuma categoria financeira foi encontrado com esta id informada"));

        //Remover a categoria financeira encontrada
        categoriaFinanceiraRepository.delete(categoriaEncontrada);

        //Retornar o valor do pagamento que foi deletado
        return new CategoriaFinanceiraResponse(categoriaEncontrada.getId(), categoriaEncontrada.getTiposCategorias(),
                categoriaEncontrada.getSubTipo());
    }


    @Override
    public List<CategoriaFinanceiraResponse> encontrarCategoriasPorTipo(TiposCategorias tipo) {
        // Encontrar todas as categorias pelo tipo
        List<CategoriaFinanceira> todasCategorias = categoriaFinanceiraRepository.encontrarPorTipoCategoria(tipo);

        // Validar se existem categorias com o tipo fornecido
        if (todasCategorias.isEmpty()) {
            throw new NoSuchElementException("Não há nenhuma categoria desse tipo no banco de dados!");
        }

        // Verificar o tipo e validar os subtipos
        if (tipo.equals(TiposCategorias.RECEITA)) {
            boolean receitaValida = todasCategorias.stream()
                    .map(CategoriaFinanceira::getSubTipo)
                    .anyMatch(subtipo -> Arrays.stream(Receitas.values())
                            .map(Enum::name)
                            .anyMatch(valor -> valor.equalsIgnoreCase(subtipo)));
            if (!receitaValida) {
                throw new NoSuchElementException("Nenhum subtipo válido de receita encontrado!");
            }
        } else if (tipo.equals(TiposCategorias.DESPESA)) {
            boolean despesaValida = todasCategorias.stream()
                    .map(CategoriaFinanceira::getSubTipo)
                    .anyMatch(subtipo -> Arrays.stream(Despesas.values())
                            .map(Enum::name)
                            .anyMatch(valor -> valor.equalsIgnoreCase(subtipo)));
            if (!despesaValida) {
                throw new NoSuchElementException("Nenhum subtipo válido de despesa encontrado!");
            }
        } else {
            throw new IllegalArgumentException("Tipo de categoria inválido. Use apenas RECEITA ou DESPESA.");
        }

        // Mapear as categorias encontradas para a resposta
        return todasCategorias.stream()
                .map(categoriaFinanceira -> new CategoriaFinanceiraResponse(
                        categoriaFinanceira.getId(),
                        categoriaFinanceira.getTiposCategorias(),
                        categoriaFinanceira.getSubTipo()))
                .toList();
    }

}



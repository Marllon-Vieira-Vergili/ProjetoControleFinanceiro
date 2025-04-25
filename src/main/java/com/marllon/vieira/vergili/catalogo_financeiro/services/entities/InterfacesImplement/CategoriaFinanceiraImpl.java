package com.marllon.vieira.vergili.catalogo_financeiro.services.entities.InterfacesImplement;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.entities.CategoriaFinanceiraRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.CategoriaNaoEncontrada;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.SubTIpoNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.TiposCategoriasNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.models.CategoriaFinanceira;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enumerator.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.CategoriaFinanceiraRepository;
import com.marllon.vieira.vergili.catalogo_financeiro.services.entities.Interfaces.CategoriaFinanceiraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
    public CategoriaFinanceira criarCategoria(CategoriaFinanceiraRequest categoria) {
        // Instanciar uma nova categoria
        CategoriaFinanceira novaCategoria = new CategoriaFinanceira();
        novaCategoria.setTiposCategorias(categoria.tipoCategoria());

        // Validar o subtipo dependendo do tipo de categoria
        if (novaCategoria.getTiposCategorias().equals(RECEITA)) {
            boolean valido = Arrays.stream(SubTipoCategoria.values())
                    .map(Enum::name)
                    .anyMatch(valor -> valor.equalsIgnoreCase(String.valueOf(categoria.subtipo())));
            if (!valido) {
                throw new SubTIpoNaoEncontrado("Subtipo inválido para RECEITA. Valores permitidos: " +
                        Arrays.toString(TiposCategorias.mostrarTodasReceitas().toArray()));
            }
        } else if (novaCategoria.getTiposCategorias().equals(DESPESA)) {
            boolean valido = Arrays.stream(SubTipoCategoria.values())
                    .map(Enum::name)
                    .anyMatch(valor -> valor.equalsIgnoreCase(String.valueOf(categoria.subtipo())));
            if (!valido) {
                throw new SubTIpoNaoEncontrado("Subtipo inválido para DESPESA. Valores permitidos: " +
                        Arrays.toString(TiposCategorias.mostrarTodasDespesas().toArray()));
            }
        } else {
            throw new TiposCategoriasNaoEncontrado("Tipo de categoria inválido. Use apenas RECEITA ou DESPESA.");
        }

        // Atualizar o subtipo após validação
        novaCategoria.setSubTipo(categoria.subtipo());

        // Salvar a nova categoria no banco de dados
        categoriaFinanceiraRepository.save(novaCategoria);

        // Retornar a resposta ao usuário
        return novaCategoria;
    }

    @Override
    public CategoriaFinanceira encontrarCategoriaPorId(Long id) {

        //Retornar a categoria, se encontrada
        return categoriaFinanceiraRepository.findById(id).orElseThrow(() ->
                new CategoriaNaoEncontrada("Nenhuma categoria foi encontrada com essa id!"));
    }

    @Override
    public List<TiposCategorias> encontrarTodasCategorias() {
        return Arrays.asList(TiposCategorias.values());
    }


    @Override
    public CategoriaFinanceira atualizarValoresCategoria(Long id, CategoriaFinanceiraRequest categoria) {

        //Encontrar a id da categoria que será atualizada
        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraRepository.findById(id).orElseThrow(() ->
                new CategoriaNaoEncontrada("Nenhuma categoria foi encontrada com essa id!"));

        //Se a categoria for encontrada.. instanciar os novos valores
        categoriaEncontrada.setTiposCategorias(categoria.tipoCategoria());


        //Dependendo da categoria que o usuário escolher, verificar o subtipo deste valor e associar
        // Validar o subtipo dependendo do tipo de categoria
        if (categoriaEncontrada.getTiposCategorias().equals(RECEITA)) {
            boolean valido = Arrays.stream(SubTipoCategoria.values())
                    .map(Enum::name)
                    .anyMatch(valor -> valor.equalsIgnoreCase(String.valueOf(categoria.subtipo())));

            if (!valido) {
                throw new SubTIpoNaoEncontrado("Subtipo inválido para RECEITA. Valores permitidos: " +
                        Arrays.toString(TiposCategorias.mostrarTodasReceitas().toArray()));
            }
        } else if (categoriaEncontrada.getTiposCategorias().equals(DESPESA)) {
            boolean valido = Arrays.stream(SubTipoCategoria.values())
                    .map(Enum::name)
                    .anyMatch(valor -> valor.equalsIgnoreCase(String.valueOf(categoria.subtipo())));
            if (!valido) {
                throw new SubTIpoNaoEncontrado("Subtipo inválido para DESPESA. Valores permitidos: " +
                        Arrays.toString(TiposCategorias.mostrarTodasDespesas().toArray()));
            }

        } else {
            throw new TiposCategoriasNaoEncontrado("Tipo de categoria inválido. Use apenas RECEITA ou DESPESA.");
        }

        categoriaEncontrada.setSubTipo(categoria.subtipo());
        //Salvar os novos valores
        categoriaFinanceiraRepository.save(categoriaEncontrada);

        //retornar ao usuário
        return categoriaEncontrada;
    }

    @Override
    public CategoriaFinanceira removerCategoria(Long id) {

        //encontrar a categoria pela id
        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraRepository.findById(id).orElseThrow(() -> new
                CategoriaNaoEncontrada("Nenhuma categoria financeira foi encontrado com esta id informada"));

        //Remover a categoria financeira encontrada
        categoriaFinanceiraRepository.delete(categoriaEncontrada);

        //Retornar o valor do pagamento que foi deletado
        return categoriaEncontrada;
    }


    @Override
    public List<SubTipoCategoria> encontrarCategoriasPorTipo(TiposCategorias tipo) {
        // Encontrar todas as categorias pelo tipo
        List<CategoriaFinanceira> todasCategorias = categoriaFinanceiraRepository.encontrarPorTipoCategoria(tipo);

        // Validar se existem categorias com o tipo fornecido
        if (todasCategorias.isEmpty()) {
            throw new CategoriaNaoEncontrada("Não há nenhuma categoria desse tipo no banco de dados!");
        }

        // Verificar o tipo e validar os subtipos
        if (tipo.equals(TiposCategorias.RECEITA)) {
            return TiposCategorias.mostrarTodasReceitas();
            // Mapear as categorias encontradas para a resposta

        } else if (tipo.equals(TiposCategorias.DESPESA)) {
            // Mapear as categorias encontradas para a resposta
            return TiposCategorias.mostrarTodasDespesas();
        } else {
            throw new TiposCategoriasNaoEncontrado("Tipo de categoria inválido. Use apenas RECEITA ou DESPESA.");

        }
    }

    @Override
    public CategoriaFinanceira encontrarCategoriaETipo(TiposCategorias tipoCategoria, SubTipoCategoria subTipoCategoria) {
        try{
            return categoriaFinanceiraRepository.findByTipoAndSubtipo(tipoCategoria, subTipoCategoria);
        } catch (Exception e) {
            throw new CategoriaNaoEncontrada("Não foi encontrado nenhuma categoria e nenhum subtipo de categoria dessa categoria informada");
        }
    }

    public void salvarNovaCategoria(CategoriaFinanceira novaCategoria){
        categoriaFinanceiraRepository.save(novaCategoria);
    }
}



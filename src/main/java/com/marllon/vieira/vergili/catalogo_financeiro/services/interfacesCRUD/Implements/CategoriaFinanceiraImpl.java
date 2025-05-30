package com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.Implements;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.CategoriaFinanceiraRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.CategoriaFinanceiraResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.AssociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DadosInvalidosException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.DesassociationErrorException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.JaExisteException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.CategoriaNaoEncontrada;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.TiposCategoriasNaoEncontrado;
import com.marllon.vieira.vergili.catalogo_financeiro.mapper.CategoriaFinanceiraMapper;
import com.marllon.vieira.vergili.catalogo_financeiro.models.*;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.SubTipoCategoria;
import com.marllon.vieira.vergili.catalogo_financeiro.models.enums.TiposCategorias;
import com.marllon.vieira.vergili.catalogo_financeiro.repository.*;
import com.marllon.vieira.vergili.catalogo_financeiro.services.AssociationsLogical.CategoriaFinanceiraAssociation;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.CategoriaFinanceiraService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoriaFinanceiraImpl implements CategoriaFinanceiraService{

    @Autowired
    private CategoriaFinanceiraRepository categoriaFinanceiraRepository;

    @Autowired
    private CategoriaFinanceiraAssociation categoriaAssociation;

    @Autowired
    private PagamentosRepository pagamentosRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ContaUsuarioRepository contaUsuarioRepository;

    @Autowired
    private HistoricoTransacaoRepository historicoTransacaoRepository;

    @Autowired
    private CategoriaFinanceiraMapper mapper;




    @Override
    public CategoriaFinanceiraResponse criarCategoriaFinanceira(CategoriaFinanceiraRequest request) {

        //Verificar se já não existe a mesma categoria criada
        jaExisteUmaCategoriaIgual(request.tipoCategoria(),request.subtipo());

        //Verificar se o tipo categoria é compativel com o subtipo informado
        validarCompatibilidadeTipoESubtipo(request.tipoCategoria(),request.subtipo());

        //Se achar, será criado a categoria financeira
        CategoriaFinanceira novaCategoria = new CategoriaFinanceira();

        novaCategoria.setTiposCategorias(request.tipoCategoria());
        novaCategoria.setSubTipo(request.subtipo());

        //Salvar a nova categoria criada
        CategoriaFinanceira categoriaSalva = categoriaFinanceiraRepository.save(novaCategoria);

        return mapper.retornarDadosCategoria(categoriaSalva);
    }

    @Override
    public Optional<CategoriaFinanceiraResponse> encontrarCategoriaPorId(Long id) {
        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraRepository.findById(id).orElseThrow(() ->
                new CategoriaNaoEncontrada("Categoria financeira com essa Id não foi encontrada na base de dados"));
        return Optional.ofNullable(mapper.retornarDadosCategoria(categoriaEncontrada));
    }


    @Override
    public CategoriaFinanceiraResponse encontrarCategoriaCriadaPeloSubtipo(SubTipoCategoria subTipo) {

        CategoriaFinanceira categoriaEncontradaPeloSubtipo =
                        categoriaFinanceiraRepository.encontrarCategoriaPeloSubTipo(subTipo);

        if (categoriaEncontradaPeloSubtipo == null){
            throw new TiposCategoriasNaoEncontrado("Não há nenhuma categoria financeira encontrada" +
                    " com esse subtipo de categoria no banco de dados!");
        }

        return mapper.retornarDadosCategoria(categoriaEncontradaPeloSubtipo);
    }

    @Override
    public Page<CategoriaFinanceiraResponse> encontrarTodasCategorias(Pageable pageable) {

        List<CategoriaFinanceira> todosEncontrados = categoriaFinanceiraRepository.findAll();
        Page<CategoriaFinanceira> paginasCategoriasCriadas = new PageImpl<>(todosEncontrados);

        if(todosEncontrados.isEmpty()){
            throw new CategoriaNaoEncontrada("Não foi encontrado nenhuma categoria financeira na base de dados");
        }

        return paginasCategoriasCriadas.map(mapper::retornarDadosCategoria);
    }

    @Override
    public CategoriaFinanceiraResponse atualizarUmaCategoriaCriada(Long idCategoria,
                                                                   TiposCategorias tiposCategoria,
                                                                   SubTipoCategoria subTipo) {

        CategoriaFinanceira categoriaSerAtualizada = categoriaFinanceiraRepository.findById(idCategoria).orElseThrow(() ->
                new CategoriaNaoEncontrada("A categoria não foi encontrada com essa id"));

        if (jaExisteUmaCategoriaIgual(tiposCategoria,subTipo)){
            throw new JaExisteException("Já existe uma Categoria Financeira criada com esses mesmos valores");
        }

            categoriaSerAtualizada.setTiposCategorias(tiposCategoria);
            categoriaSerAtualizada.setSubTipo(subTipo);


        //Salvando os dados
        categoriaFinanceiraRepository.save(categoriaSerAtualizada);
        return mapper.retornarDadosCategoria(categoriaSerAtualizada);
    }

    @Override
    public void deletarCategoria(Long id) {

        //Encontrando a categoria pela id
        CategoriaFinanceira categoriaEncontrada = categoriaFinanceiraRepository.findById(id).orElseThrow(()
                -> new CategoriaNaoEncontrada("Não foi encontrado nenhuma categoria com essa id informada"));

        //Desassociar essa categoria que será deletada, de seus relacionamentos

        Usuario usuarioEncontrado = categoriaEncontrada.getUsuarioRelacionado();
        try{
            categoriaAssociation.desassociarCategoriaUsuario(categoriaEncontrada.getId(), usuarioEncontrado.getId());
        } catch (RuntimeException e) {
            throw new DesassociationErrorException("Erro ao desassociar a categoria de Usuario");
        }

        //Desassociando de conta de usuário
        ContaUsuario contaUsuarioEncontrado = categoriaEncontrada.getContaRelacionada();
        try{
            categoriaAssociation.desassociarCategoriaAConta(categoriaEncontrada.getId(), contaUsuarioEncontrado.getId());
        } catch (RuntimeException e) {
            throw new DesassociationErrorException("Erro ao desassociar a categoria de Conta de usuário");
        }

        //Desassociando de Pagamento encontrado
        List<Pagamentos> pagamentosEncontradosRelacionados = categoriaEncontrada.getPagamentosRelacionados();
        try{
            for(Pagamentos pagamentoLocalizado: pagamentosEncontradosRelacionados){
                categoriaAssociation.desassociarCategoriaAPagamento(categoriaEncontrada.getId(), pagamentoLocalizado.getId());
            }
        } catch (RuntimeException e) {
            throw new DesassociationErrorException("Erro ao desassociar a categoria de Pagamento");
        }

        //Desassociando de Histórico de Transação encontrado
        List<HistoricoTransacao> historicosEncontradosRelacionados = categoriaEncontrada.getTransacoesRelacionadas();
        try{
            for(HistoricoTransacao historicoEncontrado: historicosEncontradosRelacionados){
                categoriaAssociation.desassociarCategoriaTransacao(categoriaEncontrada.getId(), historicoEncontrado.getId());
            }
        } catch (RuntimeException e) {
            throw new DesassociationErrorException("Erro ao desassociar a categoria de Histórico de Transação");
        }
        //Deletar a categoria encontrada
        categoriaFinanceiraRepository.deleteById(categoriaEncontrada.getId());
    }



    @Override
    public boolean validarCompatibilidadeTipoESubtipo(TiposCategorias tipoCategoria, SubTipoCategoria subTipoCategoria) {
        if(subTipoCategoria.getTiposCategorias() != tipoCategoria){
            throw new DadosInvalidosException("Tipos de categorias são incompatíveis um com o outro");
        }
        return true;
    }

    @Override
    public boolean jaExisteUmaCategoriaIgual(TiposCategorias tiposCategoria, SubTipoCategoria subTipoCategoria){

        CategoriaFinanceira categoriaIgual = categoriaFinanceiraRepository
                .encontrarPorTipoAndSubtipo(tiposCategoria,subTipoCategoria);

        if (categoriaIgual != null &&
                categoriaIgual.getTiposCategorias() == (tiposCategoria)
                && categoriaIgual.getSubTipo() == (subTipoCategoria)){
            return true;
        }
        return false;
    }
}
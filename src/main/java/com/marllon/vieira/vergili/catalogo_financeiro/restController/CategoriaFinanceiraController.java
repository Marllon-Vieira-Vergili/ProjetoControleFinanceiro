package com.marllon.vieira.vergili.catalogo_financeiro.restController;

import com.marllon.vieira.vergili.catalogo_financeiro.DTO.request.CategoriaFinanceiraRequest;
import com.marllon.vieira.vergili.catalogo_financeiro.DTO.response.CategoriaFinanceiraResponse;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.RestControllerAPIException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.CategoriaNaoEncontrada;
import com.marllon.vieira.vergili.catalogo_financeiro.services.interfacesCRUD.CategoriaFinanceiraService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;

import static org.springframework.web.servlet.function.ServerResponse.ok;
import static org.springframework.web.servlet.function.ServerResponse.status;

/**
 * Controlador REST responsável por expor os endpoints da API relacionados às categorias financeiras.
 *
 * Os métodos que serão adicionados aqui irão permitir operações como criação, atualização,
 * listagem, busca e exclusão de categorias financeiras, além de associações com outras entidades.
 */
@RestController
@RequestMapping(value = "/api/categoria")
@Tag(name = "Métodos da Categoria Financeira", description = "Operações relacionadas ao gerenciamento de categorias financeiras, como receitas e despesas.")
public class CategoriaFinanceiraController {

    @Autowired
    private CategoriaFinanceiraService categoriaService;




    @PostMapping(value = "/criarCategoria/{pagamentoId}/{historicoTransacaoId}/{contaUsuarioId}/{usuarioId}")
    public ResponseEntity<CategoriaFinanceiraResponse> adicionarNovaCategoria
            (@RequestBody CategoriaFinanceiraRequest dadosCategoria,
             @PathVariable Long pagamentoId,
             @PathVariable Long historicoTransacaoId,
             @PathVariable Long contaUsuarioId,
             @PathVariable Long usuarioId) {

        CategoriaFinanceiraResponse response;
        try {
            response = categoriaService.criarCategoriaFinanceira(dadosCategoria, pagamentoId, historicoTransacaoId, contaUsuarioId, usuarioId);
        } catch (RestControllerAPIException e) {
            throw new RestControllerAPIException();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
        //    // Métodos de manipulação de CategoriaFinanceira serão implementados aqui futuramente.
    }

    @GetMapping(value = "/mostrarTipoCategoriaPeloId/{id}")
    public ResponseEntity<CategoriaFinanceiraResponse>
    encontrarCategoriaCriadaPeloId(@PathVariable Long id) throws CategoriaNaoEncontrada {
        CategoriaFinanceiraResponse response = categoriaService.encontrarCategoriaPorId(id);
        return ResponseEntity.status(HttpStatus.FOUND).body(response);
    }

    @GetMapping(value = "/mostrarTodasAsPaginas")
    public ResponseEntity<Page<CategoriaFinanceiraResponse>> encontrarTodosEmPaginas(Pageable pageable) {
        Page<CategoriaFinanceiraResponse> response = categoriaService.encontrarTodasCategorias(pageable);
        if(response.isEmpty()){
            throw new EmptyResultDataAccessException("Não há nenhum valor no banco de dados",0);
        }
        return ResponseEntity.ok(response);
    }
}

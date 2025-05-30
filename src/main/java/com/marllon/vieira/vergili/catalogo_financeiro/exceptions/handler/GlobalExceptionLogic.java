package com.marllon.vieira.vergili.catalogo_financeiro.exceptions.handler;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.JaExisteException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Classe responsável pelo tratamento global das exceções lançadas durante a execução da aplicação.
 * Utiliza a anotação {@link RestControllerAdvice} para capturar exceções e retornar respostas HTTP apropriadas.
 */
@RestControllerAdvice
public class GlobalExceptionLogic {

    // ======================== EXCEÇÕES DE ENTIDADES NÃO ENCONTRADAS ========================

    /**
     * Trata exceções de categoria financeira não encontrada.
     *
     * @param exception CategoriaNaoEncontrada
     * @return ResponseEntity com status 404 e mensagem da exceção.
     */
    @ExceptionHandler(CategoriaNaoEncontrada.class)
    public ResponseEntity<String> categoriaNaoEncontradaException(CategoriaNaoEncontrada exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    /**
     * Trata exceções de conta não encontrada.
     *
     * @param exception ContaNaoEncontrada
     * @return ResponseEntity com status 404 e mensagem da exceção.
     */
    @ExceptionHandler(ContaNaoEncontrada.class)
    public ResponseEntity<String> contaNaoEncontradaException(ContaNaoEncontrada exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    /**
     * Trata exceções de usuário não encontrado.
     *
     * @param exception UsuarioNaoEncontrado
     * @return ResponseEntity com status 404 e mensagem da exceção.
     */
    @ExceptionHandler(UsuarioNaoEncontrado.class)
    public ResponseEntity<String> usuarioNaoEncontrado(UsuarioNaoEncontrado exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    /**
     * Trata exceções de pagamento não encontrado.
     *
     * @param exception PagamentoNaoEncontrado
     * @return ResponseEntity com status 404 e mensagem da exceção.
     */
    @ExceptionHandler(PagamentoNaoEncontrado.class)
    public ResponseEntity<String> pagamentoNaoEncontradaException(PagamentoNaoEncontrado exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    /**
     * Trata exceções de histórico de transação não encontrado.
     *
     * @param exception HistoricoTransacaoNaoEncontrado
     * @return ResponseEntity com status 404 e mensagem da exceção.
     */
    @ExceptionHandler(HistoricoTransacaoNaoEncontrado.class)
    public ResponseEntity<String> historicoTransacaoNaoEncontradaException(HistoricoTransacaoNaoEncontrado exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    /**
     * Trata exceções de tipo de categoria não encontrado.
     *
     * @param exception TiposCategoriasNaoEncontrado
     * @return ResponseEntity com status 404 e mensagem da exceção.
     */
    @ExceptionHandler(TiposCategoriasNaoEncontrado.class)
    public ResponseEntity<String> tiposCategoriasNaoEncontradaException(TiposCategoriasNaoEncontrado exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    /**
     * Trata exceções de tipo de conta não encontrado.
     *
     * @param exception TiposContasNaoEncontrado
     * @return ResponseEntity com status 404 e mensagem da exceção.
     */
    @ExceptionHandler(TiposContasNaoEncontrado.class)
    public ResponseEntity<String> tiposContasNaoEncontradaException(TiposContasNaoEncontrado exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    /**
     * Trata exceções de subtipo não encontrado ao tentar associar com uma categoria.
     *
     * @param exception SubTipoNaoEncontrado
     * @return ResponseEntity com status 404 e mensagem da exceção.
     */
    @ExceptionHandler(SubTipoNaoEncontrado.class)
    public ResponseEntity<String> subtipoNaoEncontradaException(SubTipoNaoEncontrado exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    // ======================== EXCEÇÕES DE CONFLITO ========================

    /**
     * Trata exceções lançadas quando uma entidade já existe.
     *
     * @param exception JaExisteException
     * @return ResponseEntity com status 409 (CONFLICT) e mensagem da exceção.
     */
    @ExceptionHandler(JaExisteException.class)
    public ResponseEntity<String> jaExisteException(JaExisteException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }
}

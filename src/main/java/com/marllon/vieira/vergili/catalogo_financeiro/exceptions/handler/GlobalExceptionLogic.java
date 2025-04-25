package com.marllon.vieira.vergili.catalogo_financeiro.exceptions.handler;

import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.custom.JaExisteException;
import com.marllon.vieira.vergili.catalogo_financeiro.exceptions.entitiesExc.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionLogic {


    /**
     * Lógicas customizadas para exceções das entidades que não forem encontradas no banco de dados
     * @return ResponseEntity
     * 404 NotFound exception de cada entidade separadamente, para o usuário, na sua requisição HTTP
     */
    @ExceptionHandler(CategoriaNaoEncontrada.class)
    public ResponseEntity<String> categoriaNaoEncontradaException(CategoriaNaoEncontrada exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(ContaNaoEncontrada.class)
    public ResponseEntity<String> contaNaoEncontradaException(ContaNaoEncontrada exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(UsuarioNaoEncontrado.class)
    public ResponseEntity<String> usuarioNaoEncontrado(UsuarioNaoEncontrado exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(PagamentoNaoEncontrado.class)
    public ResponseEntity<String> pagamentoNaoEncontradaException(PagamentoNaoEncontrado exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(HistoricoTransacaoNaoEncontrado.class)
    public ResponseEntity<String> historicoTransacaoNaoEncontradaException(HistoricoTransacaoNaoEncontrado exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(TiposCategoriasNaoEncontrado.class)
    public ResponseEntity<String> tiposCategoriasNaoEncontradaException(TiposCategoriasNaoEncontrado exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(TiposContasNaoEncontrado.class)
    public ResponseEntity<String> tiposContasNaoEncontradaException(TiposContasNaoEncontrado exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(SubTipoNaoEncontrado.class)
    public ResponseEntity<String> subtipoNaoEncontradaException(SubTipoNaoEncontrado exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }


    /**
     *
     * @param exception
     * @return status 410(Conflict) - Indicando ao usuário que já existe salvo este valor
     */
    @ExceptionHandler(JaExisteException.class)
    public ResponseEntity<String> jaExisteException(JaExisteException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }
}

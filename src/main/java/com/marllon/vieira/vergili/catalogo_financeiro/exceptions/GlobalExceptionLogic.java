package com.marllon.vieira.vergili.catalogo_financeiro.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionLogic {


    /**
     * Lógicas customizadas para exceções das entidades que não forem encontradas no banco de dados
     * @param exception
     * @return
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
    public ResponseEntity<String> pagamentoNaoEncontradaException(HistoricoTransacaoNaoEncontrado exception){
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

    @ExceptionHandler(SubTIpoNaoEncontrado.class)
    public ResponseEntity<String> subtipoNaoEncontradaException(SubTIpoNaoEncontrado exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }


    //Exceção já existe, sua lógica
    @ExceptionHandler(JaExisteException.class)
    public ResponseEntity<String> jaExisteException(JaExisteException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }
}

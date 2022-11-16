package com.estoqueapi.EstoqueApi.Exceptions;

public class MovimentacaoInvalidaException extends RuntimeException{
    public MovimentacaoInvalidaException(String message) {
        super(message);
    }
}

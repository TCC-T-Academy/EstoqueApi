package com.estoqueapi.EstoqueApi.Exceptions;

public class AlteracaoNaoPermitidaException extends RuntimeException{
    public AlteracaoNaoPermitidaException(String message) {
        super(message);
    }
}

package com.estoqueapi.EstoqueApi.Exceptions;

public class AcaoNaoPermitidaException extends RuntimeException{
    public AcaoNaoPermitidaException(String message) {
        super(message);
    }
}

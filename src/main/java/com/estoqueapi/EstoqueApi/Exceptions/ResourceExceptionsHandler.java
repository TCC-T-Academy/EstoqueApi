package com.estoqueapi.EstoqueApi.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionsHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<StandardError> entityNotFound(EntityNotFoundException e,
                                                        HttpServletRequest req){
        StandardError err = new StandardError();
        err.setTimeStamp(Instant.now());
        err.setStatus(HttpStatus.NOT_FOUND.value());
        err.setError("Recurso não encontrado");
        err.setMessage(e.getMessage());
        err.setPath(req.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> invalidArgument(MethodArgumentNotValidException e,
                                                         HttpServletRequest req){
        StandardError err = new StandardError();
        err.setTimeStamp(Instant.now());
        err.setStatus(HttpStatus.BAD_REQUEST.value());
        err.setError("Campo obrigatório");
        err.setMessage(e.getFieldError().getDefaultMessage());
        err.setPath(req.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(MovimentacaoInvalidaException.class)
    public ResponseEntity<StandardError> movimentacaoInvalida(MovimentacaoInvalidaException e,
                                                         HttpServletRequest req){
        StandardError err = new StandardError();
        err.setTimeStamp(Instant.now());
        err.setStatus(HttpStatus.BAD_REQUEST.value());
        err.setError("Movimentacao Inválida");
        err.setMessage(e.getMessage());
        err.setPath(req.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

}

package com.hackathon.one.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Lançada quando um recurso (ex: Usuario, Transacao) não é encontrado.
// Resulta automaticamente em resposta HTTP 404 Not Found.
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    // Para busca por ID numérico. Ex: Usuario com id 42 não encontrado.
    public ResourceNotFoundException(String recurso, Long id) {
        super(String.format("%s com id %d não encontrado.", recurso, id));
    }

    // Para busca por identificador textual. Ex: Usuario com email 'x@y.com' não encontrado.
    public ResourceNotFoundException(String recurso, String identificador) {
        super(String.format("%s com identificador '%s' não encontrado.", recurso, identificador));
    }
}

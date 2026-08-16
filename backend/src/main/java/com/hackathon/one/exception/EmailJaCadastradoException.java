package com.hackathon.one.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Lançada quando o cadastro é feito com um e-mail que já existe na base.
// Resulta em resposta HTTP 409 Conflict.
@ResponseStatus(HttpStatus.CONFLICT)
public class EmailJaCadastradoException extends RuntimeException {

    public EmailJaCadastradoException(String email) {
        super("E-mail já cadastrado: " + email);
    }
}
